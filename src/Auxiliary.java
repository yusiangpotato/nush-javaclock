import java.util.GregorianCalendar;

/**
 * Created by yusiang on 2/14/14.
 */
public class Auxiliary {//
    static double PolarToCartesianX(double theta,double length){ //Theta=0 means Straight up.
        return -1 * length *Math.sin(theta);
    }
    static double PolarToCartesianY(double theta,double length){//Theta=pi/2 means straight out right.
        return -1 * length *Math.cos(theta);
    }
    static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    static int getHour(){
        return new GregorianCalendar().get(GregorianCalendar.HOUR);
    }
    static int getMinute(){
        return new GregorianCalendar().get(GregorianCalendar.MINUTE);
    }
    static int getSecond(){
        return new GregorianCalendar().get(GregorianCalendar.SECOND);
    }
    static int getHour24(){
        return new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY);
    }

    static int getHour(GregorianCalendar g){
        return g.get(GregorianCalendar.HOUR);
    }
    static int getMinute(GregorianCalendar g){
        return g.get(GregorianCalendar.MINUTE);
    }
    static int getSecond(GregorianCalendar g){
        return g.get(GregorianCalendar.SECOND);
    }
    static int getHour24(GregorianCalendar g){
        return g.get(GregorianCalendar.HOUR_OF_DAY);
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
//    static int getHour(int i){
//
//        return new GregorianCalendar().get(GregorianCalendar.HOUR);
//    }
//    static int getMinute(int i){
//        return new GregorianCalendar().get(GregorianCalendar.MINUTE);
//    }
//    static int getSecond(int i){
//        return new GregorianCalendar().get(GregorianCalendar.SECOND);
//    }
//    static int getHour24(int i){
//        return new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY);
//    }
//
//    @Deprecated
//    static boolean processTimeCombobox(String s, ComboBox hr, ComboBox min){
//        //if(s.length()<5) return false;
//
//        SimpleDateFormat fmt = new SimpleDateFormat("H:m");
//        Date d =new Date();
//        try{
//            d= fmt.parse(s);
//        }catch (Exception ex){
//           return false;
//        }
//        hr.setValue(d.getHours());
//        min.setValue(d.getMinutes());
//        return false;
//    }
}
