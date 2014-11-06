package appname.util;

import java.util.GregorianCalendar;

/**
 * Created by yusiang on 11/6/14.
 */
public class GregCalPlus extends GregorianCalendar {
    public GregCalPlus() {
        super();
    }

    public GregCalPlus(int year, int month, int dayOfMonth) {
        super(year, month, dayOfMonth);
    }

    public GregCalPlus(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        super(year, month, dayOfMonth, hourOfDay, minute);
    }

    public GregCalPlus(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        super(year, month, dayOfMonth, hourOfDay, minute, second);
    }

    @Override
    public int get(int field) {
        if(field != GregCalPlus.MONTH)
             return super.get(field);
        else return super.get(field);
    }

    @Override
    public void set(int field, int value) {
        if(field!= GregCalPlus.MONTH)
             super.set(field, value);
        else super.set(field, value);
    }
}
