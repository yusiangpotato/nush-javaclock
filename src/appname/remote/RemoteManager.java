package appname.remote;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
//

/**
 * Created by yusiang on 11/4/14.
 */
public class RemoteManager implements Runnable {
    ScheduledExecutorService ExecService;
    private final JButton toiletButton;
    private int state = 0b00000100; //0b0000DCBA D=UDPfail C=Uninit B=Local A=1out
    private int oldState = 0xFF;
    private String remoteID = "";

    int missedCounter=0;
    final int missedThresh = 100;//~5secs
    final int cycleTime = 50; //Millis

    final int UDPport=2302;
    DatagramSocket socket=null;

    public RemoteManager(JButton button) {
        this.toiletButton =button;
        try{
            socket = new DatagramSocket(UDPport);
            socket.setSoTimeout(cycleTime);
        }catch(Exception e){
            state |= 0b00001000;//UDP failure
            System.out.println("Unable to bind port 2302, no remote control function available.");
        }
        SwingUtilities.invokeLater(new Runnable() { //Do we really need this? Invokation is guaranteed from swing.
            public void run() {
                toiletButton.setBackground(new Color(154, 154, 154));
                toiletButton.setFont(new Font("Sans",Font.PLAIN,40));
                toiletButton.setHorizontalAlignment(SwingConstants.LEFT);
                toiletButton.setFocusPainted(false);
                toiletButton.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if(SwingUtilities.isLeftMouseButton(e)) {
                            //System.out.println("LMOUSE");
                            state &= 0b11111011; //Set to manual mode
                            state |= 0b00000010; //Set to manual mode
                            state ^= 0b00000001; //Toggle current display

                        }else if(SwingUtilities.isRightMouseButton(e)){
                            //System.out.println("RMOUSE");
                            state &= 0b11111101; //Auto mode
                            state |= 0b00000100; //uninitialized mode

                        }

                        updateButton();

                    }

                    @Override
                    public void mousePressed(MouseEvent e) {}
                    @Override
                    public void mouseReleased(MouseEvent e) {}
                    @Override
                    public void mouseEntered(MouseEvent e) {}
                    @Override
                    public void mouseExited(MouseEvent e) {}
                });

            }
        });
        ExecService = Executors.newSingleThreadScheduledExecutor();
        ExecService.scheduleWithFixedDelay(this, 0, cycleTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void run() {
        byte[] b = new byte[100];
        DatagramPacket p = new DatagramPacket(b,100);
        if((state & 0b00001000)!=0) return; //Port is not bound
        try{
            socket.receive(p);
            String str = new String(p.getData());
            //System.out.println(str);
            String[] strs = str.trim().split("-");
            if(strs.length<5) return; //Not ours
            if(!strs[0].equals("JavaClockRemote")) return;//Hmm
            if(!strs[1].equals("v001")) return;//Forward compatability? Meh.
            if((state&0b00000010)!=0) return; //Manual mode. No touchie!
            if(Integer.parseInt(strs[4])!=0) state |= 1;
            else state &= 0xFE;
            state&=0b11111011;//Initialized
            missedCounter=0;
        }catch (Exception e){
            //e.printStackTrace();
            missedCounter++;
            if(missedCounter>missedThresh){
                missedCounter=missedThresh;
                state |= 0b00000100;
            }
        }
        updateButton();
    }

    private void updateButton(){
        if(state==oldState) return;
        oldState=state;
        //System.out.println("St"+ state);
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if((state&0b00001000)!=0 && (state&0b00000010)==0){ //Fail & auto
                    toiletButton.setBackground(new Color(255, 47, 252));
                    toiletButton.setText("<html><p style=\"font-size: 16\">" +
                            "Failure binding port 2302<br>" +
                            "Remote control unavailable<br>" +
                            "Left click for local control</p></html>");
                }
                else if((state&0b00000100)!=0 && (state&0b00000010) == 0 ){ //Not init & remote ctl
                    toiletButton.setBackground(new Color(154, 154, 154));
                    toiletButton.setText("<html><p>Waiting</p><p style=\"font-size: 16\">Remote control</p></html>");
                }
                else {
                    String s = "<html><p>";
                    if ((state & 0b00000001) != 0) {
                        toiletButton.setBackground(new Color(255, 134, 137));
                        s+="ONE OUT";
                    } else {
                        toiletButton.setBackground(new Color(134, 255, 136));
                        s+="ALL IN";
                    }
                    s+="</p><p style=\"font-size: 16\">";
                    if((state&0b00000010) != 0 ){
                        s+="Local control";
                    }else{
                        s+="Remote control";
                    }
                    s+=remoteID+"</p></html>";
                    toiletButton.setText(s);
                }

            }
        });
    }

    @Deprecated
    public void setState(final int state){
        this.state = state;
        updateButton();
    }

    public void shutdown() {
        ExecService.shutdown();
    }

}
/*
Left click  sets to manual mode
Right click sets to remote mode
Default is remote mode with grey back until recv
 */
