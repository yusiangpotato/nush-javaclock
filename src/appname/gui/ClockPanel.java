package appname.gui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import appname.Util;
/**
 * Created by yusiang on 11/4/14.
 */
public class ClockPanel extends JPanel {
    //double size=Math.min(this.getHeight(),this.getWidth())/2;
    double size=250;
    @Override
    public void paintComponent(Graphics g){//Repaint every second.
        //super.paintComponent(g);
        //this.setSize(Util.safeLongToInt(Math.round(2*size)),Util.safeLongToInt(Math.round(2*size)));
        {//Setup the clock face markings
            for(int i=0; i<60;i++){
                double theta = Util.map(i, 0, 60, 2 * Math.PI, 0);
                int startX=Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, i % 5 == 0 ? 0.8 * size : 0.9 * size)+size));
                int startY=Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, i % 5 == 0 ? 0.8 * size : 0.9 * size)+size));
                int endX  =Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, size)+size));
                int endY  =Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, size)+size));
                g.drawLine(startX,startY,endX,endY);

                if(i%5==0){
                    int posX = Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, 0.75*size) + -8 + size));
                    int posY = Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, 0.75*size) +  4 + size));
                    String s= ""+(i==0?12+"":(i/5<10?" "+i/5:i/5)) ;
                    g.drawString(s,posX ,posY );
                    
                }
            }
        }
    }

    void setSize(double sz){
        size=sz;
    }
}
