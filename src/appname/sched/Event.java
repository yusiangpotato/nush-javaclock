package appname.sched;


import appname.util.GregCalPlus;
import appname.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private final Logger log = Logger.getAnonymousLogger();
	public final UUID uuid;
    public String name;
    JLabel desc = new JLabel();
    JPanel pane = null;
    private GregCalPlus start;
    private int duration = 0;
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd '@' h:mm:ss a");
    private boolean waitForReady, ready=false;
    private JButton startBtn, editBtn, removeBtn;
    /*
    public Event(GregCalPlus s, GregCalPlus e, String n) {
        if (s==null || e == null) throw new NullPointerException();
        start = s;
        uuid=UUID.randomUUID();
        name = n;
        log.log(Level.INFO, "Created: " + n + " Begins: " + s + " Ends: " + e + " UUID=" + uuid);
        setEnd(e);
    }*/

    public Event(GregCalPlus s, int durationSeconds, String n) { //For backward compatability
        this(s,durationSeconds,n,false);
    }

    public Event(GregCalPlus s, int durationSeconds, String n, boolean waitForReady) {
        start = s;
        duration = durationSeconds;
        name = n;
        uuid=UUID.randomUUID();
        this.waitForReady=waitForReady;
        log.log(Level.INFO, "Created: " + n + " Begins: " + s + " Duration: " + durationSeconds + " sec UUID=" + uuid);

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
        if(getStart()==null || getEnd()==null || !hasActuallyStarted() || hasEnded()){
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

    public boolean isWaitForReady(){
        return waitForReady;
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
        pane = new JPanel(new MigLayout("fill","[33%|33%|33%]"));
        pane.setBackground(new Color(4, 17, 94));
        desc = new JLabel();
        desc.setForeground(new Color(255, 255, 255));
        pane.add(desc, "grow 1, span");

        startBtn = new JButton("");
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                if(waitForReady&&!ready&&!hasStarted()) {
                    //sb.append("Waiting for ready.\n");
                    //startBtn.setText("Ready?");
                    ready=true;
                }
                else if(waitForReady&&!ready&&hasStarted()) {
                    //sb.append("Running late!\n");
                    //startBtn.setText("Begin!");
                    ready=true;
                    setStart(new GregCalPlus());
                }
                else if(waitForReady&&ready&&!hasStarted()) {
                    //sb.append("Ready, waiting for scheduled start.\n");
                    //startBtn.setText("Waiting.");
                    setStart(new GregCalPlus());
                }
                else{
                    //sb.append("Waiting to start.\n");
                    //startBtn.setText("Start");
                    setStart(new GregCalPlus());
                }
            }
        });
        editBtn = new JButton("Edit");
        editBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventManager.getEventManager().edit(uuid);
            }
        });
        removeBtn = new JButton("Remove");
        removeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventManager.getEventManager().remove(uuid);
            }
        });
        pane.add(startBtn,"grow 1");
        pane.add(editBtn,"grow 1");
        pane.add(removeBtn,"grow 1");
        return pane;

    }

    public void refresh() {
        if (pane == null) return;
        //TODO Proper Event Listing, not using toHtmlString()...
        desc.setText(this.toHtmlString());

        if (this.hasEnded()) pane.setBackground(new Color(168, 0, 27));
        else if (this.hasActuallyStarted()) pane.setBackground(new Color(0, 116, 6));
        else if (waitForReady&&!ready&&hasStarted()) pane.setBackground(new Color(175, 121, 0));
        else pane.setBackground(new Color(4, 0, 134));

        if(waitForReady&&!ready&&!hasStarted()) {
            //sb.append("Waiting for ready.\n");
            startBtn.setText("Ready?");
        }
        else if(waitForReady&&!ready&&hasStarted()) {
            //sb.append("Running late!\n");
            startBtn.setText("Begin!");
        }
        else if((!waitForReady||(waitForReady&&ready))&&!hasStarted()) {
            //sb.append("Ready, waiting for scheduled start.\n");
            startBtn.setText("Force start");
        }
        else if (start == null) {
            //sb.append("Waiting to start.\n");
            startBtn.setText("Start Now");
        }else{
            startBtn.setText("Restart");
        }
    }

    public boolean hasActuallyStarted() {
        if(waitForReady&&!ready) return false;
        else return hasStarted();
    }

    public boolean hasStarted() {
        return start == null ? false : new GregCalPlus().afterOrEquals(getStart());
    }

    public boolean hasEnded() {
        if(!hasActuallyStarted()) return false;
        return start == null ? false : new GregCalPlus().afterOrEquals(getEnd());
    }

    public boolean canRemove() {
        if (getEnd() == null) return false;
        if(!hasActuallyStarted()) return false;
        GregCalPlus e = (GregCalPlus) getEnd().clone();
        e.add(GregCalPlus.SECOND, 10);
        return start == null ? false : new GregCalPlus().after(e);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
	    sb.append("Event: ").append(name).append('\n');
        if(waitForReady&&!ready&&!hasStarted()) {
            sb.append("Waiting for ready.\n");

        }
        else if(waitForReady&&!ready&&hasStarted()) {
            sb.append("Running late!\n");
        }
        else if(waitForReady&&ready&&!hasStarted()) {
            sb.append("Ready, waiting for scheduled start.\n");
        }
        else if (start == null) {
            sb.append("Waiting to start.\n");
        }else if(!hasEnded()){
            sb.append("Event is running.\n");
        }else{
            sb.append("Event has ended.\n");
        }

        if(start!=null)
            sb.append("Start: ").append(dateFormat.format(getStart().getTime())).append('\n');

        if(getEnd()!=null)
            sb.append("End:       ").append(dateFormat.format(getEnd().getTime())).append('\n');

	    sb.append("Duration: ").append(Util.secsToExactHMS(getDuration())).append('\n');
        sb.append("Elapsed: ");
        if(hasEnded()) sb.append("Ended");
        else if(hasStarted()) sb.append(Util.secsToExactHMS(getElapsed()));
        else sb.append("Not started yet");

        return sb.toString();
    }

    public String toHtmlString() {

        //return "<html>"+toString().replaceAll("\n","<br>")+"</html>";
        StringBuilder sb = new StringBuilder();

        sb.append("<html><p style=\"font-size: 30\">").append(name).append("</p>");
        if(getStart()!=null&&getEnd()!=null) { //Predefined start/end
            sb.append("<p style=\"font-size: 26\">").append(Util.getTimeString(getStart())).append("-").append(Util.getTimeString(getEnd())).append("</p>");
        }else{
            sb.append("<p style=\"font-size: 22\">").append(Util.secsToExactHMS(getDuration())).append("</p>");
        }

        {
            sb.append("<p>");
            if(waitForReady&&!ready&&!hasStarted()) {
                sb.append("Waiting for ready.");

            }
            else if(waitForReady&&!ready&&hasStarted()) {
                sb.append("Running late!");
            }
            else if((!waitForReady||(waitForReady&&ready))&&!hasStarted()) {
                sb.append("Ready, waiting for scheduled start.");
            }
            else if (start == null) {
                sb.append("Waiting to start.");
            }else if(!hasEnded()){
                sb.append("Event is running.");
            }else{
                sb.append("Event has ended.");
            }
            sb.append("</p>");
        }

        return sb.toString();

    }

    @Override
    public boolean equals(Object obj) {
        return this.uuid==((Event)obj).uuid;
    }
}
