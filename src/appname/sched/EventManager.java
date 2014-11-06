package appname.sched;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;

import net.miginfocom.swing.MigLayout;

/**
 * Created by yusiang on 11/4/14.
 */
public class EventManager{
    private final JPanel pane;
    private final PriorityQueue<Event> eList = new PriorityQueue<>();
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
                    dBtn = new JButton("OPTION"),
                    rBtn = new JButton("RESTART"),
                    cBtn = new JButton("CLEAR");

            JPanel p = new JPanel(new MigLayout("fill","[50%][50%]"));
            /*
            nBtn.setMinimumSize(new Dimension(100,25));
            dBtn.setMinimumSize(new Dimension(100,25));
            nBtn.setPreferredSize(new Dimension(100,25));
            dBtn.setPreferredSize(new Dimension(100,25));
            */
            nBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evx) {

                    EventDialog.makeDialog(eList,null); //Add


                }
            });
            p.add(nBtn,"grow 1");
            p.add(dBtn,"grow 1, wrap");
            p.add(rBtn,"grow 1");
            p.add(cBtn,"grow 1");

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
