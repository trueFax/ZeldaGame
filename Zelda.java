import java.util.Vector ;
import java.util.Random;
import java.time.LocalTime ;
import java.time.temporal.ChronoUnit;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing .JComponent;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import javax.swing .JComboBox;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import javax.sound.sampled.AudioInputStream; 
import javax.sound.sampled.AudioSystem ;
import javax.sound.sampled.Clip ;

// go until pg 119 playerMover 
public class Zelda {
    public Zelda() {
        setup();
    }

    public static void setup(){
        appFrame = new JFrame("The Legend of Zelda: Link's Awakening");
        XOFFSET = 0;
        YOFFSET = 40;
        WINWIDTH = 338;
        WINHEIGHT = 271;
        pi = Math.PI;
        quarterPi = pi /4;
        halfPi = pi/2;
        threeQuartersPi = pi * 0.75;
        fivequartersPi = 1.25 * pi;
        threehalvesPi = 1.5 * pi;
        sevenquartersPi = 1.75 * pi;
        twoPi = 2.0 * pi;
        endgame = false;
        p1width = 20; //18.5
        p1height = 20; //25
        p1originalX = (double)XOFFSET + ((double)WINWIDTH / 2.0) - (p1width / 2.0);
        p1originalY = (double)YOFFSET + ((double)WINHEIGHT / 2.0) - (p1height /2.0);
        level = 3;
        audiolifetime = new Long(78000);
        dropLifetime = new Long(1000);
        try{
            xdimKI = 16;
            ydimKI = 16;
            backgroundKI = new Vector<Vector<BufferedImage>>();

            for(int i =0; i < ydimKI; i++) {
                Vector<BufferedImage> temp = new Vector<BufferedImage> ();
                for(int j = 0; j < xdimKI; j++){
                    BufferedImage tempImg = ImageIO.read(new File("blank.png"));
                    temp.addElement(tempImg);
                }
                backgroundKI.addElement(tempImg);
            }
            for(int i = 0; i < backgroundKI.size(); i++){
                for (int j = 0; j < backgroundKI.elementAt(i).size(); j++){
                    if((j == 5 && i == 10) || (j == 5 && i == 11) || (j == 6 && i == 10) || (j == 6 &&i == 11) 
                    || (j == 7 && i == 10) || (j == 7 && i == 11) || (j == 8 && i ==9) || (j ==8 && i == 10)){ // TODO: Swap j and i 
                        String filename = "KI";
                        if (j < 10) {
                            filename = filename + "0";
                        }
                        filename = filename + j;
                        if (i < 10) {
                            filename = filename + "0";
                        }
                        filename = filename + i + ".png";
                        //System.out.println(filename);
                        backgroundKI.elementAt(i).set(j, ImageIO.read(new File (filename)));
                    }
                }
            }
            //setting up the Koholint Island Walls 
            wallsKI = new Vector<Vector<Vector<ImageObject>>>();
            for(int i = 0; i < ydimKI; i++){
                Vector<Vector<ImageObject>> temp = new Vector<Vector<ImageObject>>();
                for( int j = 0; j < xdimKI; j++){
                    Vector<ImageObject> tempWalls = new Vector <ImageObject>();
                    temp.addElement(tempWalls);
                }
                wallsKI.add(temp);
            }
            for(int i = 0; i < wallsKI.size(); i++){
                for(int j = 0; j < wallsKI.elementAt(i).size(); j++) {
                    if (i ==5 && j ++ 10){
                        wallsKI.elementAt(i).elementAt(j).addElement(new ImageObject( 270, 35, 68, 70, 0.0));
                        wallsKI.elementAt(i).elementAt(j).addElement(new ImageObject( 100, 100, 200, 35, 0.0));
                        wallsKI.elementAt(i).elementAt(j).addElement(new ImageObject( 100, 135, 35, 35, 0.0));
                        wallsKI.elementAt(i).elementAt(j).addElement(new ImageObject( 0, 165, 35, 135, 0.0));
                        wallsKI.elementAt(i).elementAt(j).addElement(new ImageObject( 100, 200, 35, 70, 0.0));
                        wallsKI.elementAt(i).elementAt(j).addElement(new ImageObject( 135, 270, 200, 35, 0.0));
                    }
                    if(i == 8 && j == 9){ //PAGE 112

                    }
                }
            }
        }


    }
}