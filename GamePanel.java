import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Queue;

public class GamePanel extends JPanel implements ActionListener {
    final int SCREEN_WIDTH = 900;
    final int SCREEN_HEIGHT = 900;
    final int BOX_SIZE = 25;
    final Color backGround = new Color(0, 0, 0);
    final int DELAY = 75;
    boolean running = false;
    char moveState = ' ';
    ArrayList<Character> inputQueue;

    // snake stuff
    int[] snakeBodyRow;
    int[] snakeBodyCol;
    int snakeLength = 1;
    int colVelocity = 0;
    int rowVelocity = 0;

    // fruit location
    int fruitRow;
    int fruitCol;

    // other stuff
    Timer timer;
    static final long serialVersionUID = 1;
    Random rand;

    public GamePanel() {
        rand = new Random();
        inputQueue = new ArrayList<>();

        // initializes the panel
        this.setPreferredSize(new Dimension(this.SCREEN_WIDTH, this.SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(new EventListener());

        // initializes snake body
        this.snakeBodyRow = new int[(this.SCREEN_HEIGHT / this.BOX_SIZE) * BOX_SIZE];
        this.snakeBodyCol = new int[(this.SCREEN_WIDTH / this.BOX_SIZE) * BOX_SIZE];

        // spawns snake in random location
        randomSnake();

        // spawns fruit in random location
        startGame();
        newApple();
    }

    public void randomSnake() {
        int[] snakeStart = getRandomCords();
        snakeBodyRow[0] = snakeStart[0];
        snakeBodyCol[0] = snakeStart[1];
    }

    public void restartGame() {
        newApple();
        randomSnake();
        snakeLength = 1;
        running = true;

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (this.running) {
            move();
            checkGameOver();
            eatApple();
        }
        nextMoveInQueue();
        repaint();
    }

    public void startGame() {
        running = true;
        this.timer = new Timer(this.DELAY, this);
        this.timer.start();
    }

    public void newApple() {
        int[] fruitStart = getRandomCords();
        this.fruitRow = fruitStart[0];
        this.fruitCol = fruitStart[1];
    }

    public void move() {
        for (int i = this.snakeLength; i > 0; i--) {
            this.snakeBodyCol[i] = this.snakeBodyCol[i - 1];
            this.snakeBodyRow[i] = this.snakeBodyRow[i - 1];
        }
        this.snakeBodyCol[0]+= colVelocity;
        this.snakeBodyRow[0]+= rowVelocity;
        
    }

    public void eatApple() {
        // checks if the head is currently eating the apple
        if (this.snakeBodyRow[0] == fruitRow && this.snakeBodyCol[0] == fruitCol) {
            newApple();
            snakeLength++;
        }
    }

    public void checkGameOver() {
        int row = this.snakeBodyRow[0];
        int col = this.snakeBodyCol[0];

        for (int i = 1; i < snakeLength; i++) {
            if (row == this.snakeBodyRow[i] && col == this.snakeBodyCol[i]) {
                this.running = false;
            }
        }
        if (row >= SCREEN_HEIGHT / BOX_SIZE || row < 0) {
            running = false;
        } else if (col >= SCREEN_WIDTH / BOX_SIZE || col < 0) {
            running = false;
        }
    }

    public void paint(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        Graphics2D g2D = (Graphics2D) g;
        g2D.setStroke(new BasicStroke(1));
        g2D.setColor(Color.white);

        // draws grid
        // for (int i = 1; i < SCREEN_HEIGHT / BOX_SIZE; i++) {
        // g.drawLine(0, i * BOX_SIZE, SCREEN_WIDTH, i * BOX_SIZE);
        // g.drawLine(i * BOX_SIZE, 0, i * BOX_SIZE, SCREEN_HEIGHT);
        // }
        g.setFont(new Font("Font Awesome",Font.BOLD,20));
        if (running == false) {
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Game Over", (this.SCREEN_WIDTH - metrics.stringWidth("Game OVer"))/2, this.SCREEN_HEIGHT / 2);
            g.drawString("Press Space to Restart", ((this.SCREEN_WIDTH - metrics.stringWidth("Press Space to Restart")) / 2), (this.SCREEN_HEIGHT / 2) + 20);
        }

        FontMetrics metrics = getFontMetrics((g.getFont()));
        g.drawString("Score: " + (this.snakeLength-1), (this.SCREEN_WIDTH-metrics.stringWidth("Score: " + snakeLength) ) / 2, 25);
        // draws snakeBody
        for (int i = 0; i < this.snakeLength; i++) {
            int row = this.snakeBodyRow[i] * BOX_SIZE;
            int col = this.snakeBodyCol[i] * BOX_SIZE;

            g.setColor(Color.green);
            g.fillRect(row, col, this.BOX_SIZE, this.BOX_SIZE);
        }

        // draws fruit
        g.setColor(Color.red);
        g.fillOval(fruitRow * this.BOX_SIZE, fruitCol * this.BOX_SIZE, 25, 25);
    }

    int[] getRandomCords() {
        int range = SCREEN_HEIGHT / BOX_SIZE;

        int row = rand.nextInt(range);
        int col = rand.nextInt(range);
        return new int[] { row, col };
    }

    public void nextMoveInQueue() {
        if(inputQueue.isEmpty()) return;

        char move = inputQueue.get(0);
        inputQueue.remove(0);
        if (running == false && move == ' ') {
            restartGame();
        }

        if (move == 'w' && moveState != 's') {
            colVelocity = -1;
            rowVelocity = 0;
            moveState = 'w';
        } else if (move == 's' && moveState != 'w') {
            rowVelocity = 0;
            colVelocity = 1;
            moveState = 's';
        } else if (move == 'a' && moveState != 'd') {
            rowVelocity = -1;
            colVelocity = 0;
            moveState = 'a';
        } else if (move == 'd' && moveState != 'a') {
            colVelocity = 0;
            rowVelocity = 1;
            moveState = 'd';
        }
    }

    class EventListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            if(moveState != e.getKeyCode()){
                inputQueue.add(e.getKeyChar());
            }
        }
    }
}