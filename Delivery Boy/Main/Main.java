package Main;

import javax.swing.JFrame;

public class Main {
    
    public static void main(String[] args) {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Delivery Boy");
        
        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);

        window.pack(); //Window will be sized to fit the laytout of its components (=GamePanel)

        window.setLocationRelativeTo(null);
        window.setVisible(true);
    
        gamePanel.gameSetUp();
        gamePanel.startGameThread();
    
    }
}
