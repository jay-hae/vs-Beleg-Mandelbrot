package main.java.mandelbrotbeleg.mandelbrotserver;

public class MandelBrotRechnerService {


    static int maxIter = 5000;
    static double maxBetrag2 = 35;

    public static int getMax_iter() {
        return maxIter;
    }

    public static void setMaxIter(int max_iter) {
        MandelBrotRechnerService.maxIter = max_iter;
    }

    public static double getMaxBetrag2() {
        return maxBetrag2;
    }

    public static void setMaxBetrag2(double max_betrag2) {
        MandelBrotRechnerService.maxBetrag2 = max_betrag2;
    }

    /**
     * @param cr Realteil
     * @param ci Imaginärteil
     * @return Iterationen
     */
    static public int calc(double cr, double ci) {
        int iter;
        double zr, zi, zr2 = 0, zi2 = 0, zri = 0, betrag2 = 0;
        //  z_{n+1} = z²_n + c
        //  z²  = x² - y² + i(2xy)
        // |z|² = x² + y²
        for (iter = 0; iter < maxIter && betrag2 <= maxBetrag2; iter++) {
            zr = zr2 - zi2 + cr;
            zi = zri + zri + ci;

            zr2 = zr * zr;
            zi2 = zi * zi;
            zri = zr * zi;
            betrag2 = zr2 + zi2;
        }
        return iter;
    }
}
