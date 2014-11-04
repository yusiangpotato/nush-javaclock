package appname.gui;

import appname.Util;

import javax.swing.*;
import java.awt.*;

/**
 * Created by yusiang on 11/4/14.
 */
public class ClockPanel extends JPanel {

	public ClockPanel(){}

    //double size=Math.min(this.getHeight(),this.getWidth());
    double size=200;
    @Override
    public void paintComponent(Graphics g){//Repaint every second.
        //super.paintComponent(g);
        this.setSize(Util.safeLongToInt(Math.round(2 * size)), Util.safeLongToInt(Math.round(2 * size)));
        {//Setup the clock face markings
            for(int i=0; i<60;i++){
                double theta = Util.map(i, 0, 60, 2 * Math.PI, 0);
                int startX= Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, i % 5 == 0 ? 0.8 * size : 0.9 * size) + size));
                int startY= Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, i % 5 == 0 ? 0.8 * size : 0.9 * size) + size));
                int endX  = Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, size) + size));
                int endY  = Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, size) + size));
                g.drawLine(startX,startY,endX,endY);

                if(i%5==0){
                    int posX = Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, 0.72 * size) + size * 0.92));
                    int posY = Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, 0.72 * size) + size * 1.05));
                    g.drawString((i == 0 ? 12 + "" : (i / 5 < 10 ? " ":"" +i)),posX ,posY );
                    
                }
            }
        }
    }
}
