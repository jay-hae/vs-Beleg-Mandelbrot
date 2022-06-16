package main.java.mandelbrotbeleg.mandelbrotserver;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;

public class ServerRequestRunner implements Runnable {


    BufferedImage image;
    int height;
    int width;
    double scale;
    double originX;
    double originY;
    String serverName;

    public ServerRequestRunner(
            BufferedImage image,
            int height, int width,
            double scale,
            double originX,
            double originY,
            String serverName) {
        this.image = image;
        this.height = height;
        this.width = width;
        this.scale = scale;
        this.originX = originX;
        this.originY = originY;
        this.serverName = serverName;
    }

    @Override
    public void run() {

        image = getImageSegment(
                width,
                height,
                scale,
                originX,
                originY,
                serverName
        );

    }

    // Beispiel für Anfrage von Client
    // TODO: anständiges error handling
    BufferedImage getImageSegment(int width, int height, double scale, double topLeftPositionX,double topLeftPositionY,String serverName){

        try{
            String url = serverName+"/calcolino";
            url+="/"+width;
            url+="/"+height;
            url+="/"+scale;
            url+="/"+topLeftPositionX;
            url+="/"+topLeftPositionY;

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

            System.out.println("return from get Image Segment");
            return image;

        }catch (Exception e){
            System.out.println(e.getMessage());
            return new BufferedImage(13,13,BufferedImage.TYPE_CUSTOM); // müll
        }

    }
}
