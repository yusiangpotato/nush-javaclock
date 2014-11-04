import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by yusiang on 11/4/14.
 */
public class SwingManager implements Runnable{
    int every = 1;
    String execs = "";
    final int cnt[] = {1};


    ScheduledExecutorService ExecService;
    JPanel windowpane;

    public SwingManager(){
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {

                JFrame.setDefaultLookAndFeelDecorated(true);
                JFrame window = new JFrame("Nevermind");
                windowpane = new ClockPanel();
                windowpane.setLayout(null);
                window.setContentPane(windowpane);
                //windowpane.setBackground(Color.black);
                //Pushingwindowpane.add(new ClockPanel());
                window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                windowpane.setVisible(true);
                window.setSize(640, 480);
                windowpane.setSize(640, 480);
                window.setVisible(true);

            }
        });
        ExecService = Executors.newSingleThreadScheduledExecutor();
        setExecFreq(50);

    }

    @Override
    public void run() {
        cnt[0]++;

        //TODO stub
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                windowpane.repaint();

            }
        });
    }

    public void setExecFreq(double freq) throws IllegalArgumentException {
        if (freq > 10000 || freq < 0) throw new IllegalArgumentException("And what do you think you are doing, hmm?");

        shutdown();
        ExecService = Executors.newSingleThreadScheduledExecutor();
        if (freq == 0) return;
        ExecService.scheduleWithFixedDelay(this, 0, Math.round(1E6 / freq), TimeUnit.MICROSECONDS);

    }

    public void shutdown() {
        ExecService.shutdown();
    }


    public boolean execCmd(String cmd) {
        try {
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
                /*
                case "XSZ":
                    sl.setXsz(Integer.parseInt(x[1]));
                    return true;
                case "YSZ":
                    sl.setYsz(Integer.parseInt(x[1]));
                    return true;

                case "TRANS":
                case "ALPHA":
                    sl.setTransparent(Integer.parseInt(x[1]) % 2 == 0);
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
