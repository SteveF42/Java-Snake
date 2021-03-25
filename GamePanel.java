import javax.swing.*;
import javax.swing.text.StyledEditorKit.FontFamilyAction;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;


public class GamePanel extends JPanel implements ActionListener {
    final int SCREEN_WIDTH = 900;
    final int SCREEN_HEIGHT = 900;
    final int BOX_SIZE = 25;
    final Color backGround = new Color(0, 0, 0);
    final int DELAY = 75;
    boolean running = false;
    char moveState = ' ';
    char[] inputQueue;

    // snake stuff
    int[] snakeBodyRow;
    int[] snakeBodyCol;
    int snakeLength = 1;

    // fruit location
    int fruitRow;
    int fruitCol;

    // other stuff
    Timer timer;
    static final long serialVersionUID = 1;
    Random rand;

    public GamePanel() {
        rand = new Random();

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

        if (this.moveState == 'U') {
            this.snakeBodyCol[0]--;
        } else if (this.moveState == 'D') {
            this.snakeBodyCol[0]++;
        } else if (this.moveState == 'L') {
            this.snakeBodyRow[0]--;
        } else if (this.moveState == 'R') {
            this.snakeBodyRow[0]++;
        }
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

    public void updateMove(char move) {
        if (running == false && move == ' ') {
            restartGame();
        }

        if (move == 'w' && moveState != 'D') {
            moveState = 'U';
        } else if (move == 's' && moveState != 'U') {
            moveState = 'D';
        } else if (move == 'a' && moveState != 'R') {
            moveState = 'L';
        } else if (move == 'd' && moveState != 'L') {
            moveState = 'R';
        }
    }

    class EventListener extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            updateMove(e.getKeyChar());
        }
    }
}