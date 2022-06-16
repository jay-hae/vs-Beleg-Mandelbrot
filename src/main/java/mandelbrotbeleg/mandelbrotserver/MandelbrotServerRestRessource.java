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

           // Calc Image
           MandelBrotRechnerService service = new MandelBrotRechnerService();
           BufferedImage image = service.calc(width,height,scale,originX,originY);

           // convert BufferedImage to string
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           boolean res = ImageIO.write(image, "png", baos);

           if(!res){return new HttpEntity<>("Fehler beim Bild lesen");}

           byte[] bytes = baos.toByteArray();
           baos.close();
           return new HttpEntity<>(Base64.getEncoder().encodeToString(bytes));

       }catch(Exception e){
            e.printStackTrace();
            System.err.println("Exception");
       }
        return new HttpEntity<>("Server had Error");

    }
}
