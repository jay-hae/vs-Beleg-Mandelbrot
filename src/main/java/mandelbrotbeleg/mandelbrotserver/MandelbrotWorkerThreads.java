public class MandelbrotWorkerThreads implements Runnable{
    int anzThreads; 
    int currentThread;
    int width, height;
    Double scale;
    Double originX, originY;
    BufferedImage buffImage;

    public MandelbrotWorkerThreads(int anz, int curr){
        this.anzThreads = anz;
        this.currentThread = curr;
    }

    public updateThread(int width,int height,Double scale,Double originX,Double originY, BufferedImage buffImage){
        this.width = width;
        this.height = height;
        this.scale = scale;
        this.originX = originX;
        this.originY = originY;
        this.buffImage = buffImage;
    }


    public void run(){
        Double currentPixelX;
        Double currentPixelY;

        for(int i=0; i<width; i++){

            currentPixelX = this.originX + i * scale;

            for(int j=0;j<height;j++){
                currentPixelY = this.originY - j * scale;
                int iter = MandelbrotWorkerThreads.calcIter(currentPixelX,currentPixelY);
                buffImage.setRGB(i, j, MandelbrotWorkerThreads.farbwert(iter));
            }
        }
    }

    static public int calcIter(double cr, double ci) {

        int iter=0;
        double zr=0;
        double zi=0;
        double zr2 = 0;
        double zi2 = 0;
        double zri = 0;
        double betrag2 = 0;

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

    static Integer farbwert(int iter) {

        int[] F = new int[3];
        for (int i = 1; i < farben.length - 1; i++) {
            if (iter < farben[i][0]) {
                int iterationsInterval = farben[i - 1][0] - farben[i][0];
                double gewichtetesMittel = (iter - farben[i][0]) / (double) iterationsInterval;

                for (int f = 0; f < 3; f++) {
                    int farbInterval = farben[i - 1][f + 1] - farben[i][f + 1];
                    F[f] = (int) (gewichtetesMittel * farbInterval) + farben[i][f + 1];
                }
                return new Color(F[0], F[1], F[2]).getRGB();
            }
        }
        return Color.BLACK.getRGB();
    }

}