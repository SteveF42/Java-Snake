import java.util.*;

import javax.swing.JFrame;
import javax.swing.JTextField;

import java.io.IOException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Main {

    private static Board game;

    public static void main(String[] args) throws IOException, InterruptedException {

        boolean gameOver = false;
        game = new Board(20, 20);
        JTextField component = new JTextField();
        component.addKeyListener(new MyKeyListener());

        JFrame f = new JFrame();
        f.add(component);
        f.setSize(300,300);
        f.setVisible(true);

        while (!gameOver) {
            clear();
            game.update();
            gameOver = game.checkGameOver();
            Thread.sleep(100);
        }
    }
    public static void clear() throws IOException, InterruptedException {
        new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
    }
    public static class MyKeyListener extends KeyAdapter {
        public void keyPressed(KeyEvent evt) {
            if (evt.getKeyChar() == 'w') {
                game.moveUp();
            }else if (evt.getKeyChar() == 'a'){
                game.moveLeft();
            }else if(evt.getKeyChar() == 's'){
                game.moveDown();
            }else if(evt.getKeyChar() == 'd'){
                game.moveRight();
            }
        }
    }
}
