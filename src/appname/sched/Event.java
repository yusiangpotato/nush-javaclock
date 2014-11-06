package appname.sched;


import appname.util.GregCalPlus;
import appname.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * Created by yusiang on 11/4/14.
 */
public class Event implements Comparable<Event>, Comparator<Event> {
    private GregCalPlus start, end;
    //public final UUID uuid;
    public String name;
    public boolean useDuration;
    private int duration = 0;

    public Event(GregCalPlus s, GregCalPlus e, String n) {
        if (e == null) throw new NullPointerException();
        start = s;
        end = e;
        //uuid=UUID.randomUUID();
        name = n;
        useDuration = false;
        System.out.println("Created: " + this.toString());
    }

    public int getDuration() {
        if (useDuration) return duration;
        else if (start!=null) return Util.safeLongToInt(Util.getDeltaT(start, end));
        else return 0;
    }

    public void setDuration(int duration) {
        useDuration = true;
        end = null;
        this.duration = duration;
    }

    public Event(GregCalPlus s, int durationSeconds, String n) {
        start = s;
        end = null;
        duration = durationSeconds;
        name = n;
        useDuration = true;
        System.out.println("Created: " + this.toString());

    }

//    public UUID getUuid() {
//        return uuid;
//    }

    public GregCalPlus getStart() {
        return start;
    }

    public void setStart(GregCalPlus start) {
        //Convert to duration, then setDuration().
        if (!useDuration) setDuration(Util.safeLongToInt(Util.getDeltaT(getStart(), getEnd())));
        this.start = start;
    }

    public GregCalPlus getEnd() {
        if (useDuration && start == null) return null;
        if (useDuration && start != null) {
            GregCalPlus g = (GregCalPlus) start.clone();
            g.add(GregorianCalendar.SECOND, duration);
            return g;
        }
        if (!useDuration) return end;
        return null;
    }

    public void setEnd(GregCalPlus end) {
        useDuration = false;
        this.end = end;
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

    JLabel tmp = new JLabel();
    JPanel pane = null;

    public JPanel toPanel() {
        //TODO
        pane = new JPanel(new MigLayout("fill"));
        pane.setBackground(new Color(4, 17, 94));
        tmp = new JLabel();
        tmp.setForeground(new Color(255, 255, 255));
        pane.add(tmp, "grow 1");
        return pane;

    }

    public void refresh() {
        if (pane == null) return;
        //TODO
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
        String s = "Event: " + name + '\n';
        if (start == null) s += "Manual Start";
        else
            s += "Autostart: " + Util.getYear(getStart()) + "-" + (Util.getMonth(getStart())) + "-" + Util.getDate(getStart()) + " @ " +
                    Util.getHour24(getStart()) + ":" + Util.getMinute(getStart()) + ":" + Util.getSecond(getStart());
        s += '\n';

        if(getEnd()!=null)
            s += "End:       " + Util.getYear(getEnd()) + "-" + (Util.getMonth(getEnd())) + "-" + Util.getDate(getEnd()) + " @ " +
                Util.getHour24(getEnd()) + ":" + Util.getMinute(getEnd()) + ":" + Util.getSecond(getEnd()) + "\n";

        s += "Duration: " + Util.secsToExactHMS(getDuration());

        return s;
    }

    public String toHtmlString() {
        String s = "<html>Event: " + name + "<br>";
        if (start == null) s += "Manual Start";
        else
            s += "Autostart: " + Util.getYear(getStart()) + "-" + (Util.getMonth(getStart())) + "-" + Util.getDate(getStart()) + " @ " +
                    Util.getHour24(getStart()) + ":" + Util.getMinute(getStart()) + ":" + Util.getSecond(getStart());
        s += "<br>";
        if(getEnd()!=null)
        s += "End:       " + Util.getYear(getEnd()) + "-" + (Util.getMonth(getEnd())) + "-" + Util.getDate(getEnd()) + " @ " +
                Util.getHour24(getEnd()) + ":" + Util.getMinute(getEnd()) + ":" + Util.getSecond(getEnd()) + "<br>";
        s += "Duration: " + Util.secsToExactHMS(getDuration());

        s += "<br></html>";
        return s;
    }

}
