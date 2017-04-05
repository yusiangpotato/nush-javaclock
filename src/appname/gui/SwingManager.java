package appname.gui;

import appname.sched.EventManager;
import appname.util.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.*;
//

/**
 * Created by yusiang on 11/4/14.
 */
public class SwingManager implements Runnable {
    Logger syslog = Logger.getLogger("");
    int every = 1;
    String execs = "";
    final int cnt[] = {1};
    BufferedReader stdin;

    ScheduledExecutorService ExecService;
    JPanel windowPane;
    ClockPanel clockPane;
    DigitalPanel digitalPane;
    JFrame window;
    EventManager eManager;

    public SwingManager() {
        stdin = new BufferedReader(new InputStreamReader(System.in));
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                try {
                    for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                        if ("Nimbus".equals(info.getName())) {
                            UIManager.setLookAndFeel(info.getClassName());
                            break;
                        }
                    }
                } catch (Exception e) {
                    // If Nimbus is not available, you can set the GUI to another look and feel.
                }
                //JFrame.setDefaultLookAndFeelDecorated(true);
                window = new JFrame("JavaClock ALPHA@2017-04-04");//

                clockPane = new ClockPanel();
                //windowPane = clockPane;
                windowPane = new JPanel(new MigLayout("", "5px[pref!,shrink]5px[push,fill]5px", "5px[pref!,shrink]5px[push,fill]5px"));


                window.setContentPane(windowPane);
                //windowPane.setBackground(Color.black);

                digitalPane = new DigitalPanel();
                windowPane.add(digitalPane,"push, grow 2");

                eManager = new EventManager(window);
                windowPane.add(eManager.getPane(), "push, grow 1, sy 2, wrap");

                windowPane.add(clockPane, "grow 1");
                window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);




                //windowPane.setVisible(true);
                window.setSize(1024, 768);
                window.setMinimumSize(new Dimension(600, 400));

                //windowPane.setSize(400, 400);
                window.setVisible(true);
                windowPane.setBackground(new Color(0, 0, 0));

            }
        });
        ExecService = Executors.newSingleThreadScheduledExecutor();
        setExecFreq(30);

    }

    @Override
    public void run() {
        cnt[0]++;

        final String[] s = {""};

        try {
            if (stdin.ready()) {
                s[0] = stdin.readLine();
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (!s[0].equals(""))
                    System.out.println(execCmd(s[0]) ? "ACK" : "NACK");

                windowPane.revalidate();
                windowPane.repaint();
                digitalPane.setSize(Math.min(window.getHeight() / 2, window.getWidth() / 2 ));
                digitalPane.revalidate();
                digitalPane.repaint();
                eManager.revalidate();
                clockPane.setSize(Math.min(window.getHeight() / 2  - digitalPane.getHeight()/1.87 -18, window.getWidth() / 2 -15));
                clockPane.repaint();
                //window.pack();
                //eManager.refresh(); Reval does refresh

            }
        });
    }

    public void setExecFreq(double freq) throws IllegalArgumentException {
        if (freq > 300 || freq < 0) throw new IllegalArgumentException("And what do you think you are doing, hmm?");

        ExecService.shutdown();
        ExecService = Executors.newSingleThreadScheduledExecutor();
        if (freq == 0) return;
        ExecService.scheduleWithFixedDelay(this, 0, Math.round(1E6 / freq), TimeUnit.MICROSECONDS);

    }

    public boolean execCmd(String cmd) {

        try {
            if (cmd.equals("")) return false;
            cmd = cmd.toUpperCase();
            String[] x = cmd.split(" ");

            for (int i = 0; i < x.length; i++) {
                if (!x[0].equals("EVERY") && x[i].equals("AND")) {
                    String nextExec = "";
                    for (int j = i + 1; j < x.length; j++) {
                        nextExec += x[j] + " ";
                    }
                    execCmd(nextExec);
                    break;
                }

            }
            for (int i = 0; i < x.length; i++) {
                if (!x[0].equals("EVERY") && x[i].equals("@"))
                    x[i] = "" + cnt[0];
            }
            switch (x[0]) {
                case "FREQ":
                    setExecFreq(Double.parseDouble(x[1]));
                    return true;

                case "STEP":
                    if (x.length == 1) {
                        run();
                        return true;
                    }
                case "FF":
                    for (int i = 0; i < Integer.parseInt(x[1]); i++)
                        run();
                    return true;
                case "RECALC":
                    clockPane.reCalculate();
                    return true;
                case "LS":
                    eManager.lsEvents();
                    return true;
                case "CLR":
                    eManager.clear();
                    return true;
                case "UP":
                    eManager.updatePane();
                    return true;
                case "ATE":
                    return eManager.addTestEvent(Integer.parseInt(x[1]));
                case "EDIT":
                    eManager.edit(Integer.parseInt(x[1]));
                    return true;
                /*
                case "NIGHT":
                    clockPane.toggleNightMode();
                    return true;
                */
                case "NIGHT":
                    if(Integer.parseInt(x[1])==0){
                        //clockPane.clrNightMode();
                        Settings.setStringSetting("colourMode","Day");
                    }else{
                        Settings.setStringSetting("colourMode","Night");
                    }
                    return true;
                case "ANIM":
                    clockPane.toggleAnimation();
                    return true;
                case "DIGITAL":
                    //clockPane.toggleDigital();
                    clockPane.setDigitalMode(Integer.parseInt(x[1]));
                    digitalPane.setDigitalMode(Integer.parseInt(x[1]));
                    return true;
                case "RSTATE":
                    eManager.setRemoteState(Integer.parseInt(x[1]));
                    return true;
                case "LOG":

                    switch ((x[1])){
                        case "0":
                        case "OFF":
                            syslog.setLevel(Level.OFF);
                            break;
                        case "1":
                        case "SEVERE":
                            syslog.setLevel(Level.SEVERE);
                            break;
                        case "2":
                        case "WARNING":
                            syslog.setLevel(Level.WARNING);
                            break;
                        case "3":
                        case "INFO":
                            syslog.setLevel(Level.INFO);
                            break;
                        case "4":
                        case "CONFIG":
                            syslog.setLevel(Level.CONFIG);
                            break;
                        case "5":
                        case "FINE":
                            syslog.setLevel(Level.FINE);
                            break;
                        case "6":
                        case "FINER":
                            syslog.setLevel(Level.FINER);
                            break;
                        case "7":
                        case "FINEST":
                            syslog.setLevel(Level.FINEST);
                            break;
                        case "8":
                        case "ALL":
                            syslog.setLevel(Level.ALL);
                            break;
                        default:
                            return false;
                    }
                    return true;
                case "SIZE":
                    window.setSize(Integer.parseInt(x[1]),Integer.parseInt(x[2]));
                    return true;
                case "SHOWSETTINGS":
                case "SETTINGS":
                    Settings.printSettings();
                    return true;
                case "SAVESETTINGS":
                    Settings.saveToFile();
                    return true;
                case "LOADSETTINGS":
                    Settings.loadFromFile();
                    return true;
                /*
                case "XSZ":
                    sl.setXsz(Integer.parseUInt(x[1]));
                    return true;
                case "YSZ":
                    sl.setYsz(Integer.parseUInt(x[1]));
                    return true;

                case "TRANS":
                case "ALPHA":
                    sl.setTransparent(Integer.parseUInt(x[1]) % 2 == 0);
                    return true;
                */

                case "EVERY": //Try code: Every n (something) and every 0 --> Gives a one-shot command n cycles later

                    if (Integer.parseInt(x[1]) == 0) {
                        execs = "";
                        return true;
                    } else if (every != Integer.parseInt(x[1])) {
                        execs = "";
                    }
                    every = Integer.parseInt(x[1]);
                    //execs="";
                    execs += "and ";
                    for (int i = 2; i < x.length; i++) {
                        execs += x[i] + " ";
                    }
                    return true;

                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
        //return true;.
    }
}
