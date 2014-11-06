package appname.sched;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.PriorityQueue;

/**
 * Created by yusiang on 11/6/14.
 */
public class EventAddDialog {
    static void makeDialog(final PriorityQueue<Event> pq){
        final JFrame jf = new JFrame();
        final boolean[] modeEnd = {false};
        final boolean[] autoStart = {true};

        String[] hours = new String[24];
        String[] minutes = new String[60];

        for(int i=0;i<24;i++)
            hours[i] = ""+(i<10?"0"+i:i);
        for(int i=0;i<60;i++)
            minutes[i] = ""+(i<10?"0"+i:i);
        jf.setSize(400,200);
        JPanel pane = new JPanel(new MigLayout("fill, wrap","[15%][25%][15%][5%][15%][5%][15%][5%]",""));
        jf.setContentPane(pane);
        //Name
        JLabel nameLabel = new JLabel("Name:");
        pane.add(nameLabel, "grow 1");
        JTextField nameField = new JTextField();

        pane.add(nameField,"span 7, grow 1");
        //Start
        final JButton startButton = new JButton("Autostart at:");
        pane.add(startButton,"span 2, grow 1");
        final JComboBox<String> startHour = new JComboBox<>(hours);
        pane.add(startHour,"span 2, grow 1");
        final JComboBox<String> startMinutes = new JComboBox<>(minutes);
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
        pane.add(endHour,"span 2, grow 1");
        final JComboBox<String> endMinutes = new JComboBox<>(minutes);
        pane.add(endMinutes,"span 2, grow 1");
        final JButton endDateButton = new JButton("Date...");
        pane.add(endDateButton,"span 2, grow 1");
        //TODO
        //Duration ** Only one of either end or duration is visible, clicking the button swaps.
        //Default is duration.
        final JButton durationButton = new JButton("Duration:");
        pane.add(durationButton,"span 2, grow 1");
        final JTextField durationHours = new JTextField();
        pane.add(durationHours,"grow 1");
        final JLabel durationHoursLabel = new JLabel("h");
        pane.add(durationHoursLabel, "");
        final JTextField durationMinutes = new JTextField();
        pane.add(durationMinutes,"grow 1");
        final JLabel durationMinutesLabel = new JLabel("m");
        pane.add(durationMinutesLabel,"");
        final JTextField durationSeconds = new JTextField();
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
        endButton.getActionListeners()[0].actionPerformed(null);

        //JLabel ErrorMsg
        final JLabel errorLabel = new JLabel("");
        pane.add(errorLabel,"span 2, grow 1");

        //OK/Cancel
        JButton okButton = new JButton("OK");
        pane.add(okButton,"span 3, grow 1");

        final Event[] event = {null};

        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //TODO: parse/validate
                GregorianCalendar gS=null,gE=null;
                try{
                    if(autoStart[0]){

                    }
                    jf.dispose();
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



    }
}
