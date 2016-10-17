package appname.sched;

import appname.util.GregCalPlus;
import appname.util.PriorityArrayList;
import appname.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by yusiang on 11/6/14.
 */
public class EventDialog {
    static void makeDialog(final JFrame parent, final PriorityArrayList<Event> prioList, final Event[] ev) {
        JDialog.setDefaultLookAndFeelDecorated(true);

        final JDialog jD = new JDialog(parent, "New Event");
        final boolean[] modeEnd = {false};
        final boolean[] useDuration = {false};
        final boolean[] autoStart = {true};
        final int[] startMode = {0};
        final int[] duration = {0};
        final int[] startYMDHMS = {Util.getYear(), Util.getMonth(), Util.getDate(), Util.getHour24(), Util.getMinute()+1, 0};
        final int[] endYMDHMS = {Util.getYear(), Util.getMonth(), Util.getDate(), 0, 0, 0};
        if (ev[0] != null) {
            if (ev[0].getStart() != null) {
                startYMDHMS[0] = Util.getYear(ev[0].getStart());
                startYMDHMS[1] = Util.getMonth(ev[0].getStart());
                startYMDHMS[2] = Util.getDate(ev[0].getStart());
                startYMDHMS[3] = Util.getHour24(ev[0].getStart());
                startYMDHMS[4] = Util.getMinute(ev[0].getStart());
                startYMDHMS[5] = Util.getSecond(ev[0].getStart());
            }
            /*
            if (ev[0].getEnd() != null) {
                endYMDHMS[0] = Util.getYear(ev[0].getEnd());
                endYMDHMS[1] = Util.getMonth(ev[0].getEnd());
                endYMDHMS[2] = Util.getDate(ev[0].getEnd());
                endYMDHMS[3] = Util.getHour24(ev[0].getEnd());
                endYMDHMS[4] = Util.getMinute(ev[0].getEnd());
                endYMDHMS[5] = Util.getSecond(ev[0].getEnd());
            }
            */
            {
                int[] durHMS = Util.secondsToHMS(ev[0].getDuration());
                endYMDHMS[3] = durHMS[0];
                endYMDHMS[4] = durHMS[1];
                endYMDHMS[5] = durHMS[2];
            }
        }
        String[] hours = new String[24];
        String[] minutes = new String[60];

        for (int i = 0; i < 24; i++)
            hours[i] = "" + (i < 10 ? "0" + i : i);
        for (int i = 0; i < 60; i++)
            minutes[i] = "" + (i < 10 ? "0" + i : i);
        final JPanel pane = new JPanel(new MigLayout("fill, wrap", "[15%][25%][15%][5%][15%][5%][15%][5%]", ""));
        jD.setContentPane(pane);
        //Name
        JLabel nameLabel = new JLabel("Name:");
        pane.add(nameLabel, "grow 1");
        final JTextField nameField = new JTextField(ev[0] == null ? "" : ev[0].name);

        pane.add(nameField, "span 7, grow 1");
        //Start
        final JButton startButton = new JButton("");
        pane.add(startButton, "span 2, grow 1");
        final JComboBox<String> startHour = new JComboBox<>(hours);
        startHour.setSelectedIndex(startYMDHMS[3]);
        pane.add(startHour, "span 2, grow 1");
        final JComboBox<String> startMinutes = new JComboBox<>(minutes);
        startMinutes.setSelectedIndex(startYMDHMS[4]);
        pane.add(startMinutes, "span 2, grow 1");
        final JButton startDateButton = new JButton("Date...");
        pane.add(startDateButton, "span 2, grow 1");

        startDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeDateDialog(parent, startYMDHMS, "Change start date");
            }
        });
        //End
        /*
        final JButton endButton = new JButton("End:");
        pane.add(endButton, "span 2, grow 1");
        final JComboBox<String> endHour = new JComboBox<>(hours);
        endHour.setSelectedIndex(endYMDHMS[3]);
        pane.add(endHour, "span 2, grow 1");
        final JComboBox<String> endMinutes = new JComboBox<>(minutes);
        endMinutes.setSelectedIndex(endYMDHMS[4]);
        pane.add(endMinutes, "span 2, grow 1");
        final JButton endDateButton = new JButton("Date...");
        pane.add(endDateButton, "span 2, grow 1");
        endDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                makeDateDialog(parent, endYMDHMS, "Change end date");
            }
        });
        */
        //Duration ** Only one of either end or duration is visible, clicking the button swaps.
        //Default is duration.
        final JButton durationButton = new JButton("Duration:");
        pane.add(durationButton, "span 2, grow 1");
        final JTextField durationHours = new JTextField(endYMDHMS[3] == 0 ? "" : "" + endYMDHMS[3]);
        pane.add(durationHours, "grow 1");
        final JLabel durationHoursLabel = new JLabel("h");
        pane.add(durationHoursLabel, "");
        final JTextField durationMinutes = new JTextField(endYMDHMS[4] == 0 ? "" : "" + endYMDHMS[4]);
        pane.add(durationMinutes, "grow 1");
        final JLabel durationMinutesLabel = new JLabel("m");
        pane.add(durationMinutesLabel, "");
        final JTextField durationSeconds = new JTextField(endYMDHMS[5] == 0 ? "" : "" + endYMDHMS[5]);
        pane.add(durationSeconds, "grow 1");
        final JLabel durationSecondsLabel = new JLabel("s");
        pane.add(durationSecondsLabel, "");


        durationButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //durationButton.setEnabled(true);
                durationHours.setEnabled(true);
                durationMinutes.setEnabled(true);
                durationSeconds.setEnabled(true);
                durationHoursLabel.setEnabled(true);
                durationMinutesLabel.setEnabled(true);
                durationSecondsLabel.setEnabled(true);
                //endButton.setEnabled(false);
                //ENDR//endDateButton.setEnabled(false);
                //ENDR//endHour.setEnabled(false);
                //ENDR//endMinutes.setEnabled(false);
                modeEnd[0] = false;
            }
        });
        /*//ENDR//
        endButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //durationButton.setEnabled(false);
                durationHours.setEnabled(false);
                durationMinutes.setEnabled(false);
                durationSeconds.setEnabled(false);
                durationHoursLabel.setEnabled(false);
                durationMinutesLabel.setEnabled(false);
                durationSecondsLabel.setEnabled(false);
                //endButton.setEnabled(true);
                //ENDR//endDateButton.setEnabled(true);
                //ENDR//endHour.setEnabled(true);
                //ENDR//endMinutes.setEnabled(true);
                modeEnd[0] = true;
            }
        });
        *///ENDR//

        durationButton.getActionListeners()[0].actionPerformed(null); //Use duration fields
        //JCheckbox Wait For ready before starting
        final JCheckBox waitCheckBox = new JCheckBox("Wait for ready");
        waitCheckBox.setSelected(true);
        waitCheckBox.setEnabled(false);
        pane.add(waitCheckBox,"span 8, grow 1, wrap");
        //StartBtn Actionlistener
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (startMode[0]==0){
                    startMode[0]=1;
                    startButton.setText("Autostart at:");
                    startHour.setEnabled(true);
                    startMinutes.setEnabled(true);
                    startDateButton.setEnabled(true);
                }else if (startMode[0]==1) {
                    startMode[0]=2;
                    startButton.setText("Manual Start");
                    startHour.setEnabled(false);
                    startMinutes.setEnabled(false);
                    startDateButton.setEnabled(false);
                }else if(startMode[0]==2){
                    startMode[0]=0;
                    startButton.setText("Start now...");
                    startHour.setEnabled(false);
                    startMinutes.setEnabled(false);
                    startDateButton.setEnabled(false);
                }
                if(startMode[0]==1) {
                    waitCheckBox.setEnabled(true);
                    waitCheckBox.setSelected(true);
                }
                else {
                    waitCheckBox.setEnabled(false);
                    waitCheckBox.setSelected(false);
                }
            }
        });

        startMode[0]=2;
        startButton.getActionListeners()[0].actionPerformed(null);

        if(ev[0]!=null){
            waitCheckBox.setSelected(ev[0].isWaitForReady());
            if (ev[0].getStart() == null){
                startMode[0]=1;
                startButton.getActionListeners()[0].actionPerformed(null); //Set to manual start
            }else{
                startMode[0]=0;
                startButton.getActionListeners()[0].actionPerformed(null); //Set to auto start
            }
        }



        //JLabel ErrorMsg
        final JLabel errorLabel = new JLabel("~");
        pane.add(errorLabel, "span 8, grow 1, wrap");

        //OK/Cancel
        JButton okButton = new JButton("OK");
        pane.add(okButton, "skip 2, span 3, grow 1");


        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                GregCalPlus gS = null, gE = null;
                try {
                    if (nameField.getText().trim().equals(""))
                        nameField.setText("Unnamed");//TODO Think of something witty to put here

                    if(startMode[0]==0){
                        gS=new GregCalPlus();
                    }
                    if (startMode[0]==1) {
                        startYMDHMS[3] = Util.parseUInt((String) startHour.getSelectedItem(), "Error parsing start hour!");//Hour
                        startYMDHMS[4] = Util.parseUInt((String) startMinutes.getSelectedItem(), "Error parsing start minute!");//Min
                        //startYMDHMS[5] = Util.parseUInt((String)startSec.getSelectedItem(),"Error parsing start second!");//Sec
                        gS = new GregCalPlus(startYMDHMS[0], startYMDHMS[1], startYMDHMS[2], startYMDHMS[3], startYMDHMS[4], startYMDHMS[5]);
                    }

                    if (modeEnd[0]) {//Set absolute end cal
                        //ENDR//endYMDHMS[3] = Util.parseUInt((String) endHour.getSelectedItem(), "Error parsing start hour!");//Hour
                        //ENDR//endYMDHMS[4] = Util.parseUInt((String) endMinutes.getSelectedItem(), "Error parsing start minute!");//Min
                        //startYMDHMS[5] = Util.parseUInt((String)startSec.getSelectedItem(),"Error parsing start second!");//Sec
                        //gE = new GregCalPlus(endYMDHMS[0], endYMDHMS[1], endYMDHMS[2], endYMDHMS[3], endYMDHMS[4], endYMDHMS[5]);
                        throw new Exception("Not allowed");

                    } else {//Using duration (relative end cal)
                        //
                        if (durationHours.getText().trim().equals("")) durationHours.setText("0");
                        if (durationMinutes.getText().trim().equals("")) durationMinutes.setText("0");
                        if (durationSeconds.getText().trim().equals("")) durationSeconds.setText("0");
                        useDuration[0] = true;
                        duration[0] = Util.parseUInt(durationSeconds.getText(), "Invalid duration seconds!") +
                                60 * Util.parseUInt(durationMinutes.getText(), "Invalid duration minutes!") +
                                3600 * Util.parseUInt(durationHours.getText(), "Invalid duration hours!");
                        if (duration[0] <= 0) throw new Exception("Duration too short (or negative) !");
                    }
                    if (gS != null && gE != null && gE.compareTo(gS) <= 0)
                        throw new Exception("End time equal/before start!");

                    if (true){//ev[0] == null) {
                        if (!useDuration[0])
                            throw new Exception("Not allowed");//ev[0] = new Event(gS, gE, nameField.getText());
                        else {
                            ev[0] = new Event(gS, duration[0], nameField.getText(), waitCheckBox.isSelected());
                        }
                        if(ev[0].getDuration()<0) throw new Exception("End is before start!");
                        else if(ev[0].hasEnded()) throw new Exception("Event has already ended!");

                    }
                    prioList.add(ev[0]);
	                parent.setEnabled(true);
                    jD.dispose();
                } catch (Exception e) {
                    e.printStackTrace();
                    if (e.getMessage().trim().equals(""))
                        errorLabel.setText("Critical Unknown Error! Please report bug.");
                    else
                        errorLabel.setText(e.getMessage());
                    //TODO Animate: Red, then fade off to black

                }

            }
        });
        JButton cancelButton = new JButton("Cancel");
        pane.add(cancelButton, "skip 1, span 2, grow 1");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
	            parent.setEnabled(true);
                jD.dispose();
            }
        });
        jD.setPreferredSize(new Dimension(400, 200));
	    jD.pack();
	    jD.setLocationRelativeTo(parent);
        jD.setVisible(true);


    }

    private static void makeDateDialog(final JFrame parent, final int[] YMDHMS, String title) {

        final JDialog jD = new JDialog(parent, title);
        JPanel pane = new JPanel(new MigLayout("fill, wrap", "[push][pref!][pref!][pref!]"));
        pane.add(new JLabel(title), "grow 1");
        final JTextField y = new JTextField("" + YMDHMS[0],5),
                m = new JTextField("" + YMDHMS[1],3),
                d = new JTextField("" + YMDHMS[2],3);
        pane.add(y, "grow 1");
        pane.add(m, "grow 1");
        pane.add(d, "grow 1");
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
			        YMDHMS[0] = Util.parseUInt(y.getText(), "Error parsing year!");
			        //if(YMDHMS[0]>2100) throw new Exception("Year too large!");
			        YMDHMS[1] = Util.parseUInt(m.getText(), "Error parsing month! (Use numbers?)");
			        YMDHMS[2] = Util.parseUInt(d.getText(), "Error parsing date!");
			        if (!Util.isDateValid(YMDHMS[2] + "-" + YMDHMS[1] + "-" + YMDHMS[0]))
				        throw new Exception("Date does not exist!");

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
