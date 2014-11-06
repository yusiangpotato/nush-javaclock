package appname.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import appname.util.GregCalPlus;

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
        return new GregCalPlus().get(GregCalPlus.YEAR);
    }
    public static int getMonth(){
        return new GregCalPlus().get(GregCalPlus.MONTH);
    }
    public static int getDate(){
        return new GregCalPlus().get(GregCalPlus.DATE);
    }
    public static int getHour(){
        return new GregCalPlus().get(GregCalPlus.HOUR);
    }
    public static int getMinute(){
        return new GregCalPlus().get(GregCalPlus.MINUTE);
    }
    public static int getSecond(){
        return new GregCalPlus().get(GregCalPlus.SECOND);
    }
    public static int getHour24(){
        return new GregCalPlus().get(GregCalPlus.HOUR_OF_DAY);
    }

    //SPEC
    public static int getYear(GregCalPlus g){
        return g.get(GregCalPlus.YEAR);
    }
    public static int getMonth(GregCalPlus g){
        return g.get(GregCalPlus.MONTH);
    }
    public static int getDate(GregCalPlus g){
        return g.get(GregCalPlus.DATE);
    }
    public static int getHour(GregCalPlus g){
        return g.get(GregCalPlus.HOUR);
    }
    public static int getMinute(GregCalPlus g){
        return g.get(GregCalPlus.MINUTE);
    }
    public static int getSecond(GregCalPlus g){
        return g.get(GregCalPlus.SECOND);
    }
    public static int getHour24(GregCalPlus g){
        return g.get(GregCalPlus.HOUR_OF_DAY);
    }

    public static String getTimeString(){return getTimeString(new GregCalPlus());}
    public static String getTimeString(GregCalPlus g){
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
    public static long getDeltaT(GregCalPlus g1, GregCalPlus g2){
        return Math.round((g2.getTimeInMillis()-g1.getTimeInMillis())/1000f);
    }
    public static String getDeltaT(GregCalPlus g1, GregCalPlus g2, boolean useFuzzyMode){
        return useFuzzyMode?fuzzyDeltaT(g1,g2):exactDeltaT(g1,g2);
    }
    private static String exactDeltaT(GregCalPlus g1, GregCalPlus g2){
        long deltaTSecs = (g2.getTimeInMillis()-g1.getTimeInMillis())/1000;
        return deltaTSecs/3600 + " h "+deltaTSecs%3600/60 + " min "+ deltaTSecs%60+ "sec";
    }
    private static String fuzzyDeltaT(GregCalPlus g1, GregCalPlus g2){
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


    public static boolean isDateValid(String date)
    {
        try {
            final String DATE_FORMAT = "dd-MM-yyyy";
            DateFormat df = new SimpleDateFormat(DATE_FORMAT);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }



//    public static int getHour(int i){
//
//        return new GregCalPlus().get(GregCalPlus.HOUR);
//    }
//    public static int getMinute(int i){
//        return new GregCalPlus().get(GregCalPlus.MINUTE);
//    }
//    public static int getSecond(int i){
//        return new GregCalPlus().get(GregCalPlus.SECOND);
//    }
//    public static int getHour24(int i){
//        return new GregCalPlus().get(GregCalPlus.HOUR_OF_DAY);
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
