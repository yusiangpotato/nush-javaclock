package appname.sched;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;

import appname.util.priorityArrayList;
import net.miginfocom.swing.MigLayout;

/**
 * Created by yusiang on 11/4/14.
 */
public class EventManager{
    private JPanel pane;
    private priorityArrayList<Event> eList = new priorityArrayList<>();
    public EventManager(){
        pane = new JPanel(new MigLayout("fill","[100%]","[pref!][push]"));

        {

            //pane.add();
            pane.setMinimumSize(new Dimension(150, 1));
            pane.setPreferredSize(null);
            pane.setMaximumSize(null);
            pane.setBackground(new Color(62, 62, 62));
        }
        {//Buttons: New,Delete
            JButton nBtn = new JButton("NEW"),
                    dBtn = new JButton("OPT");

            JPanel p = new JPanel(new MigLayout("fill","[50%][50%]"));
            /*
            nBtn.setMinimumSize(new Dimension(100,25));
            dBtn.setMinimumSize(new Dimension(100,25));
            nBtn.setPreferredSize(new Dimension(100,25));
            dBtn.setPreferredSize(new Dimension(100,25));
            */
            nBtn.addActionListener(new Action() {
                @Override
                public Object getValue(String key) {
                    return null;
                }

                @Override
                public void putValue(String key, Object value) {

                }

                @Override
                public void setEnabled(boolean b) {

                }

                @Override
                public boolean isEnabled() {
                    return false;
                }

                @Override
                public void addPropertyChangeListener(PropertyChangeListener listener) {

                }

                @Override
                public void removePropertyChangeListener(PropertyChangeListener listener) {

                }

                @Override
                public void actionPerformed(ActionEvent e) {

                }
            });
            p.add(nBtn,"grow 1");

            p.add(dBtn,"grow 1");

            pane.add(p,"north, wrap");
        }

        //pane.add(new JPanel(),"push");

    }
    public void repaint(){

    }

    public JPanel getPane() {
        repaint();

        return pane;
    }
}
