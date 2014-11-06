package appname.sched;

import appname.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.GregorianCalendar;
import java.util.PriorityQueue;

/**
 * Created by yusiang on 11/6/14.
 */
public class EventDialog {
    static void makeDialog(final PriorityQueue<Event> pq, Event ev){
        final JFrame jf = new JFrame();
        final boolean[] modeEnd = {false};
        final boolean[] autoStart = {true};
        final boolean[] endIsDuration = {false};
        final int[] startYMDHMS = {Util.getYear(),Util.getMonth(),Util.getDate(),0,0,0};
        final int[]   endYMDHMS = {Util.getYear(),Util.getMonth(),Util.getDate(),0,0,0};
        if(ev!=null){
            if(ev.getStart()!=null){
                startYMDHMS[0]=Util.getYear  (ev.getStart());
                startYMDHMS[1]=Util.getMonth (ev.getStart());
                startYMDHMS[2]=Util.getDate  (ev.getStart());
                startYMDHMS[3]=Util.getHour24(ev.getStart());
                startYMDHMS[4]=Util.getMinute(ev.getStart());
                startYMDHMS[5]=Util.getSecond(ev.getStart());
            }
            if(ev.getEnd()!=null){
                endYMDHMS[0]=Util.getYear  (ev.getEnd());
                endYMDHMS[1]=Util.getMonth (ev.getEnd());
                endYMDHMS[2]=Util.getDate  (ev.getEnd());
                endYMDHMS[3]=Util.getHour24(ev.getEnd());
                endYMDHMS[4]=Util.getMinute(ev.getEnd());
                endYMDHMS[5]=Util.getSecond(ev.getEnd());
            }
        }
        String[] hours = new String[24];
        String[] minutes = new String[60];

        for(int i=0;i<24;i++)
            hours[i] = ""+(i<10?"0"+i:i);
        for(int i=0;i<60;i++)
            minutes[i] = ""+(i<10?"0"+i:i);
        jf.setSize(400,200);
        final JPanel pane = new JPanel(new MigLayout("fill, wrap","[15%][25%][15%][5%][15%][5%][15%][5%]",""));
        jf.setContentPane(pane);
        //Name
        JLabel nameLabel = new JLabel("Name:");
        pane.add(nameLabel, "grow 1");
        final JTextField nameField = new JTextField(ev==null?"":ev.name);

        pane.add(nameField,"span 7, grow 1");
        //Start
        final JButton startButton = new JButton("Autostart at:");
        pane.add(startButton,"span 2, grow 1");
        final JComboBox<String> startHour = new JComboBox<>(hours);
        startHour.setSelectedIndex(startYMDHMS[3]);
        pane.add(startHour,"span 2, grow 1");
        final JComboBox<String> startMinutes = new JComboBox<>(minutes);
        startMinutes.setSelectedIndex(startYMDHMS[4]);
        pane.add(startMinutes,"span 2, grow 1");
        final JButton startDateButton = new JButton("Date...");
        pane.add(startDateButton,"span 2, grow 1");
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(autoStart[0]){
                    autoStart[0]=false;
                    startButton.setText("Manual Start");
                    startHour.setEnabled(false);
                    startMinutes.setEnabled(false);
                    startDateButton.setEnabled(false);
                }else{
                    autoStart[0]=true;
                    startButton.setText("Autostart at:");
                    startHour.setEnabled(true);
                    startMinutes.setEnabled(true);
                    startDateButton.setEnabled(true);
                }
            }
        });
        //TODO Date

        //End
        final JButton endButton = new JButton("End:");
        pane.add(endButton,"span 2, grow 1");
        final JComboBox<String> endHour = new JComboBox<>(hours);
        endHour.setSelectedIndex(endYMDHMS[3]);
        pane.add(endHour,"span 2, grow 1");
        final JComboBox<String> endMinutes = new JComboBox<>(minutes);
        endMinutes.setSelectedIndex(endYMDHMS[4]);
        pane.add(endMinutes,"span 2, grow 1");
        final JButton endDateButton = new JButton("Date...");
        pane.add(endDateButton,"span 2, grow 1");
        //TODO Date

        //Duration ** Only one of either end or duration is visible, clicking the button swaps.
        //Default is duration.
        final JButton durationButton = new JButton("Duration:");
        pane.add(durationButton,"span 2, grow 1");
        final JTextField durationHours = new JTextField(endYMDHMS[3]==0?"":""+endYMDHMS[3]);
        pane.add(durationHours,"grow 1");
        final JLabel durationHoursLabel = new JLabel("h");
        pane.add(durationHoursLabel, "");
        final JTextField durationMinutes = new JTextField(endYMDHMS[4]==0?"":""+endYMDHMS[4]);
        pane.add(durationMinutes,"grow 1");
        final JLabel durationMinutesLabel = new JLabel("m");
        pane.add(durationMinutesLabel,"");
        final JTextField durationSeconds = new JTextField(endYMDHMS[5]==0?"":""+endYMDHMS[5]);
        pane.add(durationSeconds,"grow 1");
        final JLabel durationSecondsLabel = new JLabel("s");
        pane.add(durationSecondsLabel,"");



        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                durationButton.setEnabled(true);
                durationHours.setEnabled(true);
                durationMinutes.setEnabled(true);
                durationSeconds.setEnabled(true);
                durationHoursLabel.setEnabled(true);
                durationMinutesLabel.setEnabled(true);
                durationSecondsLabel.setEnabled(true);
                endButton.setEnabled(false);
                endDateButton.setEnabled(false);
                endHour.setEnabled(false);
                endMinutes.setEnabled(false);
                modeEnd[0] = false;
            }
        });
        durationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                durationButton.setEnabled(false);
                durationHours.setEnabled(false);
                durationMinutes.setEnabled(false);
                durationSeconds.setEnabled(false);
                durationHoursLabel.setEnabled(false);
                durationMinutesLabel.setEnabled(false);
                durationSecondsLabel.setEnabled(false);
                endButton.setEnabled(true);
                endDateButton.setEnabled(true);
                endHour.setEnabled(true);
                endMinutes.setEnabled(true);
                modeEnd[0] = true;
            }
        });
        if(ev!=null){
            if(ev.getStart()==null)
                startButton.getActionListeners()[0].actionPerformed(null); //Set to manual start
            if(ev.endIsDuration)
                endButton.getActionListeners()[0].actionPerformed(null); //Use duration
            else
                durationButton.getActionListeners()[0].actionPerformed(null); //Use absolute end

        }else{
            endButton.getActionListeners()[0].actionPerformed(null); //Use duration fields
        }
        //JLabel ErrorMsg
        final JLabel errorLabel = new JLabel("~");
        pane.add(errorLabel,"span 8, grow 1, wrap");

        //OK/Cancel
        JButton okButton = new JButton("OK");
        pane.add(okButton,"skip 2, span 3, grow 1");

        final Event[] event = {null};

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                GregorianCalendar gS = null,gE=null;
                try{
                    if(nameField.getText().trim().equals("")) nameField.setText("Unnamed");//TODO Think of something witty to put here
                    if(autoStart[0]){
                        startYMDHMS[3] = Util.parseUInt((String) startHour.getSelectedItem(), "Error parsing start hour!");//Hour
                        startYMDHMS[4] = Util.parseUInt((String) startMinutes.getSelectedItem(), "Error parsing start minute!");//Min
                        //startYMDHMS[5] = Util.parseUInt((String)startSec.getSelectedItem(),"Error parsing start second!");//Sec
                        gS = new GregorianCalendar(startYMDHMS[0],startYMDHMS[1],startYMDHMS[2],startYMDHMS[3],startYMDHMS[4],startYMDHMS[5]);
                    }
                    if(modeEnd[0]){//Set absolute end cal
                        endYMDHMS[3] = Util.parseUInt((String) endHour.getSelectedItem(), "Error parsing start hour!");//Hour
                        endYMDHMS[4] = Util.parseUInt((String) endMinutes.getSelectedItem(), "Error parsing start minute!");//Min
                        //startYMDHMS[5] = Util.parseUInt((String)startSec.getSelectedItem(),"Error parsing start second!");//Sec
                        gE = new GregorianCalendar(endYMDHMS[0],endYMDHMS[1],endYMDHMS[2],endYMDHMS[3],endYMDHMS[4],endYMDHMS[5]);

                    }else{//Using duration (relative end cal)
                        //
                        if(durationHours  .getText().trim().equals("")) durationHours  .setText("0");
                        if(durationMinutes.getText().trim().equals("")) durationMinutes.setText("0");
                        if(durationSeconds.getText().trim().equals("")) durationSeconds.setText("0");

                        if(autoStart[0]){//Add to end
                            gE = (GregorianCalendar) gS.clone();
                            gE.add(GregorianCalendar.SECOND     ,Util.parseUInt(durationSeconds.getText(), "Invalid duration seconds!"));
                            gE.add(GregorianCalendar.MINUTE     ,Util.parseUInt(durationMinutes.getText(), "Invalid duration minutes!"));
                            gE.add(GregorianCalendar.HOUR_OF_DAY,Util.parseUInt(durationHours.getText(), "Invalid duration hours!"));
                        }else{//End is duration.
                            endYMDHMS[0]=0;
                            endYMDHMS[1]=0;
                            endYMDHMS[2]=0;
                            endYMDHMS[3]=Util.parseUInt(durationHours.getText(), "Invalid duration hours!");
                            endYMDHMS[4]=Util.parseUInt(durationMinutes.getText(), "Invalid duration minutes!");
                            endYMDHMS[5]=Util.parseUInt(durationSeconds.getText(), "Invalid duration seconds!");
                            gE = new GregorianCalendar(endYMDHMS[0],endYMDHMS[1],endYMDHMS[2],endYMDHMS[3],endYMDHMS[4],endYMDHMS[5]);
                            endIsDuration[0]=true;
                        }
                    }
                    if(gS!=null&&gE.compareTo(gS)<=0) throw new Exception("End time equal/before start!");
                    jf.dispose();
                    event[0] = new Event(gS,gE,nameField.getText(),endIsDuration[0]);
                    pq.add(event[0]);
                }catch(Exception e){
                    errorLabel.setText(e.getMessage());
                }

            }
        });
        JButton cancelButton = new JButton("Cancel");
        pane.add(cancelButton,"skip 1, span 2, grow 1");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                jf.dispose();
            }
        });
        jf.setVisible(true);


    }
}
