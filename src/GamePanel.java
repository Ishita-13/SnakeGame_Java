import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    // Board
    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;

    // Snake
    final int[] x = new int[900];
    final int[] y = new int[900];

    int bodyParts = 6;

    // Food
    int foodX;
    int foodY;

    // Score
    int score = 0;

    // Direction
    char direction = 'R';

    // Game state
    boolean running = false;

    // Timer
    Timer timer;

    // Random
    Random random = new Random();


    // Constructor
    public GamePanel() {

        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        addKeyListener(this);

        startGame();
    }


    // Start game
    public void startGame() {

        bodyParts = 6;
        score = 0;
        direction = 'R';
        running = true;

        // Starting position of snake
        for (int i = 0; i < bodyParts; i++) {

            x[i] = 100 - (i * UNIT_SIZE);
            y[i] = 100;
        }

        // Create first food
        newFood();

        // Start timer
        timer = new Timer(100, this);
        timer.start();
    }


    // Create food
    public void newFood() {

        int margin = 40;

        foodX = margin +
                random.nextInt((SCREEN_WIDTH - 2 * margin) / UNIT_SIZE)
                        * UNIT_SIZE;

        foodY = margin +
                random.nextInt((SCREEN_HEIGHT - 2 * margin) / UNIT_SIZE)
                        * UNIT_SIZE;
    }


    // Draw game
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (running) {

            // Draw food
            g.setColor(Color.RED);
            g.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);


            // Draw snake
            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {

                    // Snake head
                    g.setColor(Color.GREEN);

                } else {

                    // Snake body
                    g.setColor(new Color(0, 180, 0));
                }

                g.fillRect(
                        x[i],
                        y[i],
                        UNIT_SIZE,
                        UNIT_SIZE
                );
            }


            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));

            g.drawString(
                    "Score: " + score,
                    10,
                    25
            );

        } else {

            gameOverScreen(g);
        }
    }


    // Game loop
    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();

            checkFood();

            checkCollisions();

            repaint();
        }
    }


    // Move snake
    public void move() {

        // Move body
        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }


        // Move head
        if (direction == 'R') {

            x[0] = x[0] + UNIT_SIZE;
        }

        if (direction == 'L') {

            x[0] = x[0] - UNIT_SIZE;
        }

        if (direction == 'U') {

            y[0] = y[0] - UNIT_SIZE;
        }

        if (direction == 'D') {

            y[0] = y[0] + UNIT_SIZE;
        }
    }


    // Check if snake ate food
    public void checkFood() {

        if (x[0] == foodX && y[0] == foodY) {

            bodyParts++;

            score++;

            newFood();
        }
    }


    // Check collisions
    public void checkCollisions() {

        // Snake hits its body
        for (int i = bodyParts; i > 0; i--) {

            if (x[0] == x[i] && y[0] == y[i]) {

                running = false;
            }
        }


        // Snake hits left wall
        if (x[0] < 0) {

            running = false;
        }


        // Snake hits right wall
        if (x[0] >= SCREEN_WIDTH) {

            running = false;
        }


        // Snake hits top wall
        if (y[0] < 0) {

            running = false;
        }


        // Snake hits bottom wall
        if (y[0] >= SCREEN_HEIGHT) {

            running = false;
        }


        // Stop timer
        if (!running) {

            timer.stop();
        }
    }


    // Game over screen
    public void gameOverScreen(Graphics g) {

        g.setColor(Color.RED);

        g.setFont(new Font("Arial", Font.BOLD, 40));

        String message = "GAME OVER";

        int messageWidth = g.getFontMetrics().stringWidth(message);

        g.drawString(
                message,
                (SCREEN_WIDTH - messageWidth) / 2,
                SCREEN_HEIGHT / 2
        );


        // Score
        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 20));

        String scoreText = "Score: " + score;

        int scoreWidth = g.getFontMetrics().stringWidth(scoreText);

        g.drawString(
                scoreText,
                (SCREEN_WIDTH - scoreWidth) / 2,
                SCREEN_HEIGHT / 2 + 40
        );


        // Restart message
        g.setFont(new Font("Arial", Font.PLAIN, 18));

        String restartText = "Press ENTER to restart";

        int restartWidth = g.getFontMetrics().stringWidth(restartText);

        g.drawString(
                restartText,
                (SCREEN_WIDTH - restartWidth) / 2,
                SCREEN_HEIGHT / 2 + 80
        );
    }


    // Keyboard controls
    @Override
    public void keyPressed(KeyEvent e) {

        // Move left
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {

            if (direction != 'R') {

                direction = 'L';
            }
        }


        // Move right
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {

            if (direction != 'L') {

                direction = 'R';
            }
        }


        // Move up
        if (e.getKeyCode() == KeyEvent.VK_UP) {

            if (direction != 'D') {

                direction = 'U';
            }
        }


        // Move down
        if (e.getKeyCode() == KeyEvent.VK_DOWN) {

            if (direction != 'U') {

                direction = 'D';
            }
        }


        // Restart game
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {

            if (!running) {

                startGame();

                requestFocusInWindow();

                repaint();
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {
    }


    @Override
    public void keyReleased(KeyEvent e) {
    }
}