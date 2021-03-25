import javax.swing.*;

public class GameFrame extends JFrame{
    static final long serialVersionUID = 1;

    GameFrame(){
        this.add(new GamePanel());   
        this.setVisible(true);
        this.setTitle("Java-Snake");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }
    
}
