package main.java.mandelbrotbeleg.mandelbrotclient.src;

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


    public BufferedImage image;
    final int height;
    final int width;
    final double scale;
    final double topLeftPositionX;
    final double topLeftPositionY;
    final String serverName;

    public ServerRequestRunner(
            int width,
            int height,
            double scale,
            double topLeftPositionX,
            double topLeftPositionY,
            String serverName) {
        this.height = height;
        this.width = width;
        this.scale = scale;
        this.topLeftPositionX = topLeftPositionX;
        this.topLeftPositionY = topLeftPositionY;
        this.serverName = serverName;
    }

    @Override
    public void run() {

        image = getImageSegment();

    }

    // Beispiel für Anfrage von Client
    BufferedImage getImageSegment(){


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

            return image;

        }catch (Exception e){
            e.printStackTrace();
            return new BufferedImage(13,13,BufferedImage.TYPE_CUSTOM); // müll
        }

    }
}
