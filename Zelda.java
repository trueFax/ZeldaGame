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

public class Zelda {

    private static void healthDraw() {
        Graphics g = appFrame.getGraphics();
        Graphics g2D = (Graphics2D) g;

        int leftscale = 10;
        int leftoffset = 10;
        int rightoffset = 9;
        int interioroffset = 2;
        int halfinterioroffset  = 1; 

        for (int i = 0; i < p1.getMaxLife(); i++) {
            if (i % 2 == 0) {
                g2D.drawImage(rotateImageObject(p1).filter(leftHeartOutline, null), leftscale * i + leftoffset + XOFFSET, YOFFSET, null);
            }
            else {
                g2D.drawImage(rotateObject(p1).filter(rightHeartOutline, null), leftscale * i + rightoffet + XOFFSET, YOFFSET, null);
            }
        }

        for (int i = 0; i < p1.getMaxLife(); i++) {
            if (i % 2 == 0) {
                    g2D . drawImage ( rotateImageObject(p1).filter(leftHeart, null), leftscale * i + leftoffset + interioroffset + XOFFSET, interioroffset + YOFFSET, null ) ;
            }
            else {
                g2D.drawImage(rotateImageObject(p1).filter(rightHeart,null), leftscale * i + leftoffset - halfinterioroffset +
                XOFFSET, interioroffset + YOFFSET, null ) ;
            }
        }
    }

    public static void enemiesDraw() {

        Graphics g = appFrame.getGraphics();
        Graphics g2D = (Graphics2D) g;

        for (int i = 0; i < bluepigEnemies.size(); i++) {
            if (Math.abs(bluepigEnemies.elementAt(i).getInternalAngle() - 0.0) < 1.0 ) {
                if (bluepigEnemies.elementAt(i).getCurrentFrame() < bluepigEnemies.elementAt(i).getMaxFrames() / 2 ) {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(6), null ), (int)(bluepigEnemies.elementAt(i).getX() + 0.5 ), (int)(bluepigEnemies.elementAt(i).getY() + 0.5) , null);
                }
                else {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(7), null), (int)(bluepigEnemies.elementAt(i).get() + 0.5), (int)(bluepigEnemies.elementAt(i).getY() + 0.5), null);
                }
                bluepigEnemies.elementAt(i).updateCurrentFrame();
            }
            if (Math.abs(bluepigEnemies.elementAt(i).getInternalAngle() - pi) < 1.0) {
                if (bluepigEnemies.elementAt(i).getCurrentFrame() < bluepigEnemies.elementAt(i).getMaxFrames() / 2) {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(4), null), (int)(bluepigEnemies.elementAt(i).getX() + 0.5), (int)(bluepigEnemies.elementAt(i).getY() + 0.5 ), null );
                }
                else {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(5), null), (int)(bluepigEnemies.elementAt(i).getX() + 0.5) , (int)(bluepigEnemies.elementAt(i).getY() + 0.5), null );
                }
                bluepigEnemies.elementAt(i).updateCurrentFrame();
            }
            if (Math.abs(bluepigEnemies.elementAt(i).getInternalAngle() - halfPi) < 1.0) {
                if (bluepigEnemies.elementAt(i).getCurrentFrame() < bluepigEnemies.elementAt(i).getMaxFrames() / 2 ) {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(2), null), (int)(bluepigEnemies.elementAt(i).getX() + 0.5), (int)(bluepigEnemies.elementAt(i).getY() + 0.5 ), null );
                }
                else {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(3), null), (int)(bluepigEnemies.elementAt(i).getX() + 0.5) , (int)(bluepigEnemies.elementAt(i).getY() + 0.5), null );
                }
                bluepigEnemies.elementAt(i).updateCurrentFrame();
                }
            }
            if (Math.abs(bluepigEnemies.elementAt(i).getInternalAngle() - threehalvesPi) < 1.0) {
                if (bluepigEnemies.elementAt(i).getCurrentFrame() < bluepigEnemies.elementAt(i).getMaxFrames() / 2 ) {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(0), null), (int)(bluepigEnemies.elementAt(i).getX() + 0.5), (int)(bluepigEnemies.elementAt(i).getY() + 0.5 ), null );
                }
                else {
                    g2D.drawImage(rotateImageObject(bluepigEnemies.elementAt(i)).filter(bluepigEnemy.elementAt(1), null), (int)(bluepigEnemies.elementAt(i).getX() + 0.5) , (int)(bluepigEnemies.elementAt(i).getY() + 0.5), null );
                }
                bluepigEnemies.elementAt(i).updateCurrentFrame();
            }
        }   
    }

    private static class KeyPressed extends AbstractAction {

        public KeyPressed() {
            action = "";
        }

        public KeyPressed(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {
            if (action.equals("UP")) {
                upPressed = true;
                lastPressed = 90.0;
            }
            if (action.equals("DOWN")) {
                downPressed = true;
                lastPressed = 270.0;
            }
            if (action.equals("LEFT")) {
                leftPressed = true;
                lastPressed = 180.0;
            }
            if (action.equals("RIGHT")) {
                rightPressed = true;
                lastPressed = 0.0;
            }
            if (action.equals("A")) {
                aPressed = true;
            }
            if (action.equals("X")) {
                xPressed = true;
            }
        }
        private String action;
    }


    private static class KeyReleased extends AbstractAction {


        public KeyReleased() {
            action = "";
        }

        public KeyReleased(String input) {
            action = input;
        }

        public void actionPerformed(ActionEvent e) {

            if (action.equals("UP")) {
                upPressed = false;
            }
            if (action.equals("DOWN")) {
                downPressed = false;
            }
            if (action.equals("LEFT")) {
                leftPressed = false;
            }
            if (action.equals("RIGHT")) {
                rightPressed = false;
            }
            if (action.equals("A")) {

            }
            if (action.equals("X")) {
                xPressed = false;
            }
        }
        private String action;
    }

    private static class QuitGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true;
        }
    }

    private static class StartGame implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            endgame = true ;
            upPressed = false;
            downPressed = false;
            leftPressed = false;
            rightPressed = false;
            aPressed = false;
            xPressed = false;
            lastPressed = 90.0 ;
            backgroundState = "KI0809";
            availableToDropLife = true ;
            try {
                clearEnemies();
                generateEnemies(backgroundState);
            }
            catch(java.lang.NullPointerException jjlnpe) {

            }
            p1 = new ImageObject(p1originalX, p1originalY, p1width, p1height, 0.0);
            p1velocity = 0.0;
            p1.setInternalAngle(threehalvesPi); // 270 degrees, in radians
            p1.setMaxFrames(2);
            p1.setlastposx(p1originalX);
            p1.setlastposy(p1originalY);
            p1.setLife(6);
            p1.setMaxLife(6);
            doorKItoTC = new ImageObject(200, 55, 35, 35, 0.0);
            doorTCtoKI = new ImageObject(150, 270, 35, 35, 0.0);
            try {
                Thread.sleep(50);
            }
            catch (InterruptedException ie) {

            }
            lastAudioStart = System.currentTimeMillis();
            playAudio(backgroundState);
            endgame = false;
            lastDropLife = System.currentTimeMillis();
            Thread t1 = new Thread (new Animate());
            Thread t2 = new Thread (new PlayerMover());
            Thread t3 = new Thread (new CollisionChecker());
            Thread t4 = new Thread (new AudioLooper());
            Thread t5 = new Thread (new EnemyMover());
            Thread t6 = new Thread (new HealthTracker());
            t1.start();
            t2.start();
            t3.start();
            t4.start();
            t5.start();
            t6.start();

        }
    }

    private static class GameLevel implements ActionListener {
        public int decodeLevel(String input) {
            int ret = 3;
            if (input.equals("One")) {
                ret = 1;
            }
            else if (input.equals("Two")) {
                ret = 2;
            }
            else if (input.equals("Three")) {
                ret = 3;
            }
            else if (input.equals("Four")) {
                ret = 4;
            }
            else if (input.equals("Five")) {
                ret = 5;
            }
            else if (input.equals("Six")) {
                ret = 6;
            }
            else if (input.equals("Seven")) {
                ret = 7;
            }
            else if (input.equals("Eight")) {
                ret = 8;
            }
            else if (input.equals("Nine")) {
                ret = 9;
            }
            else if (input.equals("Ten")) {
                ret = 10;
            }
            return ret;
        }
        public void actionPerformed(ActionEvent e) {
            JComboBox cb = (JComboBox)e.getSource();
            String textLevel = (String)cb.getSelectedItem();
            level = decodeLevel(textLevel);
        }
    }

    private static Boolean isInside(double p1x, double p1y, double p2x1, double p2y1, double p2x2, double p2y2) {
        Boolean ret = false;
        if (p1x > p2x1 && p1x < p2x2) {
            if (p1y > p2y1 && p1y < p2y2) {
                ret = true;
            }
            if (p1y > p2y2 && p1y < p2y1) {
                ret = true;
            }
        }
        if (p1x > p2x2 && p1x < p2x1) {
            if (p1y > p2y1 && p1y < p2y2) {
                ret = true;
            }
            if (p1y > p2y2 && p1y < p2y1) {
                ret = true;
            }
        }
        return ret;
    }

    private static Boolean collisionOccursCoordinates(double p1x1, double p1y1, double p1x2, double p1y2, double p2x1,
                                                      double p2y1, double p2x2, double p2y2) {
        Boolean ret = false;
        if (isInside(p1x1, p1y1, p2x1, p2y1, p2x2, p2y2)) {
            ret = true;
        }
        if (isInside(p1x1, p1y2, p2x1, p2y1, p2x2, p2y2)) {
            ret = true;
        }
        if (isInside(p1x2, p1y1, p2x1, p2y1, p2x2, p2y2)) {
            ret = true;
        }
        if (isInside(p1x2, p1y2, p2x1, p2y1, p2x2, p2y2)) {
            ret = true;
        }
        if (isInside(p2x1, p2y1, p1x1, p1y1, p1x2, p1y2)) {
            ret = true;
        }
        if (isInside(p2x1, p2y2, p1x1, p1y1, p1x2, p1y2)) {
            ret = true;
        }
        if (isInside(p2x2, p2y1, p1x1, p1y1, p1x2, p1y2)) {
            ret = true;
        }
        if (isInside(p2x2, p2y2, p1x1, p1y1, p1x2, p1y2)) {
            ret = true;
        }
        return ret;
    }

    private static Boolean collisionOccurs(ImageObject obj1, ImageObject obj2) {
        Boolean ret = false;
        if (collisionOccursCoordinates(obj1.getX(), obj1.getY(), obj1.getX() + obj1.getWidth(), obj1.getY() + obj1.getHeight(), obj2.getX(), obj2.getY(), obj2.getX() + obj2.getWidth(), obj2.getY() + obj2.getHeight()) == true) {
            ret = true;
        }
        return ret;
    }


}

