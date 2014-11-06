package appname.sched;


import javax.swing.*;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by yusiang on 11/4/14.
 */
public class Event implements Comparable<Event>,Comparator<Event>{
    GregorianCalendar start,end;
    final UUID uuid;
    String name;
    Event nextEvent=null;
    public Event(GregorianCalendar s, GregorianCalendar e,String n){
        //if(s==null|e==null) throw new NullPointerException();
        start=s; end=e;
        uuid=UUID.randomUUID();
        name=n;
    }

    public UUID getUuid() {
        return uuid;
    }

    public GregorianCalendar getStart() {
        return start;
    }

    public void setStart(GregorianCalendar start) {
        this.start = start;
    }

    public GregorianCalendar getEnd() {
        return end;
    }

    public void setEnd(GregorianCalendar end) {
        this.end = end;
    }

    @Override //Comparable
    public int compareTo(Event o) {
        return compare(this,o);
    }

    @Override //Comparator
    public int compare(Event o1, Event o2) {// Bigger means ends later
        if(o1.getEnd()==null && o2.getEnd()!=null) return -1;//o1 bigger --> Havent started yet!
        if(o1.getEnd()!=null && o2.getEnd()==null) return  1;//o2 bigger
        if(o1.getEnd()==null && o2.getEnd()==null) return  0;//Neither

        return o1.getEnd().compareTo(o2.getEnd());
    }

    public JPanel toPanel(){
        //TODO
        return null;

    }
    @Override
    public String toString(){
        return super.toString();
    }

}
