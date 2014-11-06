package appname.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.GregorianCalendar;

/**
 * Created by yusiang on 2/14/14.
 */
public class Util {// Static class dear.
    private Util(){;}//This supposedly prevents instantiation? Whatever.

    public static double PolarToCartesianX(double theta,double length){ //Theta=0 means Straight up.
        return -1* length *Math.sin(theta);
    }
    public static double PolarToCartesianY(double theta,double length){//Theta=pi/2 means straight out right.
        return -1* length *Math.cos(theta);
    }
    public static double map(double x, double in_min, double in_max, double out_min, double out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
    //NOW
    public static int getYear(){
        return new GregorianCalendar().get(GregorianCalendar.YEAR);
    }
    public static int getMonth(){
        return new GregorianCalendar().get(GregorianCalendar.MONTH);
    }
    public static int getDate(){
        return new GregorianCalendar().get(GregorianCalendar.DATE);
    }
    public static int getHour(){
        return new GregorianCalendar().get(GregorianCalendar.HOUR);
    }
    public static int getMinute(){
        return new GregorianCalendar().get(GregorianCalendar.MINUTE);
    }
    public static int getSecond(){
        return new GregorianCalendar().get(GregorianCalendar.SECOND);
    }
    public static int getHour24(){
        return new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY);
    }

    //SPEC
    public static int getYear(GregorianCalendar g){
        return g.get(GregorianCalendar.YEAR);
    }
    public static int getMonth(GregorianCalendar g){
        return g.get(GregorianCalendar.MONTH);
    }
    public static int getDate(GregorianCalendar g){
        return g.get(GregorianCalendar.DATE);
    }
    public static int getHour(GregorianCalendar g){
        return g.get(GregorianCalendar.HOUR);
    }
    public static int getMinute(GregorianCalendar g){
        return g.get(GregorianCalendar.MINUTE);
    }
    public static int getSecond(GregorianCalendar g){
        return g.get(GregorianCalendar.SECOND);
    }
    public static int getHour24(GregorianCalendar g){
        return g.get(GregorianCalendar.HOUR_OF_DAY);
    }

    public static String getTimeString(){return getTimeString(new GregorianCalendar());}
    public static String getTimeString(GregorianCalendar g){
        return  (Util.getHour24(g)<10?"0":"")+Util.getHour24(g)+":"/*(getSecond()%2==0?":":" ")*/+
                (Util.getMinute(g)<10?"0":"")+Util.getMinute(g)+":"/*(getSecond()%2==0?":":" ")*/+
                (Util.getSecond(g)<10?"0":"")+Util.getSecond(g);
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException
                    (l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }
    public static int doubleToInt(double d){
        return safeLongToInt(Math.round(d));
    }
    public static String getDeltaT(GregorianCalendar g1, GregorianCalendar g2, boolean useFuzzyMode){
        return useFuzzyMode?fuzzyDeltaT(g1,g2):exactDeltaT(g1,g2);
    }
    private static String exactDeltaT(GregorianCalendar g1, GregorianCalendar g2){
        long deltaTSecs = (g2.getTimeInMillis()-g1.getTimeInMillis())/1000;
        return deltaTSecs/3600 + " h "+deltaTSecs%3600/60 + " min "+ deltaTSecs%60+ "sec";
    }
    private static String fuzzyDeltaT(GregorianCalendar g1, GregorianCalendar g2){
        long deltaTSecs = (g2.getTimeInMillis()-g1.getTimeInMillis())/1000;
        if(deltaTSecs>3600){ //1h+
            return toSf(deltaTSecs/3600.0, 2)+" h";
        } else if (deltaTSecs > 60){
            return toSf(deltaTSecs/60.0,2)+" min";
        } else {
            return deltaTSecs + " s";
        }
        //return "";
    }
    public static double toSf(double d, int sf){
        BigDecimal bd = new BigDecimal(d); //BigDecimal is NOT only a decimal library!
        bd = bd.round(new MathContext(sf));
        return bd.doubleValue();
    }

    public static int parseUInt(String s, String errorMsg) throws Exception{
        try{
            int x  = Integer.parseInt(s.trim());
            if(x<0) throw new Exception();
            return x;
        }catch(Exception e){
            throw new Exception(errorMsg);
        }
    }
    public static double parseDouble(String s, String errorMsg) throws Exception{
        try{
            return Double.parseDouble(s.trim());
        }catch(Exception e){
            throw new Exception(errorMsg);
        }
    }



//    public static int getHour(int i){
//
//        return new GregorianCalendar().get(GregorianCalendar.HOUR);
//    }
//    public static int getMinute(int i){
//        return new GregorianCalendar().get(GregorianCalendar.MINUTE);
//    }
//    public static int getSecond(int i){
//        return new GregorianCalendar().get(GregorianCalendar.SECOND);
//    }
//    public static int getHour24(int i){
//        return new GregorianCalendar().get(GregorianCalendar.HOUR_OF_DAY);
//    }
//
//    @Deprecated
//    public static boolean processTimeCombobox(String s, ComboBox hr, ComboBox min){
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
