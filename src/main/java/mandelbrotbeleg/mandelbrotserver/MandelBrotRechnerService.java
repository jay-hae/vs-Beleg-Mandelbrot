package main.java.mandelbrotbeleg.mandelbrotserver;

import java.awt.*;
import java.awt.image.BufferedImage;


public class MandelBrotRechnerService {
    static int anzThreads = 10; //TODO variabel an übergebenes rechteck anpassen
    static MandelbrotWorkerThreads[] workerThreads;
    static BufferedImage buffImage;
    static Thread[] threads;

    public MandelBrotRechnerService(int width, int height){
        // wenn anzThreads dynamisch ..
        // aufruf von Threads:
        workerThreads = new MandelbrotWorkerThreads[anzThreads];

        for(int i=0;i<anzThreads;i++){
            workerThreads[i] = new MandelbrotWorkerThreads(anzThreads,i);
        }
    } 

    // TODO: Multithreading wenn genug Pixel übergeben werden
    // dynamische anpassung an server -> ermittlung Cors -> so viele Threads wenn sich der Overhead lohnt
    static BufferedImage calc(int width,int height,Double scale,Double originX,Double originY){

        System.out.println("enter calc with "+" "+width+" "+height+" "+scale+" "+originX+" "+originY);

        buffImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        // width und height müssen auf die Threads noch auf geteilt werden
        // height wird so gelassen und width wird durch anzah an threads geteielt
        int heightT = height;
        int widthT  = width / anzThreads;

        for(int i=0;i<anzThreads;i++){
            threads[i] = new Thread(workerThreads[i]);
            Double originXT= originX + widthT * scale; // da bildschirm in streifen geteilt und dann originX pro streifen weiter gestellt werden muss
            workerThreads[i].updateThread(widthT, heightT, scale, originXT, originY, buffImage);
            threads[i].start();
        }

        for(int i = 0; i < anzThreads; i++)
        {
            try
            {
                threads[i].join();
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }

        return buffImage;
    }

    /*
    // Jetzt in MandelbrotWorkerThreads
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
    */

    /*
    // TODO: stetige Farbwerte Funktion schreiben
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
    */
}
