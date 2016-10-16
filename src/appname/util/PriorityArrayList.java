package appname.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Logger;

/**
 * Created by yusiang on 11/5/14.
 */
public class PriorityArrayList<T extends Comparable> extends ArrayList<T> {

    @Override
    public boolean add(T t) {
        for(int i=0;i<size();i++){
            if(t.equals(get(i))) {
                Logger.getLogger("").warning("Tried to add events with same UUID as existing!");
                return false;
            }
        }
        super.add(t);
        Collections.sort(this);
        return true;
    }

    @Override
    public void add(int index, T element) {
        add(element);
    }

}
