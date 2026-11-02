import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import java.awt.geom.*;

public class DodgeBallClasses{

}
// Class: Character
class Character extends JLabel {
    private int x = 100;
    private int y = 290;
    private final int w = 70;
    private final int h = 130;
    private final Color navyblue = new Color(37, 125, 205);
    private KeyTracker leftright = new KeyTracker();

    public Character() {
        setBounds(x, y, w, h);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(leftright);
    }

    private void updatedPosition()
    {
        setBounds(x, y, w, h);
        repaint();
    }

    public Shape getCollisionShape() {
        Area collisionArea = new Area();
        
        // Head (rounded rectangle)
        collisionArea.add(new Area(new RoundRectangle2D.Float(
            10, 0,  // x,y (10px padding on sides)
            70, 70, // width,height 
            23, 23  // arc width,height
        )));
        
        // Body (rectangle)
        collisionArea.add(new Area(new Rectangle2D.Float(
            0, 10,  // x,y
            70, 70  // width,height (only actual body pixels)
        )));
        
        return collisionArea;
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(w, h);
    }

    @Override
    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        //head
        g2d.setColor(navyblue);
        g2d.fillRoundRect(0,0, 70, 70, 23, 23);
        //body
        g2d.setColor(navyblue);
        g2d.fillRect(0, 10, 70, 70);

        // Reset color for the eyes
        g2d.setColor(Color.WHITE);
        g2d.fillOval(20, 10, 17, 17);
        g2d.fillOval(47, 10, 17, 17);
    }

    public class KeyTracker implements KeyListener
    {
        @Override
        public void keyPressed(KeyEvent e)
        {
            if (e.getKeyCode() == KeyEvent.VK_A && x >= 15)
            {
                x -= 30;
                updatedPosition();
            } else if ( x < 15) {x = 15; updatedPosition();}

            if (e.getKeyCode() == KeyEvent.VK_D && x <= 530)
            {
                x += 30;
                updatedPosition();
            } else if (x > 530) {x = 530; updatedPosition();}
        }
        public void keyReleased(KeyEvent e){}
        public void keyTyped(KeyEvent e){}
    }

    // getter methods
    public int getX(){ return x;}
    public int getY(){ return y;}
    public int getWidth(){ return w;}
    public int getHeight(){ return h;}
}

// Class: Ground
class Ground extends JComponent {
    private int y = 350;
    public Ground(){
        setBounds(0, y, 627, 100);
        setBorder(new LineBorder(Color.BLACK, 4));
    }

    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.drawRect(0, 0, 627, 100);
    }

    public int getY(){
        return y;
    }
}

// Class: ScoreLabel
class ScoreLabel extends JLabel {
    public ScoreLabel()
    {
        setText("Score: 0");
        setFont(new Font("Sans Serif", Font.BOLD, 18));
        setBounds(20, 20, 150, 20);
    }

    public void updateScore(int score)
    {
        setText("Score: " + score);
    }
}

// Class: Balls
class Balls extends JLabel {
    private int x, y;   // Position of the Ball
    private int dy = 2; // Speed of the Ball
    
    private int ballSize = 50; // Ball diameter
    private boolean reachedGround = false; 
    private Random rand = new Random();

    public Balls(){
        resetPosition();
    }

    public void move() {
        if (!reachedGround) {
            y += dy;
            setBounds(x, y, ballSize, ballSize);
        }
    }

    public void resetPosition(){
        x = rand.nextInt((440 - ballSize)) + 30;
        y = 50; // Start from the top;
        reachedGround = false;  // Reset flag
    }

    public void markReachedGround(){
        reachedGround = true;   // Mark the ball as grounded
    }

    public boolean hasReachedGround(){
        return reachedGround;
    }

    public int getBallSize(){
        return ballSize;
    }

    public int getY(){
        return y;
    }
    
    @Override
    public void paintComponent(Graphics g)
    {   
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(new Color(204, 60, 23)); // Dark Orange
        g2d.fillOval(0, 0, ballSize, ballSize);  
    }

    @Override
    public Rectangle getBounds(){
        return new Rectangle(x, y, ballSize, ballSize);
    }
}

// Class
class customButton extends JButton{
    private int x,y,w,h;
    private String text;

    public customButton(String text, int x, int y, int w, int h){
        super();
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.text = text;

        setText(text);
        setFont(new Font("San Serif", Font.BOLD, 20));
        setPreferredSize( new Dimension(w, h));
        setBounds(x, y, w, h);
        setOpaque(false);
        setBorderPainted(false);
        setBackground(Color.WHITE);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        
        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, w, h, 15, 15);
        super.paintComponent(g);
    }
}