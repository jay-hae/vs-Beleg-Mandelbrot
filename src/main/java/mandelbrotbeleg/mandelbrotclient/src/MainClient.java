package main.java.mandelbrotbeleg.mandelbrotclient.src;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JPanel;


public class MainClient extends JPanel implements MouseWheelListener{

    static MainClient mainClient;

    List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

    int serverAmount = 1;

    int width = 800, height = 800;
    int x = 5;
    int y = 5;

    int zoom = 0;

    public static void main(String avg[]) throws IOException
    {
        JFrame frame = new JFrame();
        mainClient = new MainClient(frame);

        frame.setResizable(false);
        frame.add(mainClient);
        frame.setVisible(true);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addMouseWheelListener(mainClient);
        mainClient.displayImage();
    }

    public MainClient(JFrame frame) throws IOException
    {
        this.setPreferredSize(new Dimension(width, height));
    }

    public void displayImage()
    {
        mainClient.repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {

        zoom -= e.getUnitsToScroll();
        System.out.println(zoom);
        bufferedImages = getNewImages();
        displayImage(); // Present
    }



    public List<BufferedImage> getNewImages()
    {
        int segmentWidth = (width / serverAmount);

        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

        for(int i = 0; i < serverAmount; i++)
        {
            int leftPos = segmentWidth * i;
            int rightPos = leftPos + segmentWidth;
            int topPos = 0;
            int bottomPos = height;

            // Alio Notes:
            // Pixel 1 linkes Segment:
            // Startsitatuation Viewport Zentrum in Koordinaten Ursprung
            // -> Pixel 1 Segment 1 durch Viewport Breite und Höhe ermitteln
            // globale Variable trackt die Nutzerbewegung so dass Pixel 1 stets bekannt
            // -> Pixel1 folgenden segment durch Pixel 1 erstes segment +segmentWidth*i

            // scale:
            // anfgangs:  X Achse Koordiantenbereich in Bild Durch width   Beispiel  10/600
            // und dann je nach Zoom Faktor

            BufferedImage newImage = getImageSegment(segmentWidth,height,(((double)2*(double)x)/(double)width),-x,y);
            bufferedImages.add(newImage);

        }

        for(int i = 0; i < serverAmount; i++)
        {
            //wait for response

            // bufferedImages.add(...)
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


    // Beispiel für Anfrage von Client
    // TODO: anständiges error handling
    BufferedImage getImageSegment(int width, int height, double scale, double originX,double originY){

        try{
            String url = "http://localhost:8080/calcolino";
            url+="/"+width;
            url+="/"+height;
            url+="/"+scale;
            url+="/"+originX;
            url+="/"+originY;

            URL server = new URL(url);
            URLConnection connection = server.openConnection();
            HttpURLConnection http = (HttpURLConnection)connection;
            http.setRequestMethod("GET");
            http.setDoOutput(true);
            http.connect();

            // Recieve Http Reqeuest
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String result="";
            String line;
            while((line = in.readLine()) != null){result+=line;}
            in.close();

            // Convert String back to image ...
            InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(result));
            BufferedImage image = ImageIO.read(inputStream);
            if(image==null){throw new Exception();}
            inputStream.close();
            return image;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new BufferedImage(13,13,BufferedImage.TYPE_CUSTOM); // müll
        }
    }

}