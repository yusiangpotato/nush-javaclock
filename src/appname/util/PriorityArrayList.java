package appname.util;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by yusiang on 11/5/14.
 */
public class PriorityArrayList<T extends Comparable> extends ArrayList<T> {

    @Override
    public boolean add(T t) {
        super.add(t);
        Collections.sort(this);
        return true;
    }

    @Override
    public void add(int index, T element) {
        this.add(element);
    }
}
