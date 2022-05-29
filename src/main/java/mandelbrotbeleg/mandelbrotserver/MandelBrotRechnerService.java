package main.java.mandelbrotbeleg.mandelbrotserver;


import java.awt.*;
import java.awt.image.BufferedImage;


public class MandelBrotRechnerService {

    static int maxIter = 5000;
    static double maxBetrag = 35;

    static final int[][] farben = {
            {1, 255, 255, 255}, // Hohe Iterationszahlen sollen hell,
            {30, 10, 255, 40}, //
            {300, 10, 10, 40}, // die etwas niedrigeren dunkel,
            {500, 205, 60, 40}, // die "Spiralen" rot
            {850, 120, 140, 255}, // und die "Arme" hellblau werden.
            {1000, 50, 30, 255}, // Innen kommt ein dunkleres Blau,
            {1100, 0, 255, 0}, // dann grelles Grün
            {1997, 20, 70, 20}, // und ein dunkleres Grün.
            {maxIter, 0, 0, 0}
    }; // Der Apfelmann wird schwarz.


    public MandelBrotRechnerService(){}

    static BufferedImage calc(int width,int height,Double scale,Double originX,Double originY){

        System.out.println("enter calc with "+" "+width+" "+height+" "+scale+" "+originX+" "+originY);
        BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        Double currentPixelX;
        Double currentPixelY;

        for(int i=0; i<width; i++){

            currentPixelX = originX + i * scale;

            for(int j=0;j<height;j++){

                currentPixelY = originY - j * scale;
                int iter = MandelBrotRechnerService.calcIter(currentPixelX,currentPixelY);
                image.setRGB(i, j, farbwert(iter));
            }
        }


        return image;
    }

    static public int calcIter(double cr, double ci) {

        int iter=0;
        double zr=0;
        double zi=0;
        double zr2 = 0;
        double zi2 = 0;
        double zri = 0;
        double betrag2 = 0;

        //  z_{n+1} = z²_n + c
        //  z²  = x² - y² + i(2xy)
        // |z|² = x² + y²
        for (iter = 0; iter < maxIter && betrag2 <= maxBetrag; iter++) {
            zr = zr2 - zi2 + cr;
            zi = zri + zri + ci;

            zr2 = zr * zr;
            zi2 = zi * zi;
            zri = zr * zi;
            betrag2 = zr2 + zi2;
        }

        return iter;
    }

    static Integer farbwert(int iter) {

        int[] F = new int[3];
        for (int i = 1; i < farben.length - 1; i++) {
            if (iter < farben[i][0]) {
                int iterationsInterval = farben[i - 1][0] - farben[i][0];
                double gewichtetesMittel = (iter - farben[i][0]) / (double) iterationsInterval;

                for (int f = 0; f < 3; f++) {
                    int farbInterval = farben[i - 1][f + 1] - farben[i][f + 1];
                    F[f] = (int) (gewichtetesMittel * farbInterval) + farben[i][f + 1];
                }
                return new Color(F[0], F[1], F[2]).getRGB();
            }
        }
        return Color.BLACK.getRGB();
    }


    // Getter setter
    public static int getMax_iter() {
        return maxIter;
    }

    public static void setMaxIter(int max_iter) {
        MandelBrotRechnerService.maxIter = max_iter;
    }

    public static double getMaxBetrag() {
        return maxBetrag;
    }

    public static void setMaxBetrag2(double max_betrag) {
        MandelBrotRechnerService.maxBetrag = max_betrag;
    }

}
