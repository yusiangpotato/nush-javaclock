package appname.sched;


import appname.util.GregCalPlus;
import appname.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yusiang on 11/4/14.
 */
public class Event implements Comparable<Event>, Comparator<Event> {
	private final Logger logger = Logger.getLogger(this.getClass().getName());
	public final UUID uuid;
    public String name;
    JLabel tmp = new JLabel();
    JPanel pane = null;
    private GregCalPlus start;
    private int duration = 0;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd '@' h:mm:ss a");

    public Event(GregCalPlus s, GregCalPlus e, String n) {
        if (e == null) throw new NullPointerException();
        start = s;
        uuid=UUID.randomUUID();
        name = n;
        logger.log(Level.FINE, "Created: " + this.toString());
        setEnd(e);
    }

    public Event(GregCalPlus s, int durationSeconds, String n) {
        start = s;
        duration = durationSeconds;
        name = n;
        uuid=UUID.randomUUID();
        logger.log(Level.FINE, "Created: " + this.toString());

    }

//    public UUID getUuid() {
//        return uuid;
//    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getElapsed(){
        if(getStart()==null || getEnd()==null || !hasStarted() || hasEnded()){
            return -1;
        }
        return Util.safeLongToInt(Util.getDeltaT(start, new GregCalPlus()));
    }

    public GregCalPlus getStart() {
        return start;
    }

    public void setStart(GregCalPlus start) {
        //Convert to duration, then setDuration().
        //setDuration(Util.safeLongToInt(Util.getDeltaT(getStart(), getEnd())));
        this.start = start;
    }

    public GregCalPlus getEnd() {
        if (start == null) return null;
        if (start != null) {
            GregCalPlus g = (GregCalPlus) start.clone();
            g.add(GregorianCalendar.SECOND, duration);
            return g;
        }
        return null;
    }

    public void setEnd(GregCalPlus end) {
        setDuration(Util.safeLongToInt(Util.getDeltaT(getStart(), end)));
    }

    @Override //Comparable
    public int compareTo(Event o) {
        return compare(this, o);
    }

    @Override //Comparator
    public int compare(Event o1, Event o2) {// Bigger means ends later
        if (o1.getEnd() == null && o2.getEnd() != null) return -1;//o1 bigger --> Havent started yet!
        if (o1.getEnd() != null && o2.getEnd() == null) return 1;//o2 bigger
        if (o1.getEnd() == null && o2.getEnd() == null) return 0;//Neither

        return o1.getEnd().compareTo(o2.getEnd());
    }

    public JPanel toPanel() {
        pane = new JPanel(new MigLayout("fill"));
        pane.setBackground(new Color(4, 17, 94));
        tmp = new JLabel();
        tmp.setForeground(new Color(255, 255, 255));
        pane.add(tmp, "grow 1");
        return pane;

    }

    public void refresh() {
        if (pane == null) return;
        //TODO Proper Event Listing, not using toHtmlString()...
        tmp.setText(this.toHtmlString());

        if (this.hasEnded()) pane.setBackground(new Color(94, 0, 13));
        else if (this.hasStarted()) pane.setBackground(new Color(12, 75, 0));
        else pane.setBackground(new Color(4, 17, 94));
    }

    public boolean hasStarted() {
        return start == null ? false : new GregCalPlus().after(getStart());
    }

    public boolean hasEnded() {

        return start == null ? false : new GregCalPlus().after(getEnd());
    }

    public boolean canRemove() {
        if (getEnd() == null) return false;
        GregCalPlus e = (GregCalPlus) getEnd().clone();
        e.add(GregCalPlus.SECOND, 5);
        return start == null ? false : new GregCalPlus().after(e);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
	    sb.append("Event: ").append(name).append('\n');
        if (start == null) sb.append("Waiting to start");
        else
            sb.append("Start: ").append(dateFormat.format(getStart().getTime()));
        sb.append('\n');

        if(getEnd()!=null)
            sb.append("End:       ").append(dateFormat.format(getEnd().getTime())).append('\n');

	    sb.append("Duration: ").append(Util.secsToExactHMS(getDuration())).append('\n');
        sb.append("Elapsed: ");
        if(hasStarted()) sb.append(Util.secsToExactHMS(getElapsed()));
        else if(hasEnded()) sb.append("Ended");
        else sb.append("Not started yet");

        return sb.toString();
    }

    public String toHtmlString() {
	    return "<html>"+toString().replaceAll("\n","<br>")+"</html>";
    }

}
