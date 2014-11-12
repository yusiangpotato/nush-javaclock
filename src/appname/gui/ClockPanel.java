package appname.gui;

import appname.util.Pair;
import appname.util.Quadruple;
import appname.util.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by yusiang on 11/4/14.
 */
public class ClockPanel extends JPanel {
	private static final Logger logger = Logger.getLogger(Thread.currentThread().getClass().getName());
    double size = 250f, prevSize = 0;
    static final double secondHandLength = 0.875;
    static final double minuteHandLength = 0.75;
    static final double hourHandLength = 0.55;
    static final int handsAlpha = 192;
    boolean nightMode = true;
    boolean drawDigital = true;
    boolean animate = true;
    Quadruple<Double, Double, Double, Double>[] divPos = new Quadruple[60];
    Pair<Integer, Integer> numPos[] = new Pair[12];
    private static double[] animateValues = {-1.0, -0.80, -0.5, -0.3, -0.1, 0.05, 0.10, 0.05, 0.0};
    int prevSecond = -1, animateDuration = animateValues.length - 1, animateTick = animateDuration;

    public ClockPanel() {
        //if (nightMode) setBackground(new Color(32, 32, 32));
        this.setDoubleBuffered(true);
    }

	/** Repaints at 30Hz **/
    @Override
    public void paintComponent(Graphics g) {
        //If size has changed, recalculate positions
        if (prevSize != size) {
            logger.log(Level.FINER, "New size: " + size + "; Recalculating...");
            calculateDivisions();
            calculateNumbers();
            prevSize = size;
        }
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        super.paintComponent(g2);


        {   //  Setup the clock face
	        g2.setStroke(new BasicStroke(2));
            //Circle fill
            g2.setPaint(nightMode ? new Color(32, 32, 32) : new Color(223, 223, 223));
            final double fillMargin = 0.015;
            g2.fillOval(Util.doubleToInt(size * fillMargin), Util.doubleToInt(size * fillMargin), Util.doubleToInt(size * (2 - 2 * fillMargin)), Util.doubleToInt(size * (2 - 2 * fillMargin)));


            for (int i = 0; i < 60; i++) {
                //Divisions
                if (nightMode)
                    g2.setPaint(i % 5 == 0 ? new Color(255, 255, 255) : new Color(192, 192, 192));
                else
                    g2.setPaint(i % 5 == 0 ? new Color(63, 63, 63) : new Color(127, 127, 127));
                g2.draw(new Line2D.Double(divPos[i].first, divPos[i].second, divPos[i].third, divPos[i].fourth));

                //Numbers
                g2.setPaint(nightMode ? Color.WHITE : Color.BLACK);
                if (i % 5 == 0) {
                    String s = "" + (i == 0 ? 12 + "" : (i / 5 < 10 ? " " + i / 5 : i / 5));
                    g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 10)), 75/*MAX*/), 8/*MIN*/)));
                    g2.drawString(s, numPos[i / 5].first, numPos[i / 5].second);

                }
            }
            //Circle outline
            final double lineMargin = 0.017;
            g2.setStroke(new BasicStroke(nightMode?5:3));
            g2.setPaint(nightMode ? new Color(127, 127, 127) : new Color(32, 32, 32));
            g2.drawOval(Util.doubleToInt(size * lineMargin), Util.doubleToInt(size * lineMargin), Util.doubleToInt(size * (2 - 2 * lineMargin)), Util.doubleToInt(size * (2 - 2 * lineMargin)));

            {   //  Second hand
                if (animate && Util.getSecond() != prevSecond) {
                    animateTick = -1;
                    prevSecond = Util.getSecond();
                }
                double second = animate ? (Util.getSecond() + animateValues[animateTick = Math.min(animateTick + 1, animateDuration)]) : Util.getSecond();
                double secondAngle = Util.map(second, 0, 60, 2 * Math.PI, 0);
                double secondX = size + Util.PolarToCartesianX(secondAngle, size * secondHandLength);
                double secondY = size + Util.PolarToCartesianY(secondAngle, size * secondHandLength);
                g2.setStroke(new BasicStroke(2));
                g2.setPaint(nightMode ? new Color(192, 32, 32, handsAlpha) : new Color(255, 0, 0, handsAlpha));
                g2.draw(new Line2D.Double(size, size, secondX, secondY));
            }
            {   //  Minute hand
                double minuteAngle = Util.map(60 * Util.getMinute() + Util.getSecond(), 0, 60 * 60, 2 * Math.PI, 0);
                double minuteX = size + Util.PolarToCartesianX(minuteAngle, size * minuteHandLength);
                double minuteY = size + Util.PolarToCartesianY(minuteAngle, size * minuteHandLength);
                g2.setStroke(new BasicStroke(3));
                g2.setPaint(nightMode ? new Color(57, 255, 57, handsAlpha) : new Color(0, 223, 0, handsAlpha));
                g2.draw(new Line2D.Double(size, size, minuteX, minuteY));
            }
            {   //  Hour hand
                double hourAngle = Util.map(60 * 60 * Util.getHour() + 60 * Util.getMinute() + Util.getSecond(), 0, 12 * 60 * 60, 2 * Math.PI, 0);
                double hourX = size + Util.PolarToCartesianX(hourAngle, size * hourHandLength);
                double hourY = size + Util.PolarToCartesianY(hourAngle, size * hourHandLength);
                g2.setStroke(new BasicStroke(6));
                g2.setPaint(nightMode ? new Color(64, 64, 192, handsAlpha) : new Color(0, 0, 255, handsAlpha));
                g2.draw(new Line2D.Double(size, size, hourX, hourY));
            }
            if (drawDigital) {
                String s = Util.getTimeString();
                g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 5)), 150/*MAX*/), 20/*MIN*/)));
                g2.setPaint(nightMode ? new Color(255,255,255) : new Color(0,0,0));
                int stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
                g2.drawString(s, Util.doubleToInt(size - stringLen / 2f), Util.doubleToInt(size * 0.95f));

            }
        }


    }

    public void setSize(double sz) {
        size = sz;
        setPreferredSize(new Dimension(Util.doubleToInt(size * 2), Util.doubleToInt(size * 2)));
    }

    public void reCalculate() {
        calculateNumbers();
        calculateDivisions();
    }

    public boolean toggleNightMode(){
        return nightMode = !nightMode;
    }
    public boolean toggleAnimation(){
        return animate=!animate;
    }
    public boolean toggleDigital(){
        return drawDigital=!drawDigital;
    }

    private void calculateDivisions() {
        final double minorDivision = 0.93, majorDivison = 0.90, divisionMargin = 0.98;
        for (int i = 0; i < 60; i++) {
            double theta = Util.map(i, 0, 60, 2 * Math.PI, 0);
            double startX = (Util.PolarToCartesianX(theta, i % 5 == 0 ? majorDivison * size : minorDivision * size) + size),
                    startY = (Util.PolarToCartesianY(theta, i % 5 == 0 ? majorDivison * size : minorDivision * size) + size),
                    endX = (Util.PolarToCartesianX(theta, divisionMargin * size) + size),
                    endY = (Util.PolarToCartesianY(theta, divisionMargin * size) + size);
            divPos[i] = new Quadruple<>(startX, startY, endX, endY);
        }
    }

    private void calculateNumbers() {
        final double textPosition = 0.82;
        for (int i = 0; i < 12; i++) {
            double theta = Util.map(i, 0, 12, 2 * Math.PI, 0);
            double posX = (Util.PolarToCartesianX(theta, textPosition * size) + -size / 17 + size);
            double posY = (Util.PolarToCartesianY(theta, textPosition * size) + size / 30 + size);
            numPos[i] = new Pair<>(Util.doubleToInt(posX), Util.doubleToInt(posY));
        }
    }
}
