package appname.util;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.*;

/**
 * Created by yusiang on 5/4/17.
 */
public class Settings {
    private Settings(){}//Static class, no constructor

    static Map<String,Boolean> booMap = new HashMap<>();
    static Map<String,Integer> intMap= new HashMap<>();
    static Map<String,Double> fltMap = new HashMap<>();
    static Map<String,String> strMap = new HashMap<>();

    public static boolean getBoolSetting(String key) {
        if(booMap.containsKey(key)) return booMap.get(key);
        new NullPointerException("No such key: "+key+" in booleanMap").printStackTrace();
        return false;
    }
    public static void setBoolSetting(String key,boolean val){
        booMap.put(key,val);
        saveToFile();
    }

    public static int getIntSetting(String key){
        if(intMap.containsKey(key)) return intMap.get(key);
        new NullPointerException("No such key: "+key+" in intMap").printStackTrace();
        return 0;
    }
    public static void setIntSetting(String key,int val){
        intMap.put(key,val);
        saveToFile();
    }

    public static double getFloatSetting(String key){
        if(fltMap.containsKey(key)) return fltMap.get(key);
        new NullPointerException("No such key: "+key+" in floatMap").printStackTrace();
        return 0;
    }
    public static void setFloatSetting(String key, double val){
        fltMap.put(key,val);
        saveToFile();
    }

    public static String getStringSetting(String key){
        if(strMap.containsKey(key)) return strMap.get(key);
        new NullPointerException("No such key: "+key+" in stringMap").printStackTrace();
        return null;
    }
    public static void setStringSetting(String key,String val){
        val.replaceAll("\t"," ");
        strMap.put(key,val);
        saveToFile();
    }

    public static void clearSettings(){
        booMap.clear();
        intMap.clear();
        fltMap.clear();
        strMap.clear();
    }

    public static void loadDefaultValues(){
        strMap.put("colourMode","Dark");//One of: Dark,Light,HiContrast,Custom
        intMap.put("custHHandColR",255);
        intMap.put("custHHandColG",255);
        intMap.put("custHHandColB",255);
        intMap.put("custMHandColR",255);
        intMap.put("custMHandColG",255);
        intMap.put("custMHandColB",255);
        intMap.put("custSHandColR",255);
        intMap.put("custSHandColG",255);
        intMap.put("custSHandColB",255);
        intMap.put("custHandsAlpha",255);
        strMap.put("custBgColScheme","HiContrast");//One of Dark,Light,HiContrast

        intMap.put("evTitleFontSz",40);
        intMap.put("evTimeFontSz",35);
        intMap.put("evStatusFontSz",20);

        //booMap.put("toiletUseLocal",false);

    }

    public static boolean loadFromFile(){
        try{
            Scanner sc = new Scanner(new File("settings.txt"));

            while(sc.hasNextLine()){
                String s[] = sc.nextLine().split("\t");
                if(s.length!=3||s[0].length()!=1) continue;
                switch(s[0].toLowerCase()){
                    case "b":
                        booMap.put(s[1],Boolean.parseBoolean(s[2]));
                        break;
                    case "i":
                        intMap.put(s[1],Integer.parseInt(s[2]));
                        break;
                    case "f":
                        fltMap.put(s[1],Double.parseDouble(s[2]));
                        break;
                    case "s":
                        strMap.put(s[1],s[2]);
                        break;
                }

            }
            System.out.println("Loaded settings from file.");

        }catch (FileNotFoundException ex){
            System.out.println("No settings file found.");
            return false;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean saveToFile(){
        try{
            PrintWriter pw = new PrintWriter("settings.txt");
            ArrayList<String> ls;
            ls = new ArrayList<String>(booMap.keySet());
            for(String s:ls){
                pw.println("b\t"+s+"\t"+(booMap.get(s)?"true":"false"));
            }
            ls = new ArrayList<String>(intMap.keySet());
            for(String s:ls){
                pw.println("i\t"+s+"\t"+intMap.get(s));
            }
            ls = new ArrayList<String>(fltMap.keySet());
            for(String s:ls){
                pw.println("f\t"+s+"\t"+ fltMap.get(s));
            }
            ls = new ArrayList<String>(strMap.keySet());
            for(String s:ls){
                pw.println("s\t"+s+"\t"+ strMap.get(s));
            }
            pw.flush();
            pw.close();
        }catch (Exception e){
            e.printStackTrace();
            return false; //Unable to save settings
        }
        return true; //OK
    }

    public static boolean printSettings(){
        try{
            PrintStream ps = System.out;
            ArrayList<String> ls;
            ls = new ArrayList<String>(booMap.keySet());
            Collections.sort(ls);
            for(String s:ls){
                ps.println("b\t"+s+"\t"+(booMap.get(s)?"true":"false"));
            }
            ls = new ArrayList<String>(intMap.keySet());
            Collections.sort(ls);
            for(String s:ls){
                ps.println("i\t"+s+"\t"+intMap.get(s));
            }
            ls = new ArrayList<String>(fltMap.keySet());
            Collections.sort(ls);
            for(String s:ls){
                ps.println("f\t"+s+"\t"+ fltMap.get(s));
            }
            ls = new ArrayList<String>(strMap.keySet());
            Collections.sort(ls);
            for(String s:ls){
                ps.println("s\t"+s+"\t"+ strMap.get(s));
            }
            ps.flush();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    static final int handsAlpha = 192;
    final static public Color[][] colorPalette =

            {
                    //BG,clock face,lgDiv,smDiv,numbers,circleOutline,
                    // hHand,mHand,sHand,digitalText
                    {},//0=custom
                    //Dark
                    {new Color(64, 64, 64), new Color(32, 32, 32), new Color(255,255,255), new Color(192, 192, 192), Color.WHITE, new Color(127, 127, 127),
                            new Color(64,64,255, handsAlpha), new Color(57,255,57, handsAlpha), new Color(255,0,0, handsAlpha), Color.WHITE
                    },
                    //Light
                    {new Color(192,192,192),new Color(223,223,223),new Color(63,63,63),   new Color(127,127,127),    Color.BLACK, new Color(32,32,32),
                            new Color(0,0,255,handsAlpha),new Color(0,223,0,handsAlpha),new Color(255,0,0, handsAlpha),Color.BLACK
                    },
                    //HiContrast
                    {new Color(0,0,0), new Color(0,0,0),           new Color(255,255,255), new Color(192, 192, 192), Color.WHITE, new Color(127, 127, 127),
                            new Color(255,255,0,255), new Color(0,255,255,255), new Color(255,0,255,255), Color.WHITE
                    }

            };

    public static Color getColor(int num){

        if(getStringSetting("colourMode").equals("Dark")) return colorPalette[1][num];
        if(getStringSetting("colourMode").equals("Light")) return colorPalette[2][num];
        if(getStringSetting("colourMode").equals("HiContrast")) return colorPalette[3][num];
        if(getStringSetting("colourMode").equals("Custom")){
            if(num==6) return new Color(getIntSetting("custHHandColR"), getIntSetting("custHHandColG"), getIntSetting("custHHandColB"), getIntSetting("custHandsAlpha"));
            if(num==7) return new Color(getIntSetting("custMHandColR"), getIntSetting("custMHandColG"), getIntSetting("custMHandColB"), getIntSetting("custHandsAlpha"));
            if(num==8) return new Color(getIntSetting("custSHandColR"), getIntSetting("custSHandColG"), getIntSetting("custSHandColB"), getIntSetting("custHandsAlpha"));
            if(getStringSetting("custBgColScheme").equals("Dark")) return colorPalette[1][num];
            if(getStringSetting("custBgColScheme").equals("Light")) return colorPalette[2][num];
            if(getStringSetting("custBgColScheme").equals("HiContrast")) return colorPalette[3][num];
        };
        return null;
    }
}
