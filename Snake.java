import java.util.Random;
import java.util.*;

public class Snake {
    private int posCol;
    private int posRow;

    private int snakeSize;
    public int[] previousCol;
    public int[] previousRow;

    public Snake(int row, int col){
        this.posCol = col;
        this.posRow = row;
        this.snakeSize = 1;
        previousCol = new int[100];
        previousRow = new int[100];
    }

    public void setSnakePos(int row, int col){
        this.posCol=col;
        this.posRow=row;
    }

    public void updateSnakePos(int row, int col){
        this.posCol += col;
        this.posRow += row;
    }
    public void updateSnakeSize(){
        this.snakeSize++;
    }

    public int[] getSnakePos(){
        return new int[]{this.posRow,this.posCol};
    }
    public int getSnakeSize(){
        return this.snakeSize;
    }
}
