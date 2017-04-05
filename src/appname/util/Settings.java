package appname.util;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by yusiang on 5/4/17.
 */
public class Settings {
    private Settings(){}//Static class, no constructor

    static Map<String,Boolean> booleanMap= new HashMap<>();
    static Map<String,Integer> intMap= new HashMap<>();
    static Map<String,Double> floatMap = new HashMap<>();
    static Map<String,String> stringMap= new HashMap<>();

    public static boolean getBoolSetting(String key) throws Exception{
        if(booleanMap.containsKey(key)) return booleanMap.get(key);
        else throw new Exception("Bool:KeyNotFound:"+key);
    }
    public static void setBoolSetting(String key,boolean val){
        booleanMap.put(key,val);
        saveToFile();
    }

    public static int getIntSetting(String key) throws Exception{
        if(intMap.containsKey(key)) return intMap.get(key);
        else throw new Exception("Int:KeyNotFound:"+key);
    }
    public static void setIntSetting(String key,int val){
        intMap.put(key,val);
        saveToFile();
    }

    public static double getFloatSetting(String key) throws Exception{
        if(floatMap.containsKey(key)) return floatMap.get(key);
        else throw new Exception("Float:KeyNotFound:"+key);
    }
    public static void setFloatSetting(String key, double val){
        floatMap.put(key,val);
        saveToFile();
    }

    public static String getStringSetting(String key)throws Exception{
        if(stringMap.containsKey(key)) return stringMap.get(key);
        else throw new Exception("String:KeyNotFound:"+key);
    }
    public static void setStringSetting(String key,String val){
        val.replaceAll("\t"," ");
        stringMap.put(key,val);
        saveToFile();
    }

    public static void clearSettings(){
        booleanMap.clear();
        intMap.clear();
        floatMap.clear();
        stringMap.clear();
    }

    public static boolean openFromFile(){
        try{
            Scanner sc = new Scanner("settings.txt");
            while(sc.hasNextLine()){
                String s[] = sc.nextLine().split("\t");
                if(s.length!=3||s[0].length()!=1) continue;
                switch(s[0].toLowerCase()){
                    case "b":
                        booleanMap.put(s[1],Boolean.parseBoolean(s[2]));
                        break;
                    case "i":
                        intMap.put(s[1],Integer.parseInt(s[2]));
                        break;
                    case "f":
                        floatMap.put(s[1],Double.parseDouble(s[2]));
                        break;
                    case "s":
                        stringMap.put(s[1],s[2]);
                        break;
                }

            }

        }catch (Exception ex){
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    public static boolean saveToFile(){
        try{
            PrintWriter pw = new PrintWriter("settings.txt");
            ArrayList<String> ls;
            ls = new ArrayList<String>(booleanMap.keySet());
            for(String s:ls){
                pw.println("b\t"+s+"\t"+(booleanMap.get(s)?'1':'0'));
            }
            ls = new ArrayList<String>(intMap.keySet());
            for(String s:ls){
                pw.println("i\t"+s+"\t"+intMap.get(s));
            }
            ls = new ArrayList<String>(floatMap.keySet());
            for(String s:ls){
                pw.println("f\t"+s+"\t"+ floatMap.get(s));
            }
            ls = new ArrayList<String>(stringMap.keySet());
            for(String s:ls){
                pw.println("s\t"+s+"\t"+stringMap.get(s));
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
            ls = new ArrayList<String>(booleanMap.keySet());
            for(String s:ls){
                ps.println("b\t"+s+"\t"+(booleanMap.get(s)?'1':'0'));
            }
            ls = new ArrayList<String>(intMap.keySet());
            for(String s:ls){
                ps.println("i\t"+s+"\t"+intMap.get(s));
            }
            ls = new ArrayList<String>(floatMap.keySet());
            for(String s:ls){
                ps.println("f\t"+s+"\t"+ floatMap.get(s));
            }
            ls = new ArrayList<String>(stringMap.keySet());
            for(String s:ls){
                ps.println("s\t"+s+"\t"+stringMap.get(s));
            }
            ps.flush();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
