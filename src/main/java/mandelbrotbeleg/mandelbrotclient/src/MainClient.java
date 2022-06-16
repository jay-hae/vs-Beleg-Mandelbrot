package main.java.mandelbrotbeleg.mandelbrotclient.src;

import main.java.mandelbrotbeleg.mandelbrotserver.ServerRequestRunner;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.swing.JFrame;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;



// idefix port größer 1024 zum testen
public class MainClient extends JPanel {

    static MainClient mainClient;
    static final String[] servers = {
            "http://localhost:8080",
    };
    static Thread[] threads = new Thread[servers.length];


    List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

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
        while(true)
        {
            getAndDisplayImages();

            topLeftPositionX*=zoomFactor;
            topLeftPositionY*=zoomFactor;

            try
            {
                Thread.sleep(100);
            }
            catch(InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void getAndDisplayImages()
    {
        bufferedImages = getNewImages();
        displayImage();
    }

    public void displayImage()
    {
        mainClient.repaint();
    }

    // TODO: möglichkeit an mehrere Server zu senden
    // obwohl vlt. unnötig da nicht gefordert
    // alternativ könnte auch der eine Server die anderen Server verwalten, Schnittstelle bleibt gleich
    public List<BufferedImage> getNewImages()
    {

        int segmentWidth = (width / servers.length);

        BufferedImage images[] = new BufferedImage[servers.length]; // TODO: change if server change

        for(int i = 0; i < servers.length; i++)
        {

            ServerRequestRunner requestRunner = new ServerRequestRunner(
                    images[i],
                    segmentWidth,
                    height,
                    ((2.0*(double)topLeftPositionX)/(double)width), // scale
                    -topLeftPositionX+segmentWidth*i+(originPositionX),
                    topLeftPositionY+(originPositionY),
                    servers[i]);

            threads[i] = new Thread(requestRunner);
            threads[i].start();

        }

        for(int i = 0; i < servers.length; i++)
        {
            try{
                threads[i].join();
            }catch(Exception e){
                System.out.println("Error join !");
            }
        }

        return bufferedImages;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for(int i = 0; i < bufferedImages.size(); i++)
        {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(bufferedImages.get(i), (width / bufferedImages.size()) * i, 0, this);
            g2d.dispose();
        }
    }

/*
            ServerRequestRunner requestRunner = new ServerRequestRunner(
                    images[i],
                    segmentWidth,
                    height,
<<<<<<< HEAD
                    ((2.0*(double)x)/(double)width),
                    -x+segmentWidth*i,
                    y,
                    servers[i]);

            threads[i] = new Thread(requestRunner);
            threads[i].start();

=======
                    ((2.0*(double)topLeftPositionX)/(double)width), // scale
                    -topLeftPositionX+segmentWidth*i+(originPositionX),
                    topLeftPositionY+(originPositionY));
            bufferedImages.add(newImage);
>>>>>>> c5a86fe18acdf11479a8012d64b367a22813e66d
* */
}
