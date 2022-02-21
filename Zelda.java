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
    // begining on True's code 
    private static class PlayerMover implements Runnable
    {
        public PlayerMover()
        {
            velocitystep = 3;
        }
        public void run()
        {
            while(endgame == false)
            {
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {

                }
                if(upPressed || downPressed || leftPressed || rightPressed)
                {
                    p1velocity = velocitystep;
                    if(upPressed)
                    {
                        if(leftPressed)
                        {
                            p1.setInternalAngle(fivequartersPi);
                        }
                        else if(rightPressed)
                        {
                            p1.setInternalAngle(5.49779);
                        }
                        else
                        {
                            p1.setInternalAngle(threehalvesPi);
                        }
                    }
                    if(downPressed)
                    {
                        if(leftPressed)
                        {
                            p1.setInternalAngle(2.35619);
                        }
                        else if (rightPressed)
                        {
                            p1.setInternalAngle(quarterPi);
                        }
                        else
                        {
                            p1.setInternalAngle(halfPi);
                        }
                    }
                    if(leftPressed)
                    {
                        if(upPressed)
                        {
                            p1.setInternalAngle(fivequartersPi);
                        }
                        else if(downPressed)
                        {
                            p1.setInternalAngle(threequartersPi);
                        }
                        else
                        {
                            p1.setInternalAngle(pi);
                        }
                    }
                    if(rightPressed)
                    {
                        if(upPressed)
                        {
                            p1.setInternalAngle(5.49779);
                        }
                        else if (downPressed)
                        {
                            p1.setInternalAngle(quarterPi);
                        }
                        else
                        {
                            p1.setInternalAngle(0.0);
                        }
                    }
                }
                else 
                {
                    p1velocity = 0.0;
                    p1.setInternalAngle(threehalvesPi);
                }
                p1.updateBounce();
                p1.move(p1velocity * Math.cos(p1.getInternalAngle()), p1velocity * Math.sin(p1.getInternalAngle()));
                int wrap = p1.screenWrap(XOFFSET, XOFFSET + WINWIDTH, YOFFSET, YOFFSET + WINHEIGHT);
                backgroundState = bgWrap(backgroundState, wrap);
                if(wrap != 0)
                {
                    clearEnemies();
                    generateEnemies(backgroundState);
                }
            }
        }
        private double velocitystep;
    }

    private static void clearEnemies()
    {
        bluepigEnemies.clear();
        bubblebossEnemies.clear();
    }
    private static void generateEnemies(String backgroundState)
    {
        if(backgroundState.substring(0, 6).equals("KI0809"))
        {
            bluepigEnemies.addElement(new ImageObject(20, 90, 33, 33, 0.0));
            bluepigEnemies.addElement(new ImageObject(250, 230, 33, 33, 0.0));
        }
        for (int i = 0; i < bluepigEnemies.size(); i++)
        {
            bluepigEnemies.elementAt(i).setMaxFrames(25);
        }
    }
    private static class EnemyMover implements Runnable
    {
        public EnemyMover()
        {
            bluepigvelocitystep = 2;
        }
        public void run()
        {
            Random randomNumbers = new Random(LocalTime.now().getNano());
            while (endgame == false)
            {
                try
                {
                    Thread.sleep(10);
                }
                catch (InterruptedException e)
                {
                    // NOP
                }

                // TODO
                try
                {
                    for (int i = 0; i < bluepigEnemies.size(); i++)
                    {
                        int state = randomNumbers.nextInt(1000);
                        if(state < 5)
                        {
                            bluepigvelocity = bluepigvelocitystep;
                            bluepigEnemies.elementAt(i).setInternalAngle(0);
                        }
                        else if (state < 10)
                        {
                            bluepigvelocity = bluepigvelocitystep;
                            bluepigEnemies.elementAt(i).setInternalAngle(halfPi);
                        }
                        else if(state < 15)
                        {
                            bluepigvelocity = bluepigvelocitystep;
                            bluepigEnemies.elementAt(i).setInternalAngle(pi);
                        }
                        else if (state < 20)
                        {
                            bluepigvelocity = bluepigvelocitystep;
                            bluepigEnemies.elementAt(i).setInternalAngle(threehalvesPi);
                        }
                        else
                        {
                            bluepigvelocity = 0;
                        }
                        bluepigEnemies.elementAt(i).updateBounce();
                        bluepigEnemies.elementAt(i).move(bluepigvelocity * Math.cos(bluepigEnemies.elementAt(i).getInternalAngle()), bluepigvelocity * Math.sin(bluepigEnemies.elementAt(i).getInternalAngle()));
                    }
                    for (int i = 0; i < bubblebossEnemies.size(); i++)
                    {

                    }
                }
                catch (java.lang.NullPointerException jlnpe)
                {
                    // NOP
                }
            }
        }
        private double bluepigvelocitystep;
        private double bluepigvelocity;
    }

    private static class HealthTracker implements Runnable
    {
        public void run()
        {
            while (endgame == false)
            {
                Long curTime = new Long(System.currentTimeMillis());
                if(availableToDropLife && p1.getDropLife() > 0)
                {
                    int newLife = p1.getLife() - p1.getDropLife();
                    p1.setDropLife(0);
                    availableToDropLife = false;

                    lastDropLife = System.currentTimeMillis();
                    p1.setLife(newLife);
                    try
                    {
                        AudioInputStream ais = AudioSystem.getAudioInputStream(new File("hurt.wav").getAbsoluteFile());
                        Clip hurtclip = AudioSystem.getClip();
                        hurtclip.open(ais);
                        hurtclip.start();
                    }
                    catch (Exception e)
                    {

                    }
                }
                else
                {
                    if (curTIme - lastDropLife > dropLifeLifetime)
                    {
                        availableToDropLife = true;
                    }
                }
            }
        }
    }

    private static class CollisionChecker implements Runnable
    {
        public void run()
        {
            // Random randomNumbers = new Random(LocalTime.now().getNano())
            while(endgame == false)
            {
                // check player against doors in given scenes
                if(backgroundState.substring(0, 6).equals("KI0511"))
                {
                    if(collisionOccurs(p1, doorKItoTC))
                    {
                        p1.moveto(p1originalX, p1originalY);
                        backgroundState = "TC0305";
                        clip.stop();
                        playAudio(backgroundState);
                    }
                }
                else if(backgroundState.substring(0, 6).equals("TC0305"))
                {
                    if(collisionOccurs(p1, doorTCtoKI))
                    {
                        p1.moveto(p1originalX, p1originalY);
                        backgroundState = "KI0511";
                        clip.stop();
                        playAudio(backgroundState);
                    }
                }

                // check player and enemies against walls
                if (backgroundState.substring(0, 6).equals("KI0510"))
                {
                    checkMoversAgainstWalls(wallsKI.elementAt(5).elementAt(10));
                }
                if(backgroundState.substring(0, 6).equals("KI0809"))
                {
                    checkMoversAgainstWalls(wallsKI.elementAt(8).element(9));
                }

                // check player against enemies
                for (int i = 0; i < bluepigEnemies.size(); i++)
                {
                    if (collisionOccurs(p1, bluepigEnemies.elementAt(i)))
                    {
                        // System.out.println("Still Colliding: " + I " , " + System.currentTimeMillis());
                        p1.setBounce(true);
                        bluepigEnemies.elementAt(i).setBounce(true);
                        if(availableToDropLife)
                        {
                            p1.setDropLife(1);
                        }
                    }
                }
                // TODO: check enemies against walls

                // TODO: check player against deep water or pits

                // TODO: check player against enemy arrows

                // TODO: check enemies against player weapons
            }
        }
        private static void checkMoversAgainstWalls(Vector< ImageObject > wallsInput)
        {
            for (int i = 0; i < wallsInput.size(); i++)
            {
                if(collisionOccurs(p1, wallsInput.elementAt(i)))
                {
                    p1.setBounce(true);
                }
                for (int j = 0; j < bluepigEnemies.size(); j++)
                {
                    if (collisionOccurs(bluepigEnemies.elementAt(j), wallsInput.elementAt(i)))
                    {
                        bluepigEnemies.elementAt(j).setBounce(true);
                    }
                }
            }
        }
    }

    // TODO: make one lockrotate function which takes as input objInner, objOuter, and point relative to objInner's x,y that objOuter must rotate around
    // dist is a distance between the two objects at the bottom of objInner.
    private static void lockrotateObjAroundObjbottom(ImageObject objOuter, ImageObject objInner, double dist)
    {
        objOuter.moveto(objInner.getX() + (dist + objInner.getWidth() / 2.0) * Math.cos(-objInner.getAngle() + pi/2.0) + objOuter.getWidth() / 2.0, objInner.getY() + (dist + objInner.getHeight() / 2.0) * Math.sin(-objInner.getAngle() + pi/2.0) + objOuter.getHeight() / 2.0);
        objouter.setAngle(objInner.getAngle());
    }

    // dist is a distance between the two objects at the top of the inner object
    private static void lockrotateObjAroundObjtop(ImageObject objOuter, ImageObject objInner, double dist)
    {
        objOuter.moveto(objInner.getX() + objOuter.getWidth() + (objInner.getWidth() / 2.0 + (dist + objInner.getWdith() / 2.0) * Math.cos(objInner.getAngle() + pi / 2.0)) / 2.0, objInner.getY() - objOuter.getHeight() + (dist + objInner.getHeight() / 2.0) * Math.sin(onobjInner.getAngle() / 2.0));
        objOuter.setAngle(objInner.getAngle());
    }

    private static AffineTransformOp rotateImageObject(ImageObject obj)
    {
        AffineTransform at = AffineTransform.getRotateInstance(-obj.getAngle(), obj.getWidth() / 2.0, obj.getHeight() / 2.0);
        AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }

    private static AffineTransformOp spinImageObject(ImageObject obj)
    {
        AffineTransform at = AffineTransform.getRotateInstance(-obj.getInternalAngle(), obj.getWidth() / 2.0, obj.getHeight() / 2.0);
        AffineTransformOp atop = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        return atop;
    }

    private static void backgroundDraw()
    {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;

        if(backgroundState.substring(0, 2).equals("KI"))
        {
            int i = Integer.parseInt(backgroundState.substring(4, 6));
            int j = Integer.parseInt(backgroundState.substring(2, 4));
            if(i < backgroundTC.size())
            {
                if (j < backgroundTC.elementAt(i).size())
                {
                    g2D.drawImage(backgroundTC.elementAt(i).elementAt(j), XOFFSET, YOFFSET, null);
                }
            }
        }
    }

    private static void playerDraw()
    {
        Graphics g = appFrame.getGraphics();
        Graphics2D g2D = (Graphics2D) g;

        if(upPressed || downPressed || leftPressed || rightPressed)
        {
            if(upPressed == true)
            {
                if(p1.getCurrentFrame() == 0)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(4), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                else if(p1.getCurrentFrame() == 1)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(5), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                p1.updateCurrentFrame();
            }

            if(downPressed == true)
            {
                if(p1.getCurrentFrame() == 0)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(2), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                else if(p1.getCurrentFrame() == 1)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(3), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                p1.updateCurrentFrame();
            }
            if(leftPressed == true)
            {
                if(p1.getCurrentFrame() == 0)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(0), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                else if(p1.getCurrentFrame() == 1)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(1), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                p1.updateCurrentFrame();
            }
            if(rightPressed == true)
            {
                if(p1.getCurrentFrame() == 0)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(6), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                else if (p1.getCurrentFrame() == 1)
                {
                    g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(7), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
                }
                p1.updateCurrentFrame();
            }
        }
        else
        {
            if(Math.abs(lastPressed - 90.0) < 1.0)
            {
                g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(4), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
            }
            if(Math.abs(lastPressed - 270.0) < 1.0)
            {
                g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(2), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
            }
            if(Math.abs(lastPressed - 0.0) < 1.0)
            {
                g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(6), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
            }
            if(Math.abs(lastPressed - 180.0) < 1.0)
            {
                g2D.drawImage(rotateImageObject(p1).filter(link.elementAt(0), null), (int)(p1.getX() + 0.5), (int)(p1.getY() + 0.5), null);
            }
        }
    }
    // Matt's code
}