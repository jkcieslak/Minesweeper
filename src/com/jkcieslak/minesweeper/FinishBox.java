package com.jkcieslak.minesweeper;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;

public class FinishBox extends JFrame implements ActionListener {
    GameRenderer parentComponent;
    Board board;
    GameSettings gameSettings;
    public FinishBox(boolean isWon, Board board, GameSettings gameSettings, GameRenderer parentComponent){
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(355, 130);
        setLayout(null);
        setResizable(false);
        this.board = board;
        this.gameSettings = gameSettings;
        this.parentComponent = parentComponent;

        JLabel resultLabel = new JLabel();
        resultLabel.setBounds(0, 10, 330, 20);
        if(isWon){
            resultLabel.setText("Congratulations, you've won!");
        }else{
            resultLabel.setText("You've lost. Better luck next time!");
        }
        resultLabel.setHorizontalAlignment(SwingConstants.CENTER);
        this.add(resultLabel);

        JButton newGameButton = new JButton("New game");
        newGameButton.setBounds(10, 50, 150, 30);
        newGameButton.addActionListener(this);
        newGameButton.setActionCommand("startNewGame");
        add(newGameButton);

        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(170, 50, 150, 30);
        exitButton.addActionListener(this);
        exitButton.setActionCommand("exitGame");
        add(exitButton);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("startNewGame")){
            parentComponent.actionPerformed(new ActionEvent(parentComponent, 0, "startNewGame"));

            this.dispose();
        }
        if(e.getActionCommand().equals("exitGame"))
            System.exit(0);
    }
}
