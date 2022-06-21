package main.java.mandelbrotbeleg.mandelbrotserver;

import java.awt.image.BufferedImage;


public class MandelBrotRechnerService {

    static int anzThreads = 13;

    MandelbrotWorkerThreads[] workerThreads;
    BufferedImage buffImage;
    Thread[] threads;

    public MandelBrotRechnerService(){
        // wenn anzThreads dynamisch ..
        // aufruf von Threads:
        threads = new Thread[anzThreads];
        workerThreads = new MandelbrotWorkerThreads[anzThreads];

        for(int i=0;i<anzThreads;i++){
            workerThreads[i] = new MandelbrotWorkerThreads(anzThreads,i);
        }
    } 

    // TODO: Multithreading wenn genug Pixel übergeben werden
    // dynamische anpassung an server -> ermittlung Cores -> so viele Threads wenn sich der Overhead lohnt
    BufferedImage calc(int width,int height,Double scale,Double originX,Double originY){

        buffImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);

        // width und height müssen auf die Threads noch auf geteilt werden
        // height wird so gelassen und width wird durch anzah an threads geteielt

        for(int i=0;i<anzThreads;i++){

            workerThreads[i].updateThread(width, height, scale, originX, originY, buffImage);

            threads[i] = new Thread(workerThreads[i]);
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
}
