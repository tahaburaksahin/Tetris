package main;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Tetris");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        GamePanel gamePanel = new GamePanel();
        window.getContentPane().add(gamePanel); // Add the GamePanel to the JFrame's content pane

        window.pack(); // Pack the components to set the size of the JFrame
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.launchGame(); // Start the game loop
    }
}
