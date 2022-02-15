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
    public static void main(String[] args){
        setup();
        appFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        appFrame.setSize(WINWIDTH+1, WINHEIGHT+85);
    
        JPanel myPanel = new JPanel();
        
        JButton quitButton = new JButton("Select");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);
    
        JButton newGameButton = new JButton("Start");
        newGameButton.addActionListener(new StartGame());
    
        JButton quitButton = new JButton ("Quit_Game");
        quitButton.addActionListener(new QuitGame());
        myPanel.add(quitButton);
    
        bindKey(myPanel, "UP");
        bindKey(myPanel,"DOWN");
        bindKey(myPanel, "LEFT");
        bindKey(myPanel, "RIGHT");
        bindKey(myPanel, "F");
        appFrame.getContentPane().add(myPanel, "South");
        appFrame.setVisible(true);
    }
    private static Boolean endgame ;
    private static Vector< Vector< BufferedImage > > backgroundKI ;
    private static Vector< Vector< BufferedImage > > backgroundTC ;
    private static Vector< Vector< Vector< ImageObject > > > wallsKI ;
    private static Vector< Vector< Vector< ImageObject > > > wallsTC ;
    private static int xdimKI ;
    private static int ydimKI ;
    private static int xdimTC ;
    private static int ydimTC ;
    private static BufferedImage player ;
    private static Vector< BufferedImage > link ;
    private static BufferedImage leftHeartOutline;
    private static BufferedImage rightHeartOutline;
    private static BufferedImage leftHeart;
    private static BufferedImage rightHeart ;
    private static Vector< BufferedImage > bluepigEnemy ;
    private static Vector< ImageObject > bluepigEnemies ;
    private static Vector< ImageObject > bubblebossEnemies ;
    private static ImageObject doorKItoTC ;
    private static ImageObject doorTCtoKI ;
    private static Boolean upPressed ;
    private static Boolean downPressed ;
    private static Boolean leftPressed ;
    private static Boolean rightPressed ;
    private static Boolean aPressed ;
    private static Boolean xPressed ;
    private static double lastPressed ;
    private static ImageObject p1 ;
    private static double p1width ;
    private static double p1height ;
    private static double p1originalX ;
    private static double p1originalY ;
    private static double p1velocity ;
    private static int level ;
    private static Long audiolifetime ;
    private static Long lastAudioStart ;
    private static Clip clip ;
    private static Long dropLifeLifetime ;
    private static Long lastDropLife ;
    private static int XOFFSET;
    private static int YOFFSET;
    private static int WINWIDTH;
    private static int WINHEIGHT;
    private static double pi ;
    private static double quarterPi ;
    private static double halfPi;
    private static double threequartersPi ;
    private static double fivequartersPi;
    private static double threehalvesPi;
    private static double sevenquartersPi;
    private static double twoPi;
    private static JFrame appFrame ;
    private static String backgroundState ;
    private static Boolean availableToDropLife;
    private static final int IFW = JComponent.WHEN_IN_FOCUSED_WINDOW;


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
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 0 , 35 , 135, 35 , 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 100, 70 , 35 , 140, 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 35 , 135, 35 , 100, 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 0 , 170, 35 , 70 , 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 0 , 235, 35 , 70 , 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 0 , 270, 135, 35 , 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 170, 270, 135, 35 , 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 300, 35 , 35 , 270, 0.0 ) ) ;
                        wallsKI. elementAt ( i ).elementAt ( j ).addElement ( new ImageObject ( 235, 35 , 70 , 35 , 0.0 ) ) ;

                    }
                }
            }
            // setting up the Tail Cave Images
            xdimTC = 9; //7; //TODO: need to be able to just use 7 and 6, not 9 and 8.
            ydimTC = 8; //6;
            backgroundTC = new Vector <Vector <BufferedImage>>();
            for(int i = 0; i < ydimTC; i++) {
                Vector<BufferedImage> temp = new Vector <BufferedImage>();
                for(int j = 0; j < xdimTC; j++) {
                    BufferedImage tempImg = ImageIO.read(new File("blank.png"));
                    temp.addElement(tempImg);
                } 
                backgroundTC.addElement(temp);
            }

        for(int i = 0; i < backgroundTC.size(); i++) {
            for(int j = 0; j < backgroundTC.elementAt(i).size(); j++){
                if((j == 0 && i == 2) || (j == 0 && i == 3) || (j == 0 && i == 4) || (j == 1 && i == 1) || ( j == 1 && i == 3 ) || ( j == 1 && i == 5 ) || ( j == 2 &&
                i == 1 ) || ( j == 2 && i == 2 ) ||
                ( j == 2 && i == 3 ) || ( j == 2 && i == 4 ) || ( j == 2 &&
                i == 5 ) || ( j == 2 && i == 6 ) ||
                ( j == 3 && i == 1 ) || ( j == 3 && i == 2 ) || ( j == 3 &&
                i == 3 ) || ( j == 3 && i == 4 ) ||
                ( j == 3 && i == 5 ) || ( j == 4 && i == 2 ) || ( j == 4 &&
                i == 3 ) || ( j == 4 && i == 4 ) ||
                ( j == 5 && i == 2 ) || ( j == 5 && i == 3 ) || ( j == 6 &&
                i == 0 ) || ( j == 6 && i == 1 ) ||
                ( j == 6 && i == 2 ) || ( j == 6 && i == 3 )){
                    String filename = "TC";
                    if(j < 10){
                        filename = filename + "0";
                    }
                    filename = filename + j;
                    if (i < 10) {
                        filename = filename + 1 + ".png";
                        //System.out.println(filename);
                        backgroundTC.elementAt(i).set(j, ImageIO.read(new File(filename)));
                    }
                }
            }
            //setting up the Tail Cave walls
            wallsTC = new Vector<Vector<Vector<ImageObject>>>();
            for(int i = 0; i < ydimTC; i++){
                Vector<Vector<ImageObject>> temp = new Vector<Vector<ImageObject>>();
                for(int j = 0; j < xdimTC; j++){
                    Vector<ImageObject> tempWalls = new Vector<ImageObject>();
                    temp.addElement(tempWalls);
                }
                wallsTC.add(temp);
            }
            player = ImageIO.read(new file("link00.png"));
             //links images
            link = new Vector<BufferedImage>();
            for(int i = 0; i < 72; i++){
                if(i < 10){
                    String filename = "link0" + i + ".png";
                    link.addElement(ImageIO.read(new File(filename)));

                } else {
                    String filename = "link" + i + ".png";
                    link.addElement(ImageIO.read(new File(filename)));
                }
            }
            //bluepig enemy images 
            bluepigEnemies = new Vector <ImageObject>();
            bluepigEnemy = new Vector <BufferImage>();
            bluepigEnemy.addElement(ImageIO.read(new File("BPB1.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPB2.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPF1.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPF2.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPL1.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPL2.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPR1.png")));
            bluepigEnemy.addElement(ImageIO.read(new File("BPR2.png")));

            //BubbleBoss Enemies
            bubblebossEnemies = new Vector<ImageObject>();
            //Health Images
            leftHeartOutline = ImageIO.read(new File("heartOutlineLeft.png"));
            rightHeartOutline = ImageIO.read(new File("heartOutlineRight.png"));
            rightHeart = ImageIO.read(new File("heartRight.png"));
            leftHeart = ImageIO.read(new File("heartLeft.png"));
            
        } 
        catch (IOException ioe) {} // left off on page 115 
    }
    private stati class Animate implements Runnable{
        public void run(){
            while(endgame == false){
                backgroundDraw();
                enemiesDraw();
                playerDraw();
                healthDraw();

                try{
                    Thread.sleep(32);
                }
                catch(InterruptedException e) {

                }
            }
        }
    }
    private static class AudioLooper implements Runnable{
        public void run(){
            while(endgame == false) {
                Long curTime = new Long(System.currentTimeMillis());
                if(curTime - lastAudioStart > audiolifetime) {
                    playAudio(backgroundState)
                }
            }
        }
    }
    private static void playAudio(String backgroundState){
        try{
            clip.stop();
        }
        catch(Exception e){
            //NOP
        }
        try{
            if(backgroundState.substring(0, 2).equals("KI")){
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("KI.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                lastAudioStart = System.currentTimeMillis();

            }
            else if(backgroundState.substring(0,2).equals("TC")) {
                AudioInputStream ais = AudioSystem.getAudioInputStream(new File("TC.wav").getAbsoluteFile());
                clip = AudioSystem.getClip();
                clip.open(ais);
                clip.start();
                lastAudioStart = System.currentTimeMillis();
                audiolifetime = new Long(191000);

            }
        }
        catch(Exception e) {
            //NOP
        }
    }
    private static String bgWrap(String input, int wrap){
        String ret = input;
        if (wrap == 0){
            //NOP
        }
        else if (wrap == 1) {
            int xcoord = Integer.parseInt(Input.substring(2,4));
            int ycoord = Integer.parseInt(input.substring(4,6));

            xcoord = xcoord + 1;
            if(xcoord < 10){
                ret = input.substring(0, 2) + "0" + xcoord;

            }
            else {
                ret = input.substring(0, 2) + xcoord;
            }
            if(ycoord < 10){
                ret = ret + "0" + ycoord;
            }
            else {
                ret = ret + ycoord;
            }
        }
        else if(wrap == 2) { //left
            int xcoord = Integer.parseInt(input.substring(2, 4));
            int ycoord = Integer.parseInt(input.substring(4,6));

            xcoord = xcoord - 1;

            if(xcoord < 10){
                ret = input.substring(0,2) + "0" + xcoord;
            }
            else {
                ret = input.substring(0, 2) + xcoord;
            }
            if( ycoord < 10) {
                ret = ret + "0" + ycoord;
            } else{
                ret = ret + ycoord;
            }
        }
        else if(wrap == 4) //up
        {
            int xcoord = Integer.parseInt(input.substring(2, 4));
            int ycoord = Integer.parseInt(input.substring(4, 6));

            ycoord = ycoord- 1;

            if(xcoord < 10){
                ret = input.substring(0, 2) + "0" + xcoord;
            }
            else {
                ret = input.substring(0, 2) + xcoord;
            }
            if(ycoord < 10) {
                ret = ret + "0" + ycoord;
            } 
            else {
                ret = ret + ycoord;
            }
        }
        return ret;
    }

// start on psc animate

} 
