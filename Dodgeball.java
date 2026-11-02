import javax.swing.*;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.*;
import java.util.ArrayList;

class DodgeBall
{
    // GUI Instances
    private JFrame frame;   // game frame
    private JLayeredPane layeredPane; // To separate layers
    private Character sylus = new Character(); // player
    private Ground ground = new Ground();
    private ScoreLabel scoreL = new ScoreLabel();

    // Other Instances
    private ArrayList<Balls> balls = new ArrayList<>(); // List to store all the balls
    
    private Timer animationTimer;    // Timer for animation
    private Timer ballSpawn;    // Timer to spwan balls
    private final int MAXBALLS = 10;
    private int score = 0;      // Track score
    private final int WINNING_SCORE = 15; // Score needed to win

    public DodgeBall()
    {
        // Instances
        frame = new JFrame(); 
        frame.setSize(640,480);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // LayerdPane
        layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(frame.getSize());
        frame.setContentPane(layeredPane);
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLayout(null);

        layeredPane.add(ground, JLayeredPane.DEFAULT_LAYER); // Background
        layeredPane.add(scoreL, JLayeredPane.PALETTE_LAYER); // Foreground
        layeredPane.add(sylus, JLayeredPane.MODAL_LAYER); // Character in front layer

        addBalls(); // Add the balls

        animationTimer = new Timer(30, e -> { // 30 ms ball animation
            for (int i = balls.size() - 1; i >= 0; i--) {
                Balls ball = balls.get(i);
                
                ball.move();

                // Check for Collision
                if (checkCollision(ball)) {

                    frame.getContentPane().removeAll();
                    GameOverScreen();
                    animationTimer.stop();
                    ballSpawn.stop();
                    return;
                }

                // Check if ball reached ground
                if (ball.getY() + ball.getBallSize() >= ground.getY()  && !ball.hasReachedGround())
                {
                    ball.markReachedGround();   // Prevent continuous score incrementation
                    score++;
                    scoreL.updateScore(score);
                    
                    // Check if player won
                    if (score >= WINNING_SCORE){
                        frame.getContentPane().removeAll();
                        GameWonScreen();
                        animationTimer.stop();
                        ballSpawn.stop();
                        return;
                    }

                    balls.remove(i);
                    frame.remove(ball);
                    frame.revalidate();
                    frame.repaint();
                    continue;
                } 

                ball.repaint();
            }
            layeredPane.repaint();  // Repaint entire frame for smooth rendering
        });
        animationTimer.start();
    }

    private void addBalls()
    {
        ballSpawn = new Timer(1000, e -> { //1000ms ball spawn frquencies
            if (balls.size() < MAXBALLS)
            {
                Balls ball = new Balls();
                balls.add(ball);
                layeredPane.add(ball, JLayeredPane.MODAL_LAYER);
                ball.setBounds(0, 0, frame.getWidth(), frame.getHeight());
            }
        });
        ballSpawn.start();
        frame.setVisible(true);
    }

    // GameOverScreen
    private void GameOverScreen(){
        // Reset Frame
        frame.getContentPane().removeAll();
        frame.setLayout(null);

        JLabel youLoseL = new JLabel("You Lose :(", SwingConstants.CENTER);
        youLoseL.setFont(new Font("Sans Serif", Font.BOLD, 32));
        youLoseL.setBounds(0, 100, 640, 50);
        
        JButton restartB = new customButton("Restart", 220, 300, 200, 50);
        restartB.addActionListener(e -> restartGame());

        frame.add(youLoseL);
        frame.add(restartB);

        frame.revalidate();
        frame.repaint();
    }

    // GameWonScreen
    private void GameWonScreen() {
        // Reset Frame
        frame.getContentPane().removeAll();
        frame.setLayout(null);

        JLabel youWinL = new JLabel("You Win :) !", SwingConstants.CENTER);
        youWinL.setFont(new Font("Sans Serif", Font.BOLD, 32));
        youWinL.setBounds(0, 100, 640, 80);

        JButton restartB = new customButton("Play Again", 220, 300, 200, 50);
        restartB.addActionListener(e -> restartGame());

        frame.add(youWinL);
        frame.add(restartB);

        frame.revalidate();
        frame.repaint();
    }

    // Restart Game
    private void restartGame() {
        score = 0;
        scoreL.updateScore(score); // reset score to 0 on Label 
        balls.clear();  // clear balls arraylist

        frame.getContentPane().removeAll();
        frame.setLayout(null);

        // Re-add main components
        layeredPane.add(scoreL);
        layeredPane.add(sylus);
        layeredPane.add(ground);

        frame.revalidate();
        frame.repaint();

        // Restart Timer
        animationTimer.start();
        ballSpawn.start();

        sylus.requestFocusInWindow();
        sylus.addKeyListener(sylus.new KeyTracker()); // Re-add Listener 
    }

    // Collision detection
    private boolean checkCollision(Balls ball){
        Shape charShape = sylus.getCollisionShape();
        
        Rectangle ballsBounds = new Rectangle (
            ball.getX(),
            ball.getY(),
            ball.getBallSize(),
            ball.getBallSize()
        );

        // Precise collision check
        AffineTransform tx = new AffineTransform();
        tx.translate(sylus.getBounds().x, sylus.getBounds().y);
        Shape translatedCharShape = tx.createTransformedShape(charShape);
        
        // Using Area to detect collision
        Area ballArea = new Area(ballsBounds);
        ballArea.intersect(new Area(translatedCharShape));

        return !ballArea.isEmpty();
    }
}

