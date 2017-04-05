package appname.gui;

import appname.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yusiang on 5/4/17.
 */
public class SettingsDialog {
    private static void makeSettingsDialog(final JFrame parent, String title) {

        final JDialog jD = new JDialog(parent, title);
        JPanel pane = new JPanel(new MigLayout("fill, wrap", "[push][pref!][pref!][pref!]"));
        pane.add(new JLabel(title), "grow 1");
        //final JTextField y = new JTextField("" + YMDHMS[0],5),
                //m = new JTextField("" + YMDHMS[1],3),
                //d = new JTextField("" + YMDHMS[2],3);

        final JLabel dbg = new JLabel("~");
        pane.add(dbg, "span 4");
        JButton ok = new JButton("OK");
        JButton cc = new JButton("Cancel");
        pane.add(ok, "skip 1, grow 1");
        pane.add(cc, "span 2, grow 1");
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ev) {
                try {
                    //YMDHMS[0] = Util.parseUInt(y.getText(), "Error parsing year!");
                    //if(YMDHMS[0]>2100) throw new Exception("Year too large!");
                    //YMDHMS[1] = Util.parseUInt(m.getText(), "Error parsing month! (Use numbers?)");
                    //YMDHMS[2] = Util.parseUInt(d.getText(), "Error parsing date!");
                    //if (!Util.isDateValid(YMDHMS[2] + "-" + YMDHMS[1] + "-" + YMDHMS[0]))
                    //    throw new Exception("Date does not exist!");

                    jD.dispose();
                } catch (Exception ex) {
                    dbg.setText(ex.getMessage());
                }
            }
        });
        cc.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jD.dispose();
            }
        });
        jD.setContentPane(pane);
        jD.setSize(600, 100);
        jD.pack();
        jD.setLocationRelativeTo(parent);
        jD.setVisible(true);
    }

}
