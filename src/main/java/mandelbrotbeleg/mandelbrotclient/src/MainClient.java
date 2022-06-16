package main.java.mandelbrotbeleg.mandelbrotclient.src;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JFrame;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;



// idefix port größer 1024 zum testen
public class MainClient extends JPanel {



    // 9.6 SEk mit 1 localhost und  8 threads pro calc
    // 13.3 SEk mit 1 localhost und  2 threads pro calc
    // 7.2 SEk mit 8 localhost und  2 threads pro calc
    // 10.2 SEk mit 8 localhost und  2 threads pro calc
    // 8.0 SEk mit 8 localhost und  8 threads pro calc
    static double counter = 100;
    static double startTime = 0;
    static MainClient mainClient;
    static final String[] servers = {
            "http://localhost:8080",
            "http://localhost:8080",
            "http://localhost:8080",
            "http://localhost:8080",
            "http://localhost:8080",
            "http://localhost:8080",
            "http://localhost:8080",
            "http://localhost:8080",
    };
    static Thread[] threads = new Thread[servers.length];
    static ServerRequestRunner[] workers = new ServerRequestRunner[servers.length];

    BufferedImage[] bufferedImages = new BufferedImage[servers.length];

    // Start Mapping bei Zoom=0
    int width = 800, height = 800;

    // BigDecimal als Koordinaten Dattentyp sinnvoll
    double topLeftPositionX = 5;
    double topLeftPositionY = 5;

    double originPositionX = -0.743643887037151;
    double originPositionY = 0.131825904205330;

    double zoomFactor=0.99;

    public static void main(String avg[]) throws IOException
    {

        startTime = System.currentTimeMillis();

        JFrame frame = new JFrame();
        mainClient = new MainClient(frame);

        frame.setResizable(false);
        frame.add(mainClient);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        mainClient.zoomAnimation();
    }

    public MainClient(JFrame frame) throws IOException
    {
        this.setPreferredSize(new Dimension(width, height));
    }

    public void zoomAnimation()
    {
        while(counter-- >0)
        {
            getAndDisplayImages();

            topLeftPositionX *= zoomFactor;
            topLeftPositionY *= zoomFactor;

            try
            {
                Thread.sleep(1);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        System.out.println((System.currentTimeMillis() - startTime)/1000+"s \n");
    }

    public void getAndDisplayImages()
    {
        getNewImages();
        displayImage();
    }

    public void displayImage()
    {
        mainClient.repaint();
    }

    // TODO: möglichkeit an mehrere Server zu senden
    // obwohl vlt. unnötig da nicht gefordert
    // alternativ könnte auch der eine Server die anderen Server verwalten, Schnittstelle bleibt gleich
    public void getNewImages()
    {
        double scale = ((2.0*(double)topLeftPositionX)/(double)width);
        double segmentWidthInCoordinateSystem = (width / servers.length) * scale;
        int segmentWidth = width/servers.length;
        
        for(int i = 0; i < servers.length; i++)
        {

            double leftTopPosX = -topLeftPositionX + segmentWidthInCoordinateSystem * i + (originPositionX);
            double leftTopPosY = topLeftPositionY+(originPositionY);
            System.out.println("counter: "+counter);
            workers[i] = new ServerRequestRunner(
                    segmentWidth,
                    height,
                    scale, // scale
                    leftTopPosX,
                    leftTopPosY,
                    servers[i]);

            threads[i] = new Thread(workers[i]);
            threads[i].start();

        }
        System.out.println("\n");

        for(int i = 0; i < servers.length; i++)
        {
            try{
                threads[i].join();
                bufferedImages[i] = workers[i].image;

            }catch(Exception e){
                System.out.println("Error join !");
            }
        }

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < bufferedImages.length; i++)
        {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(bufferedImages[i], (width / bufferedImages.length) * i, 0, this);
            g2d.dispose();
        }
    }
}
