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

    int serverAmount = 0;

    int width = 800, height = 600;

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

        // Proof of concept - Changing Model:

        System.out.println(new File("imageOne.jpg").getCanonicalPath());
        mainClient.bufferedImages.add(ImageIO.read(new File("imageOne.jpg")));
        mainClient.bufferedImages.add(ImageIO.read(new File("imageTwo.png")));

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

    /* Wird noch nicht getriggered*/
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom -= e.getUnitsToScroll();

        System.out.println(e.getWheelRotation());
        // bufferedImages = getNewImages(); // Change model
        displayImage(); // Present
    }

    public List<BufferedImage> getNewImages()
    {
        int segmentWidth = (width / serverAmount);

        List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>(serverAmount);

        /* Alio:
        * */
        for(int i = 0; i < serverAmount; i++)
        {
            int leftPos = segmentWidth * i;
            int rightPos = leftPos + segmentWidth;
            int topPos = 0;
            int bottomPos = height;

            // bufferedImages[i] = getImageSegment(zoom, leftPos, rightPos, topPos, bottomPos); // async
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
            return image;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new BufferedImage(13,13,BufferedImage.TYPE_CUSTOM); // müll
        }
    }

}