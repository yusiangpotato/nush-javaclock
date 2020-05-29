package javaclock.gui;

import javaclock.util.Settings;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yusiang on 5/4/17.
 */
public class SettingsDialog {
    public static void makeSettingsDialog(final JFrame parent, String title) {

        final JDialog jD = new JDialog(parent,title);
        JPanel pane = new JPanel(new MigLayout("fill, wrap", "[pref!][pref!][pref!]"));
        //pane.add(new JLabel(title), "grow 1");
        pane.add(new JLabel("Event title font size:"));
        final JTextField eventTitleFontSz = new JTextField(""+Settings.getIntSetting("evTitleFontSz"),4);
        pane.add(eventTitleFontSz,"grow 1,span 2,wrap");

        pane.add(new JLabel("Event time font size:"));
        final JTextField eventTimeFontSz = new JTextField(""+Settings.getIntSetting("evTimeFontSz"),4);
        pane.add(eventTimeFontSz,"grow 1,span 2,wrap");

        pane.add(new JLabel("Event status font size:"));
        final JTextField eventStatusFontSz = new JTextField(""+Settings.getIntSetting("evStatusFontSz"),4);
        pane.add(eventStatusFontSz,"grow 1,span 2,wrap");

        pane.add(new JLabel("Color scheme:"));
        final JComboBox<String> colourScheme = new JComboBox<>(new String[]{"Dark","Light","HiContrast","Custom"});
        colourScheme.setSelectedItem(Settings.getStringSetting("colourMode"));
        pane.add(colourScheme,"grow 1,span 2,wrap");

        final Color handColors[] = {
                new Color(Settings.getIntSetting("custHHandColR"), Settings.getIntSetting("custHHandColG"),Settings.getIntSetting("custHHandColB")),
                new Color(Settings.getIntSetting("custMHandColR"), Settings.getIntSetting("custMHandColG"),Settings.getIntSetting("custMHandColB")),
                new Color(Settings.getIntSetting("custSHandColR"), Settings.getIntSetting("custSHandColG"),Settings.getIntSetting("custSHandColB")),
        };

        pane.add(new JLabel("Custom hour hand colour:"));
        final JPanel previewHHand = new JPanel(new BorderLayout());
        pane.add(previewHHand,"grow 1");
        JButton hHandColorPicker = new JButton("Edit...");
        hHandColorPicker.addActionListener(e1 -> {
            handColors[0]=JColorChooser.showDialog(jD,"Custom hour hand colour",handColors[0]);
            previewHHand.setBackground(handColors[0]);
        });
        pane.add(hHandColorPicker,"grow 1,wrap");

        pane.add(new JLabel("Custom minute hand colour:"));
        final JPanel previewMHand = new JPanel(new BorderLayout());
        pane.add(previewMHand,"grow 1");
        JButton mHandColorPicker = new JButton("Edit...");
        mHandColorPicker.addActionListener(e1 -> {
            handColors[1]=JColorChooser.showDialog(jD,"Custom minute hand colour",handColors[1]);
            previewMHand.setBackground(handColors[1]);
        });
        pane.add(mHandColorPicker,"grow 1,wrap");

        pane.add(new JLabel("Custom second hand colour:"));
        final JPanel previewSHand = new JPanel(new BorderLayout());
        pane.add(previewSHand,"grow 1");
        JButton sHandColorPicker = new JButton("Edit...");
        sHandColorPicker.addActionListener(e1 -> {
            handColors[2]=JColorChooser.showDialog(jD,"Custom second hand colour",handColors[2]);
            previewSHand.setBackground(handColors[2]);
        });
        pane.add(sHandColorPicker,"grow 1,wrap");

        pane.add(new JLabel("Custom hands transparency:"));
        JSlider alphaSlider = new JSlider(JSlider.HORIZONTAL,0,235,255-Settings.getIntSetting("custHandsAlpha"));
                //Invert the alpha value (right=max transparency=almost clear)
        pane.add(alphaSlider,"grow 1,span 2,wrap");

        pane.add(new JLabel("Custom background colours:"));
        final JComboBox<String> custBGScheme = new JComboBox<>(new String[]{"Dark","Light","HiContrast"});
        custBGScheme.setSelectedItem(Settings.getStringSetting("custBgColScheme"));
        pane.add(custBGScheme,"grow 1,span 2,wrap");


        colourScheme.addItemListener(e -> {
            if(colourScheme.getSelectedItem().toString().equals("Custom")){
                hHandColorPicker.setEnabled(true);
                mHandColorPicker.setEnabled(true);
                sHandColorPicker.setEnabled(true);
                custBGScheme.setEnabled(true);
                alphaSlider.setEnabled(true);
                previewHHand.setBackground(handColors[0]);
                previewMHand.setBackground(handColors[1]);
                previewSHand.setBackground(handColors[2]);
            }else{
                hHandColorPicker.setEnabled(false);
                mHandColorPicker.setEnabled(false);
                sHandColorPicker.setEnabled(false);
                custBGScheme.setEnabled(false);
                alphaSlider.setEnabled(false);
                previewHHand.setBackground(null);
                previewMHand.setBackground(null);
                previewSHand.setBackground(null);
            }
        });
        if(colourScheme.getSelectedItem().toString().equals("Custom")){
            hHandColorPicker.setEnabled(true);
            mHandColorPicker.setEnabled(true);
            sHandColorPicker.setEnabled(true);
            custBGScheme.setEnabled(true);
            alphaSlider.setEnabled(true);
            previewHHand.setBackground(handColors[0]);
            previewMHand.setBackground(handColors[1]);
            previewSHand.setBackground(handColors[2]);
        }else{
            hHandColorPicker.setEnabled(false);
            mHandColorPicker.setEnabled(false);
            sHandColorPicker.setEnabled(false);
            custBGScheme.setEnabled(false);
            alphaSlider.setEnabled(false);
            previewHHand.setBackground(null);
            previewMHand.setBackground(null);
            previewSHand.setBackground(null);
        }


        final JLabel dbg = new JLabel("~");
        pane.add(dbg, "span");
        JButton def= new JButton("Defaults");
        JButton ok = new JButton("Apply");
        JButton cc = new JButton("Close");
        pane.add(def,"split 2");
        pane.add(new JPanel());
        pane.add(ok, "grow 1");
        pane.add(cc, "grow 1");
        def.addActionListener(e -> {
            if(JOptionPane.YES_OPTION==JOptionPane.showInternalConfirmDialog(pane,"" +
                    "<html><p>Are you sure you want to <br>reset to default settings?</p></html>",
                    "Confirm reset to defaults",JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE)){
                Settings.clearSettings();
                Settings.loadDefaultValues();
                Settings.saveToFile();
                jD.dispose();
            }

        });
        ok.addActionListener(ev -> {
            try {
                int a = Integer.parseInt(eventTitleFontSz.getText());
                if(a<=10) throw new Exception("Title font size must be >10!");
                else if (a>=100) throw new Exception("Title font size must be <100!");
                int b = Integer.parseInt(eventTimeFontSz.getText());
                if(b<=10) throw new Exception("Time font size must be >10!");
                else if (b>=100) throw new Exception("Time font size must be <100!");
                int c = Integer.parseInt(eventStatusFontSz.getText());
                if(c<=5) throw new Exception("Status font size must be >5!");
                else if (c>=100) throw new Exception("Status font size must be <100!");
                if(colourScheme.getSelectedItem().toString().equals("Custom")) {
                    if (handColors[0] == null) throw new Exception("Must select a hour hand colour!");
                    if (handColors[1] == null) throw new Exception("Must select a minute hand colour!");
                    if (handColors[2] == null) throw new Exception("Must select a second hand colour!");
                }


                Settings.setIntSetting("evTitleFontSz",a);
                Settings.setIntSetting("evTimeFontSz",b);
                Settings.setIntSetting("evStatusFontSz",c);
                Settings.setStringSetting("colourMode",colourScheme.getSelectedItem().toString());
                Settings.setStringSetting("custBgColScheme",custBGScheme.getSelectedItem().toString());
                Settings.setIntSetting("custHandsAlpha",255-alphaSlider.getValue());//Invert the alpha value

                Settings.setIntSetting("custHHandColR",handColors[0].getRed());
                Settings.setIntSetting("custHHandColG",handColors[0].getGreen());
                Settings.setIntSetting("custHHandColB",handColors[0].getBlue());

                Settings.setIntSetting("custMHandColR",handColors[1].getRed());
                Settings.setIntSetting("custMHandColG",handColors[1].getGreen());
                Settings.setIntSetting("custMHandColB",handColors[1].getBlue());

                Settings.setIntSetting("custSHandColR",handColors[2].getRed());
                Settings.setIntSetting("custSHandColG",handColors[2].getGreen());
                Settings.setIntSetting("custSHandColB",handColors[2].getBlue());

                //jD.dispose();
                dbg.setText("~");
                dbg.setForeground(Color.BLACK);
            } catch (Exception ex) {
                dbg.setText(ex.getMessage());
                dbg.setForeground(Color.RED);
            }
        });
        cc.addActionListener(e -> jD.dispose());
        jD.setContentPane(pane);
        //jD.setSize(800, 600);
        jD.pack();
        jD.setLocationRelativeTo(parent);
        jD.setVisible(true);
    }

}
