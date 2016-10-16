package appname.remote;

import appname.gui.ClockPanel;
import appname.sched.EventManager;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
//

/**
 * Created by yusiang on 11/4/14.
 */
public class RemoteManager implements Runnable {
    ScheduledExecutorService ExecService;
    final JButton inOutButton;

    public RemoteManager(JButton inOutButton) {
        this.inOutButton=inOutButton;
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {


            }
        });
        ExecService = Executors.newSingleThreadScheduledExecutor();
        ExecService.scheduleWithFixedDelay(this, 0, 50, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {


            }
        });
    }

    public void shutdown() {
        ExecService.shutdown();
    }

}
