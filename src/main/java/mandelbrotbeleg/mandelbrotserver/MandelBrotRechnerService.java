//package main.java.mandelbrotbeleg.mandelbrotserver;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
public class MandelBrotRechnerService {


    static int maxIter = 500;
    static double maxBetrag = 35;
    double xmin = -1.666, xmax = 1, ymin = -1, ymax = 1; // Parameter des Ausschnitts
    double cr = -0.743643887036151, ci = 0.131825904205330;
    double zoomRate = 1.5;
    int xpix = 640, ypix = 480;
    int y_sta=0, y_sto=400; 

    /*
    final int[][] farben = {
        {1, 255, 255, 255}, // Hohe Iterationszahlen sollen hell,
        {30, 10, 255, 40}, //
        {300, 10, 10, 40}, // die etwas niedrigeren dunkel,
        {500, 205, 60, 40}, // die "Spiralen" rot
        {850, 120, 140, 255}, // und die "Arme" hellblau werden.
        {1000, 50, 30, 255}, // Innen kommt ein dunkleres Blau,
        {1100, 0, 255, 0}, // dann grelles Grün
        {1997, 20, 70, 20}, // und ein dunkleres Grün.
        {max_iter, 0, 0, 0}
    }; // Der Apfelmann wird schwarz.
    */
    int[][] matrixIter;
    BufferedImage bufImg;

    public MandelBrotRechnerService(int imgPartWidth, int imgPartHeight){ //bestimmt zu bemalende Bild größe
        bufImg = new BufferedImage(640, 480,BufferedImage.TYPE_INT_RGB); 
        matrixIter = new int[xpix][ypix];

    }
    public static int getMax_iter() {
        return maxIter;
    }

    public static void setMaxIter(int max_iter) {
        MandelBrotRechnerService.maxIter = max_iter;
    }

    public static double getMaxBetrag() {
        return maxBetrag;
    }

    public static void setMaxBetrag2(double max_betrag) {
        MandelBrotRechnerService.maxBetrag = max_betrag;
    }

    public BufferedImage getBuffImage(){
        return bufImg;
    }
    public void draw(){
        int rgb = 0xFF00FF00;
        for(int i=0;i<=xpix-40;i++){
            for(int j=0;j<=ypix-40;j++){
                bufImg.setRGB(i, j, rgb);
            } 
        }
    }
    /**
     * @param cr Realteil
     * @param ci Imaginärteil
     * @return Iterationen
     *
    public void calPoint(){
        double c_re, c_im;
        for (int y = y_sta; y < y_sto; y++) {
            c_im = ymin + (ymax - ymin) * y / ypix;

            for (int x = 0; x < xpix; x++) {
                c_re = xmin + (xmax - xmin) * x / xpix;
                int iter = calc(c_re, c_im);
                bildIter[x][y] = iter;
                Color pix = farbwert(iter); // Farbberechnung
                // if (iter == max_iter) pix = Color.RED; else pix = Color.WHITE;
                // v.image.setRGB(x, y, pix.getRGB()); // rgb
                bild[x][y] = pix;
            }
        }
    }
    */
    static public int calcIter(double cr, double ci) {
        int iter;
        double zr, zi, zr2 = 0, zi2 = 0, zri = 0, betrag2 = 0;
        //  z_{n+1} = z²_n + c
        //  z²  = x² - y² + i(2xy)
        // |z|² = x² + y²
        for (iter = 0; iter < maxIter && betrag2 <= maxBetrag; iter++) {
            zr = zr2 - zi2 + cr;
            zi = zri + zri + ci;

            zr2 = zr * zr;
            zi2 = zi * zi;
            zri = zr * zi;
            betrag2 = zr2 + zi2;
        }
        return iter;
    }
    
    static public void main( String args[]){
        MandelBrotRechnerService apfel = new MandelBrotRechnerService(50,500);
        apfel.draw();
        BufferedImage bImg = apfel.getBuffImage();

        try {
            if(bImg == null)
                System.out.println("kein Bild vorhanden");
            else
                ImageIO.write(bImg,"jpeg",new File("./bild01.jpg"));
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
/*
    Color farbwert(int iter) {
        if (!farbe) {
            if (iter == max_iter) return Color.BLACK;
            else return Color.RED;
        }
        int[] F = new int[3];
        for (int i = 1; i < farben.length - 1; i++) {
            if (iter < farben[i][0]) {
                int iterationsInterval = farben[i - 1][0] - farben[i][0];
                double gewichtetesMittel = (iter - farben[i][0]) / (double) iterationsInterval;

                for (int f = 0; f < 3; f++) {
                    int farbInterval = farben[i - 1][f + 1] - farben[i][f + 1];
                    F[f] = (int) (gewichtetesMittel * farbInterval) + farben[i][f + 1];
                }
                return new Color(F[0], F[1], F[2]);
            }
        }
        return Color.BLACK;
    }
*/
}
