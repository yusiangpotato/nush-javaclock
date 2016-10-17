package appname.util;

/**
 * Created by yusiang on 11/5/14.
 */
public class Quadruple<W, X, Y, Z> {
    public final W first;
    public final X second;
    public final Y third;
    public final Z fourth;

    public Quadruple(W sw, X sx, Y sy, Z sz) {
        first = sw;
        second = sx;
        third = sy;
        fourth = sz;
    }
}
