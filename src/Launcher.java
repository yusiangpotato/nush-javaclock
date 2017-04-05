import appname.gui.SwingManager;
import appname.util.Settings;

import javax.swing.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher {
	private static final Logger logger = Logger.getLogger(Thread.currentThread().getClass().getName());

    public static void main(String[] args) {
        /*
	    try {
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	    } catch (ClassNotFoundException |
			    InstantiationException |
			    IllegalAccessException |
			    UnsupportedLookAndFeelException e) {
		    logger.log(Level.WARNING, "Unable to load System L&F: ", e);
	    }
	    */

        Settings.loadDefaultValues();
        Settings.loadFromFile(); //These will override the defaults if set.

        new SwingManager();

	    logger.log(Level.INFO, "Launch complete");

    }
}
