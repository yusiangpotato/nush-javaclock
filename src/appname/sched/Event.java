package appname.sched;


import appname.util.Util;

import javax.swing.*;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.UUID;

/**
 * Created by yusiang on 11/4/14.
 */
public class Event implements Comparable<Event>,Comparator<Event>{
    private GregorianCalendar start,end;
    public final UUID uuid;
    public String name;
    public boolean endIsDuration;
    public Event(GregorianCalendar s , GregorianCalendar e,String n, boolean eid){
        //if(s==null|e==null) throw new NullPointerException();
        start=s; end=e;
        uuid=UUID.randomUUID();
        name=n;
        endIsDuration = eid;
        System.out.println("New Event: "+this.toString());
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
    public boolean hasEnded(){

        return start==null?false:new GregorianCalendar().after(end);
    }
    @Override
    public String toString(){
        String s="Event: "+name+'\n';
        if(start==null) s+= "Manual Start";
        else s+="Autostart: "+ Util.getYear(start)+"-"+Util.getMonth(start)+"-"+Util.getDate(start)+" @ "+
                Util.getHour24(start)+":"+Util.getMinute(start)+":"+Util.getSecond(start);
        s+='\n';
        if(endIsDuration){
            s+="Duration: "+Util.getYear(end)+"yr "+Util.getMonth(end)+"mth "+Util.getDate(end)+"day "+
                    Util.getHour24(end)+"hr "+Util.getMinute(end)+"min "+Util.getSecond(end)+"sec";;
        }else{
            s+="End:       "+Util.getYear(end)+"-"+Util.getMonth(end)+"-"+Util.getDate(end)+" @ "+
                    Util.getHour24(end)+":"+Util.getMinute(end)+":"+Util.getSecond(end);
        }
        return s;
    }

}
