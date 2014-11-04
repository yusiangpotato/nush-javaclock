import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by yusiang on 11/4/14.
 */
public class ClockPanel extends JPanel {
    //double size=Math.min(this.getHeight(),this.getWidth())/2;
    double size=200;
    @Override
    public void paintComponent(Graphics g){//Repaint every second.
        //super.paintComponent(g);
        //this.setSize(Auxiliary.safeLongToInt(Math.round(2*size)),Auxiliary.safeLongToInt(Math.round(2*size)));
        {//Setup the clock face markings
            for(int i=0; i<60;i++){
                double theta = Auxiliary.map(i, 0, 60, 2 * Math.PI, 0);
                int startX=Auxiliary.safeLongToInt(Math.round(Auxiliary.PolarToCartesianX(theta, i % 5 == 0 ? 0.8 * size : 0.9 * size) + size));
                int startY=Auxiliary.safeLongToInt(Math.round(Auxiliary.PolarToCartesianY(theta, i % 5 == 0 ? 0.8 * size : 0.9 * size)+size));
                int endX  =Auxiliary.safeLongToInt(Math.round(Auxiliary.PolarToCartesianX(theta, size)+size));
                int endY  =Auxiliary.safeLongToInt(Math.round(Auxiliary.PolarToCartesianY(theta, size)+size));
                g.drawLine(startX,startY,endX,endY);

                if(i%5==0){
                    int posX = Auxiliary.safeLongToInt(Math.round(Auxiliary.PolarToCartesianX(theta, 0.75*size)+size));
                    int posY = Auxiliary.safeLongToInt(Math.round(Auxiliary.PolarToCartesianY(theta, 0.75*size)+size));
                    g.drawString((i==0?12+"":(i/5<10?" "+i/5:i/5))+"",posX ,posY );
                    
                }
            }
        }
    }
}
