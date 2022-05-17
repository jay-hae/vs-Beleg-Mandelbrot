package main.java.mandelbrotbeleg.mandelbrotserver;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Base64;

@RestController
public class MandelbrotServerRestRessource {

    /*
    * width: segment Width
    * height: segment Height
    * scale: pixel abstand in Zahlenebene
    * originX: X Koordinate Ursprung Segment / Pixel1 in Zahlenebene
    * originY: Y koordinate Ursprung in Segment / Pixel1 in Zahlenebene
    * Returns: Als String codiertes BufferedImage, TeilSegment des Mandelbrot sets
    * */
    @GetMapping("/calcolino/{width}/{height}/{scale}/{originX}/{originY}")
    HttpEntity<String> getImageSegment(
            @PathVariable int width,
            @PathVariable int height,
            @PathVariable Double scale,
            @PathVariable Double originX,
            @PathVariable Double originY) {

        System.out.println("Server got Request with:\n" +
                "width: "+width+"\n"+
                "height: "+height+"\n"+
                "scale: "+scale+"\n"+
                "originX: "+originX+"\n"+
                "originY: "+originY+"\n");
       try{

           BufferedImage image = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
           // hier können dann die Pixel verändert werden

           // https://stackoverflow.com/questions/4801366/convert-rgb-values-to-integer
           // image.setRGB(0,0,4);
           // MandelBrotRechnerService.calc(xAktuellerPunkt,yAktuelleerPunkt) ->iterationen

           // convert BufferedImage to string
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           boolean res = ImageIO.write(image, "png", baos);

           if(!res){return new HttpEntity<>("Fehler beim Bild lesen");}

           byte[] bytes = baos.toByteArray();
           baos.close();
           return new HttpEntity<>(Base64.getEncoder().encodeToString(bytes));

       }catch(Exception e){
            System.out.println("Exception");
       }
        return new HttpEntity<>("Server had Error");

    }

    /*
    // nur Test aufruf
    @GetMapping("/calcGet")
    String calcPixel2(){

        System.out.println("Get request made");
        try{

            // Beispiel für Anfrage von Client
            URL server = new URL("http://localhost:8080/calcolino/600/600/2/0/0");
            URLConnection connection = server.openConnection();
            HttpURLConnection http = (HttpURLConnection)connection;
            http.setRequestMethod("GET");
            http.setDoOutput(true);
            http.connect();

            // Recieve Http Reqeuest
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream()));
            String result="";
            String line;
            while((line = in.readLine()) != null){
                result+=line;
            }
            in.close();

            // Convert String back to image ...
            InputStream inputStream = new ByteArrayInputStream(Base64.getDecoder().decode(result));
            BufferedImage image = ImageIO.read(inputStream);
            if(image==null){
                return "image was null";
            }

            // test save  auf client unrelevant
            ImageIO.write(image, "png", new File("imageTwo.png"));

            return result;

        }catch (Exception e){

            System.out.println(e.getMessage());
        }
        return "none";
    } */

}
