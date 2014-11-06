package appname.sched;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.PriorityQueue;
import java.util.UUID;

import appname.util.priorityArrayList;
import net.miginfocom.swing.MigLayout;

/**
 * Created by yusiang on 11/4/14.
 */
public class EventManager{
    private JPanel pane;
    private PriorityQueue<Event> eList = new PriorityQueue<>();
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
            nBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent evx) {
                    final JFrame jf = new JFrame();
                    final boolean[] modeEnd = {false};
                    final boolean[] modeStart = {true};

                    String[] hours = new String[24];
                    String[] minutes = new String[60];

                    for(int i=0;i<24;i++)
                        hours[i] = ""+(i<10?" "+i:i);
                    for(int i=0;i<60;i++)
                        minutes[i] = ""+(i<10?" "+i:i);
                    jf.setSize(400,200);
                    JPanel pane = new JPanel(new MigLayout("fill, wrap","[30%][10%][15%][5%][15%][5%][15%][5%]",""));
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
                            if(modeStart[0]){
                                modeStart[0]=false;
                                startButton.setText("Manual Start");
                                startHour.setEnabled(false);
                                startMinutes.setEnabled(false);
                                startDateButton.setEnabled(false);
                            }else{
                                modeStart[0]=true;
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
                    //OK/Cancel
                    JButton okButton = new JButton("OK");
                    pane.add(okButton,"skip 2, span 3, grow 1");
	                okButton.addActionListener(new ActionListener() {
		                @Override
		                public void actionPerformed(ActionEvent actionEvent) {
			                //TODO: parse/validate
			                jf.dispose();
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
                    //TODO parse/validate
                    //Event e = new Event(new JFrame("New event"));
                    //TODO add to list
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
