package appname.util;

import java.util.GregorianCalendar;

/**
 * Created by yusiang on 11/6/14.
 */
public class GregCalPlus extends GregorianCalendar {
    public GregCalPlus() {
        super();
        this.set(MILLISECOND,0);
        //TODO NTP like if using network sync?
    }

    public GregCalPlus(int year, int month, int dayOfMonth, int hourOfDay, int minute) {
        this(year, month, dayOfMonth, hourOfDay, minute, 0);
    }

    public GregCalPlus(int year, int month, int dayOfMonth, int hourOfDay, int minute, int second) {
        super(year, month, dayOfMonth, hourOfDay, minute, second);
        this.set(MILLISECOND,0);
    }

    @Override
    public int get(int field) {
        if (field != GregCalPlus.MONTH)
            return super.get(field);
        else return super.get(field)+1;
    }

    @Override
    public void set(int field, int value) {
        if (field != GregCalPlus.MONTH)
            super.set(field, value);
        else super.set(field, value-1);
    }
}
