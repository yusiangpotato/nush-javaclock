package appname.gui;

import appname.util.Pair;
import appname.util.Quadruple;
import appname.util.Settings;
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

    static final boolean evalMode = false;
    //boolean nightMode = true;
    boolean drawDigital = false;
    boolean animate = true;
    Quadruple<Double, Double, Double, Double>[] divPos = new Quadruple[60];
    Pair<Integer, Integer> numPos[] = new Pair[12];
    private static double[] animateValues = {-0.8, -0.5, -0.3, -0.1, 0.02, 0.05, 0.02, 0.0};
    int prevSecond = -1, animateDuration = animateValues.length - 1, animateTick = animateDuration;

    public ClockPanel() {
        //if (nightMode) setBackground(new Color(32, 32, 32));
        this.setDoubleBuffered(true);
    }

    /**
     * Repaints at 30Hz
     **/
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
        setBackground(Settings.getColor(0));

        {   //  Setup the clock face
            g2.setStroke(new BasicStroke(2));
            //Circle fill
            g2.setPaint(Settings.getColor(1));
            final double fillMargin = 0.015;
            g2.fillOval(Util.doubleToInt(size * fillMargin), Util.doubleToInt(size * fillMargin), Util.doubleToInt(size * (2 - 2 * fillMargin)), Util.doubleToInt(size * (2 - 2 * fillMargin)));


            for (int i = 0; i < 60; i++) {
                //Divisions
                g2.setPaint(i % 5 == 0 ? Settings.getColor(2) : Settings.getColor(3));
                g2.draw(new Line2D.Double(divPos[i].first, divPos[i].second, divPos[i].third, divPos[i].fourth));

                //Numbers
                g2.setPaint(Settings.getColor(4));
                if (i % 5 == 0) {
                    String s = "" + (i == 0 ? 12 + "" : (i / 5 < 10 ? " " + i / 5 : i / 5));
                    g2.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 10)), 75/*MAX*/), 8/*MIN*/)));
                    g2.drawString(s, numPos[i / 5].first, numPos[i / 5].second);

                }

            }
            //Circle outline
            final double lineMargin = 0.017;
            g2.setStroke(new BasicStroke(5));
            g2.setPaint(Settings.getColor(5));
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
                g2.setPaint(Settings.getColor(6));
                g2.draw(new Line2D.Double(size, size, secondX, secondY));
            }
            {   //  Minute hand
                double minuteAngle = Util.map(60 * Util.getMinute() + Util.getSecond(), 0, 60 * 60, 2 * Math.PI, 0);
                double minuteX = size + Util.PolarToCartesianX(minuteAngle, size * minuteHandLength);
                double minuteY = size + Util.PolarToCartesianY(minuteAngle, size * minuteHandLength);
                g2.setStroke(new BasicStroke(4));
                g2.setPaint(Settings.getColor(7));
                g2.draw(new Line2D.Double(size, size, minuteX, minuteY));
            }
            {   //  Hour hand
                double hourAngle = Util.map(60 * 60 * Util.getHour() + 60 * Util.getMinute() + Util.getSecond(), 0, 12 * 60 * 60, 2 * Math.PI, 0);
                double hourX = size + Util.PolarToCartesianX(hourAngle, size * hourHandLength);
                double hourY = size + Util.PolarToCartesianY(hourAngle, size * hourHandLength);
                g2.setStroke(new BasicStroke(12));
                g2.setPaint(Settings.getColor(8));
                g2.draw(new Line2D.Double(size, size, hourX, hourY));
            }
            if (drawDigital) {
                String s = Util.getTimeString();
                g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 5)), 150/*MAX*/), 20/*MIN*/)));
                g2.setPaint(Settings.getColor(9));
                int stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
                g2.drawString(s, Util.doubleToInt(size - stringLen / 2f), Util.doubleToInt(size * 0.95f));

            }
            if (evalMode) {
                {
                    String s = "FOR INTERNAL EVALUATION ONLY";
                    g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 15)), 150/*MAX*/), 10/*MIN*/)));
                    g2.setPaint(Settings.getColor(9));
                    int stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
                    g2.drawString(s, Util.doubleToInt(size - stringLen / 2f), Util.doubleToInt(size * 0.95f + 25));
                }
                {
                    String s = "DO NOT USE FOR EXAMS";
                    g2.setFont(new Font(Font.MONOSPACED, Font.PLAIN, Math.max(Math.min(Util.safeLongToInt(Math.round(size / 15)), 150/*MAX*/), 10/*MIN*/)));
                    g2.setPaint(Settings.getColor(9));
                    int stringLen = (int) g2.getFontMetrics().getStringBounds(s, g2).getWidth();
                    g2.drawString(s, Util.doubleToInt(size - stringLen / 2f), Util.doubleToInt(size * 0.95f + 50));
                }

            }
        }


    }

    public void setSize(double sz) {
        size = sz;
        setPreferredSize(new Dimension(Util.doubleToInt(size * 2), Util.doubleToInt(size * 2)));
        logger.finest("Changing size to " + sz);
    }

    public void reCalculate() {
        calculateNumbers();
        calculateDivisions();
    }


    public boolean toggleAnimation() {
        return animate = !animate;
    }

    public void setDigitalMode(int i) {
        drawDigital = (i & 0b10) != 0;
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
