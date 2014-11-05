package appname.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.logging.Level;
import java.util.logging.Logger;

import appname.util.Pair;
import appname.util.Quadruple;
import appname.util.Util;
/**
 * Created by yusiang on 11/4/14.
 */
public class ClockPanel extends JPanel {
    //double size=Math.min(this.getHeight(),this.getWidth())/2;
    double size=250f, prevSize=0;
    static final double secondHandLength =0.9;
    static final double minuteHandLength =0.75;
    static final double hourHandLength   =0.55;
    static final int   handsAlpha       =192;
	boolean nightMode = true;
	boolean drawDigital = true;
	boolean animate = true;
    Quadruple<Integer,Integer,Integer,Integer> divPos[] = new Quadruple[60];
    Pair<Integer,Integer> numPos[] = new Pair[12];
	private static float[] animateValues = {-1.0f, -0.95f, -0.85f, -0.7f, -0.5f, -0.3f, -0.1f, 0.05f, 0.10f, 0.05f, 0};
	int prevSecond = -1, animateDuration = animateValues.length - 1, animateTick = animateDuration;

	public ClockPanel(){
        /*
		setBackground(nightMode?
                new Color(32, 32, 32):
                new Color(223, 223, 223));
        */
        if(nightMode) setBackground(new Color(32, 32, 32));
        //calculateDivisions();
        //calculateNumbers();
	}

    @Override
    public void paintComponent(Graphics g){//Repaint every second.
        //If size changed, recalculate positions
        if(prevSize!=size){
            System.out.println("New size: "+size+"; Recalculating...");
            calculateDivisions();
            calculateNumbers();
            prevSize=size;
        }
        Graphics2D g2 = (Graphics2D) g;
	    g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);
	    g2.setColor(nightMode?Color.WHITE:Color.BLACK);
        //this.setSize(Util.safeLongToInt(Math.round(2*size)),Util.safeLongToInt(Math.round(2*size)));
        {//Setup the clock face markings
	        g2.setStroke(new BasicStroke(2));
	        float margin=0.01f;
            //Circle
	        g2.drawOval((int)(size*margin), (int)(size*margin), (int)(size*(2-margin)), (int)(size*(2-margin)));

            for(int i=0; i<60;i++){
                double theta = Util.map(i, 0, 60, 2 * Math.PI, 0);

                //Divisions
	            if(nightMode)
		            g2.setPaint(i % 5 == 0?Color.WHITE:Color.LIGHT_GRAY);
	            else
		            g2.setPaint(i % 5 == 0?Color.DARK_GRAY:Color.LIGHT_GRAY);
                g2.drawLine(divPos[i].first,divPos[i].second,divPos[i].third,divPos[i].fourth);


                //Numbers
                g2.setPaint(nightMode?Color.WHITE:Color.BLACK);
                if(i%5==0){
                    String s= ""+(i==0?12+"":(i/5<10?" "+i/5:i/5)) ;
                    g2.setFont(new Font(Font.SANS_SERIF,Font.PLAIN,Math.max(Math.min(Util.safeLongToInt(Math.round(size / 10)), 75/*MAX*/), 8/*MIN*/)  ));
                    g2.drawString(s,numPos[i/5].first ,numPos[i/5].second );

                }
            }
            {
                //Second hand
	            if (animate && Util.getSecond() != prevSecond) {
		            animateTick = -1;
		            prevSecond = Util.getSecond();
	            }
	            double second = animate?(Util.getSecond() + animateValues[animateTick=Math.min(animateTick + 1, animateDuration)]):Util.getSecond();
	            double secondAngle = Util.map(second, 0, 60, 2 * Math.PI, 0);
                double secondX = size+ Util.PolarToCartesianX(secondAngle, size*secondHandLength);
                double secondY = size+ Util.PolarToCartesianY(secondAngle, size * secondHandLength);
                g2.setStroke(new BasicStroke(3));
                g2.setPaint(nightMode?new Color(192,32,32,handsAlpha)
                        :new Color(255,0,0,handsAlpha));
                //System.out.println(secondAngle+","+Util.getSecond());
                g2.draw(new Line2D.Double(size, size, secondX, secondY));
            }
            {
                //Minute hand
                double minuteAngle = Util.map(60*Util.getMinute()+Util.getSecond(), 0, 60*60, 2 * Math.PI, 0);
                double minuteX = size+ Util.PolarToCartesianX(minuteAngle, size*minuteHandLength);
                double minuteY = size+ Util.PolarToCartesianY(minuteAngle, size*minuteHandLength);
                g2.setStroke(new BasicStroke(4));
                g2.setPaint(nightMode?new Color(57, 255, 57,handsAlpha)
                        :new Color(0,255,0,handsAlpha));
                //System.out.println(secondAngle+","+Util.getSecond());
                g2.draw(new Line2D.Double(size, size, minuteX, minuteY));
            }
            {
                //Hour hand
                double hourAngle = Util.map(60*60*Util.getHour()+60*Util.getMinute()+Util.getSecond(), 0, 12*60*60, 2 * Math.PI, 0);
                double hourX = size+ Util.PolarToCartesianX(hourAngle, size*hourHandLength);
                double hourY = size+ Util.PolarToCartesianY(hourAngle, size*hourHandLength);
                g2.setStroke(new BasicStroke(4));
                g2.setPaint(nightMode?new Color(64,64,192,handsAlpha)
                        :new Color(0,0,255,handsAlpha));
                //System.out.println(secondAngle+","+Util.getSecond());
                g2.draw(new Line2D.Double(size, size, hourX, hourY));
            }
            if(drawDigital){
                String s=Util.getTimeString();
                g2.setFont(new Font(Font.MONOSPACED,Font.PLAIN,Math.max(Math.min(Util.safeLongToInt(Math.round(size / 5)), 150/*MAX*/), 20/*MIN*/)  ));
                g2.setPaint(!nightMode?Color.BLACK:Color.WHITE);
                int stringLen = (int)
                        g2.getFontMetrics().getStringBounds(s, g2).getWidth();
                g2.drawString(s,Util.doubleToInt(size-stringLen/2f),Util.doubleToInt(size*0.9f));

            }
        }


    }

    public void setSize(double sz){
        size = sz;
        setPreferredSize(new Dimension(Util.doubleToInt(size*2),Util.doubleToInt(size*2)));
        //setMinimumSize(new Dimension(Util.doubleToInt(size*2),Util.doubleToInt(size*2)));
    }
    public void reCalc(){
        calculateNumbers();
        calculateDivisions();
    }
    private void calculateDivisions(){
        final float margin = 0.01f, minorDivision = 0.95f, majorDivison = 0.93f, divisionMargin = 0.98f;
        for(int i=0;i<60;i++){
            double theta = Util.map(i, 0, 60, 2 * Math.PI, 0);
            int startX=Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, i % 5 == 0 ? majorDivison * size : minorDivision * size) + size));
            int startY=Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, i % 5 == 0 ? majorDivison * size : minorDivision * size) + size));
            int endX  =Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, divisionMargin * size) + size));
            int endY  =Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, divisionMargin * size) + size));
            divPos[i]=new Quadruple<>(startX,startY,endX,endY);
        }
    }
    private void calculateNumbers(){
        final float textPosition = 0.82f;
        for(int i=0;i<12;i++){
            double theta = Util.map(i, 0, 12, 2 * Math.PI, 0);
            int posX = Util.safeLongToInt(Math.round(Util.PolarToCartesianX(theta, textPosition * size) + -size/17 + size));
            int posY = Util.safeLongToInt(Math.round(Util.PolarToCartesianY(theta, textPosition * size) +  size/30 + size));
            numPos[i]=new Pair<>(posX,posY);
        }
    }
}
