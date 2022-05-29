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

    int serverAmount = 1; // width muss durch serveramount ganzzahlig teilbar sein !


    // Start Mapping bei Zoom=0
    int width = 800, height = 800;
    double x = 5;
    double y = 5;
    double zoomFactor=0.9;

    boolean _isLoading = false; // aktuell nur mit einem Server
    // bei n servern braucht es ein Array

    double zoom = 0;

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


        if(_isLoading){return;}

        zoom -= e.getUnitsToScroll();
        System.out.println(zoom);

        // noch position des scrollevents bestimmen und skalierten offset aufrechnen
        // sinnvollen zoom, x kann hier noch negativ werden

        if(zoom>0){
            x*=zoomFactor;
            y*=zoomFactor;
        }else{
            x/=zoomFactor;
            y/=zoomFactor;
        }
        System.out.println(x);
        System.out.println(y);
        bufferedImages = getNewImages();
        displayImage(); // Present
    }



    public List<BufferedImage> getNewImages()
    {

        _isLoading = true;

        int segmentWidth = (width / serverAmount);

        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

        for(int i = 0; i < serverAmount; i++)
        {
            // TODO: Tracking von x,y und movement von Origin
            BufferedImage newImage = getImageSegment(
                    segmentWidth,
                    height,
                    ((2.0*(double)x)/(double)width), // scale
                    -x+segmentWidth*i,
                    y);
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

        System.out.println(" enter get Imge Segment");
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

            _isLoading = false;
            return image;

        }catch (Exception e){
            System.out.println(e.getMessage());
            _isLoading = false;
            return new BufferedImage(13,13,BufferedImage.TYPE_CUSTOM); // müll
        }

    }

}