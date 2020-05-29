package javaclock.gui;

import javaclock.util.Settings;
import javaclock.util.Util;
import net.miginfocom.layout.CC;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yusiang on 8/11/16.
 */
public class DigitalPanel extends JPanel {
    JLabel l;
    double size=250;
    boolean drawDigital = true;
    public DigitalPanel(){
        super(new MigLayout("align center, fillx"));
        l = new JLabel(){
            @Override
            public void paintComponent(Graphics g){
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                super.paintComponent(g);
            }
        };
        add(l,new CC().alignX("center").spanX());
    }

    @Override
    public void paintComponent(Graphics g) {
        if(!drawDigital){
            l.setText("");
            return;
        }else {

            l.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 6)), 150/*MAX*/), 20/*MIN*/)));
            l.setText(Util.getTimeString());
            l.setForeground(Settings.getColor(9));
        }
        setBackground(Settings.getColor(0));
        super.paintComponent(g);
    }

    public void setSize(double sz){size=sz;}
    public void setDigitalMode(int i){
        drawDigital = (i&0b1) != 0;
    }
}
