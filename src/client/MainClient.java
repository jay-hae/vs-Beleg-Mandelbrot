import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.*;
import java.awt.Graphics2D;
import java.awt.Graphics;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainClient extends JPanel implements MouseWheelListener{

    List<BufferedImage> bufferedImages = new ArrayList<BufferedImage>();

    int width = 800, height = 600;

    
    //JLabel lbl = new JLabel();

    int zoom = 0;

    public static void main(String avg[]) throws IOException
    {
        JFrame frame = new JFrame();
        MainClient abc=new MainClient(frame);
        //abc.displayImage(ImageIO.read(new File("image.png")));
        abc.bufferedImages.add(ImageIO.read(new File("image.png")));
        abc.repaint();
        frame.revalidate();
    }

    public MainClient(JFrame frame) throws IOException
    {
        frame.setLayout(new FlowLayout());
        frame.setSize(width, height);
        frame.setResizable(false);
        //frame.add(lbl);
        frame.add(this);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.addMouseWheelListener(this);
    }

    public void displayImage(BufferedImage bufferedImage)
    {
        //ImageIcon icon=new ImageIcon(bufferedImage);
        //lbl.setIcon(icon);
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        zoom -= e.getUnitsToScroll();
        System.out.println(zoom);
    }

    public BufferedImage[] getNewImages(int serverAmount)
    {
        int segmentWidth = (width / serverAmount);

        BufferedImage[] bufferedImages = new BufferedImage[serverAmount];

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


        }

        return bufferedImages;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            bufferedImages.set(0, ImageIO.read(new File("image.png")));
        }
        catch(IOException e)
        {

        }

        for(int i = 0; i < bufferedImages.size(); i++)
        {
            System.out.println("draw");
        g.drawOval(0, 0, getWidth(), getHeight());

            Graphics2D g2d = (Graphics2D) g.create();
            g2d.drawImage(bufferedImages.get(i), 5, 10, this);
            g2d.dispose();
        }
    }
}