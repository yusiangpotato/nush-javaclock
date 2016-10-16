package appname.sched;

import appname.util.GregCalPlus;
import appname.util.PriorityArrayList;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * Created by yusiang on 11/4/14.
 */
public class EventManager {
    private static EventManager eventManager = null;
    private final Logger syslog = Logger.getLogger("");
	private JFrame parent;
    private final JPanel pane;
    private final JPanel eventsPane;
    private final JPanel buttonPane;
    private final PriorityArrayList<Event> eList = new PriorityArrayList<>();
    PriorityArrayList<Event> eListOld = (PriorityArrayList<Event>) eList.clone();
    boolean nightMode = true;
    public EventManager(JFrame parent) {
	    this.parent = parent;
        pane = new JPanel(new MigLayout("fill", "[100%]", "[pref!][push]"));

        {
            pane.setMinimumSize(new Dimension(100, 1));
            pane.setPreferredSize(null);
            pane.setMaximumSize(null);
            pane.setBackground(new Color(62, 62, 62));
        }
        {   //  Buttons: New,Delete
            JButton nBtn = new JButton("NEW EVENT"),
                    dBtn = new JButton("OPTIONS"),
                    rBtn = new JButton("[RE]START ALL"),
                    cBtn = new JButton("CLEAR");

            buttonPane = new JPanel(new MigLayout("fill", "[50%][50%]"));

            /*
            nBtn.setMinimumSize(new Dimension(100,25));
            dBtn.setMinimumSize(new Dimension(100,25));
            nBtn.setPreferredSize(new Dimension(100,25));
            dBtn.setPreferredSize(new Dimension(100,25));
            */
            nBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evx) {
                    Event[] ev = {null};
                    EventDialog.makeDialog(EventManager.this.parent, eList, ev); //Add


                }
            });
            //  dBtn
            rBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    restartAll();
                }
            });
            cBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clear();
                }
            });
            buttonPane.add(nBtn, "grow 1");
            buttonPane.add(dBtn, "grow 1, wrap");
            buttonPane.add(rBtn, "grow 1");
            buttonPane.add(cBtn, "grow 1");

            pane.add(buttonPane, "north, wrap");
        }

        //pane.add(new JPanel(),"push");

        eventsPane = new JPanel(new MigLayout("fill, wrap 1"));
        pane.add(eventsPane, "grow 1");
        ImageIcon toiletIcon = new ImageIcon("images/toilet.png");
        toiletIcon = new ImageIcon(toiletIcon.getImage().getScaledInstance(50,50,Image.SCALE_SMOOTH));
        final JButton toiletButton = new JButton("ALL IN",toiletIcon);
        toiletButton.setBackground(new Color(134, 255, 136));
        toiletButton.setFont(new Font("Sans",Font.PLAIN,40));

        toiletButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(toiletButton.getText().equals("ALL IN")){
                    toiletButton.setBackground(new Color(255, 134, 137));
                    toiletButton.setText("ONE OUT");
                }else{
                    toiletButton.setBackground(new Color(134, 255, 136));
                    toiletButton.setText("ALL IN");
                }
            }
        });

        pane.add(toiletButton,"south,grow 1");
        eventManager = this;
    }

    public void refresh() {
        for (int i = 0; i < eList.size(); i++)
            eList.get(i).refresh();
        for (int i = 0; i < pane.getComponentCount(); i++) {
            pane.getComponent(i).revalidate();
            pane.getComponent(i).repaint();
        }
        buttonPane.setBackground(nightMode?new Color(64,64,64):new Color(192,192,192));
        eventsPane.setBackground(nightMode?new Color(64,64,64):new Color(192,192,192));
        pane      .setBackground(nightMode?new Color(64,64,64):new Color(192,192,192));
        pane.revalidate();
        pane.repaint();

    }

	/** Check components. Add/delete/reorder as necessary. **/
    public void revalidate() {
        //  First go through each item and if necc delete.
        for (int i = 0; i < eList.size(); ) {
            if (eList.get(i).canRemove()) eList.remove(i);
            else i++;
        }
        //  If we sort it and != before sort then need to update display
        Collections.sort(eList);
        if (!eList.equals(eListOld)) {//So just clear disp and re-add all.
            updatePane();
            eListOld = (PriorityArrayList<Event>) eList.clone();
        }
        refresh();
    }

    public void updatePane() {
        eventsPane.removeAll();
        for (Event e : eList) {
            e.refresh();
            eventsPane.add(e.toPanel(), "grow 1");
        }
    }

    public void lsEvents() {
        for (int i=0;i<eList.size();i++)
            System.out.println(i+": "+eList.get(i).toString());
    }


    public JPanel getPane() {
        refresh();

        return pane;
    }

    @Deprecated
    public boolean addTestEvent(int type) {
        switch (type) {
            case 1:
                eList.add(new Event(null, 5, "TEST1"));
                return true;
            case 2:
                eList.add(new Event(new GregCalPlus(), 5, "TEST2"));
                return true;
            case 3: {
                GregCalPlus g = new GregCalPlus();
                g.add(GregCalPlus.SECOND, 5);
                eList.add(new Event(g, 5, "TEST3"));
                return true;
            }
            /* //Removed after constructor deactivated
            case 4: {
                GregCalPlus g = new GregCalPlus();
                g.add(GregCalPlus.SECOND, 5);
                eList.add(new Event(new GregCalPlus(), g, "TEST4"));
                return true;
            }
            case 5: {
                GregCalPlus g1 = new GregCalPlus();
                g1.add(GregCalPlus.SECOND, 5);
                GregCalPlus g2 = new GregCalPlus();
                g2.add(GregCalPlus.SECOND, 10);
                eList.add(new Event(g1, g2, "TEST5"));
                return true;
            }*/
        }
        return false;
    }

    public void clear() {
        eList.clear();
    }

    public void restartAll() {
        for (Event e : eList)
            e.setStart(new GregCalPlus());
    }

    public void edit(int index){
        Event[] ev = {eList.get(index)};
        eList.remove(index);
        EventDialog.makeDialog(parent, eList,ev);
    }
    public void edit(UUID uuid){
        int index= -1;
        for(int i=0;i<eList.size();i++){
            if(eList.get(i).uuid == uuid)
                index=i;
        }
        if(index==-1) syslog.warning("Tried to edit non-existent event! - "+uuid);
        else edit(index);
    }
    public void remove(int index){
        eList.remove(index);
    }
    public void remove(UUID uuid){
        int index= -1;
        for(int i=0;i<eList.size();i++){
            if(eList.get(i).uuid == uuid)
                index=i;
        }
        if(index==-1) syslog.warning("Tried to remove non-existent event! - "+uuid);
        else remove(index);
    }
    public boolean setNightMode(){
        return nightMode = true;
    }
    public boolean clrNightMode(){
        return nightMode = false;
    }

    public static EventManager getEventManager() {
        return eventManager;
    }
}
