import java.util.Random;

public class Board {
    private int rowSize;
    private int colSize;

    private int fruitRow;
    private int fruitCol;

    private Snake snake;

    private enum State {
        UP, DOWN, LEFT, RIGHT, DEFAULT
    };

    private State moveState;

    private int score;

    public Board(int x, int y) {
        // initialize board on spawn
        // create new locations for apple
        // move snake

        // initializes the board size
        this.rowSize = x;
        this.colSize = y;
        this.moveState = State.DEFAULT;

        // initializes the fruit coordinates
        int[] coordinates = getRandomLocation();
        this.fruitRow = coordinates[0];
        this.fruitCol = coordinates[1];

        // initializes the snake coordinates
        coordinates = getRandomLocation();
        this.snake = new Snake(coordinates[0], coordinates[1]);

        this.score = 0;
        // draws first frame
        draw();
    }

    // creates the play area
    private void draw() {
        System.out.println("Score: " + this.score);
        //draws the board
        for (int row = 0; row <= rowSize; row++) {
            for (int col = 0; col <= colSize; col++) {
                // draws the board
                if (row == 0 || row == rowSize) {
                    System.out.print('#');
                } else if (col == 0 || col == colSize) {
                    System.out.print('#');
                } else {

                    // checks the snakes coordinates
                    // if it is the current snakes location then print big O else little o
                    int[] snakeLocation = this.snake.getSnakePos();
                    boolean print = false;
                    for (int i = 1; i < snake.getSnakeSize(); ++i) {
                        if (snake.previousCol[i] == col && snake.previousRow[i] == row) {
                            print = true;
                            System.out.print('o');
                        }
                    }
                    if (snakeLocation[0] == row && snakeLocation[1] == col) {
                        // saves the body of the snake
                        System.out.print('O');
                        // checks the current fruit location
                    } else if (fruitRow == row && fruitCol == col) {
                        System.out.print('f');
                    } else {
                        if (!print)
                            System.out.print(' ');
                    }
                }
            }
            System.out.println();
        }
    }

    private void saveSnakeBody() {
        int[] currentSnakePos = snake.getSnakePos();

        // gets the head of the snake
        int previousCol = snake.previousCol[0];
        int previousRow = snake.previousRow[0];

        snake.previousCol[0] = currentSnakePos[1];
        snake.previousRow[0] = currentSnakePos[0];

        // saves the body of the snake
        for (int i = 1; i < snake.getSnakeSize(); i++) {
            int tempCol = snake.previousCol[i];
            int tempRow = snake.previousRow[i];
            snake.previousCol[i] = previousCol;
            snake.previousRow[i] = previousRow;
            previousCol = tempCol;
            previousRow = tempRow;
        }

    }

    private void updateScore() {
        // checks if snake is over a fruit
        int[] snakeLocation = snake.getSnakePos();

        if (snakeLocation[0] == fruitRow && snakeLocation[1] == fruitCol) {
            score++;
            snake.updateSnakeSize();
            int[] randLocations = getRandomLocation();
            fruitRow = randLocations[0];
            fruitCol = randLocations[1];
        }
    }
    private void handleOutOfBounds(){
        int[] snakeLocation = snake.getSnakePos();
        if (snakeLocation[0] >= rowSize - 1)
            snake.setSnakePos(0, snakeLocation[1]);
        if (snakeLocation[0] <= 0)
            snake.setSnakePos(colSize - 1, snakeLocation[1]);
        if (snakeLocation[1] >= colSize - 1)
            snake.setSnakePos(snakeLocation[0], 0);
        if (snakeLocation[1] <= 0)
            snake.setSnakePos(snakeLocation[0], rowSize - 1);
    }

    //handles the move state by moving the snake
    private void handleMoveState(){
        if (this.moveState == State.UP) {
            snake.updateSnakePos(-1, 0);
        } else if (this.moveState == State.DOWN) {
            snake.updateSnakePos(1, 0);

        } else if (this.moveState == State.LEFT) {
            snake.updateSnakePos(0, -1);

        } else if (this.moveState == State.RIGHT) {
            snake.updateSnakePos(0, 1);
        }
    }

    //checks if the snakes head is touching any of its body
    public boolean checkGameOver() {
        int[] snakePos = snake.getSnakePos();
        for (int i = 1; i < snake.getSnakeSize(); i++) {
            if (snakePos[0] == snake.previousRow[i] && snakePos[1] == snake.previousCol[i]) {
                return true;
            }
        }
        return false;
    }

    // main update function that save sthe snake body, draws the board, handles when snake goes out of bounds, and moves the snake
    public void update() {
        saveSnakeBody();
        draw();
        handleOutOfBounds();
        handleMoveState();
        updateScore();
    }

    //sets the state for UP,DOWN,LEFT,RIGHT
    public void moveUp() {
        if (this.moveState != State.DOWN) {
            this.moveState = State.UP;
        }
    }

    public void moveDown() {
        if (this.moveState != State.UP) {
            this.moveState = State.DOWN;
        }
    }

    public void moveLeft() {
        if (this.moveState != State.RIGHT) {
            this.moveState = State.LEFT;
        }
    }

    public void moveRight() {
        if (this.moveState != State.LEFT) {
            this.moveState = State.RIGHT;
        }
    }

    //helper function that returns a random location
    private int[] getRandomLocation() {
        Random rand = new Random();

        int[] set = new int[2];
        set[0] = rand.nextInt(this.rowSize - 1) + 1;
        set[1] = rand.nextInt(this.colSize - 1) + 1;
        return set;
    }
}
