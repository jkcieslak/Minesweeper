package com.jkcieslak.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SettingsWindow extends JFrame implements ActionListener {
    GameRenderer parentComponent;
    GameSettings gameSettings;
    JSpinner widthSpinner;
    JSpinner heightSpinner;
    JSpinner bombQuantitySpinner;
    JSpinner seedSpinner;

    public SettingsWindow(GameSettings gameSettings, GameRenderer parentComponent){
        super("Settings");
        this.parentComponent = parentComponent;
        this.gameSettings = gameSettings;
        this.setLayout(new GridLayout(5, 2, 10, 10));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JLabel widthLabel = new JLabel("Width:", JLabel.RIGHT);
        this.add(widthLabel);

        widthSpinner = new JSpinner(new SpinnerNumberModel(GameSettings.getDefaultSettings().getWidth(),
                GameSettings.MINIMUM_WIDTH, GameSettings.MAXIMUM_WIDTH, 1));
        if((gameSettings.getWidth() >= GameSettings.MINIMUM_WIDTH)
                && (gameSettings.getWidth() <= GameSettings.MAXIMUM_WIDTH))
            widthSpinner.setValue(gameSettings.getWidth());
        this.add(widthSpinner);

        JLabel heightLabel = new JLabel("Height:", JLabel.RIGHT);
        this.add(heightLabel);

        heightSpinner = new JSpinner(new SpinnerNumberModel(GameSettings.getDefaultSettings().getHeight(),
                GameSettings.MINIMUM_HEIGHT, GameSettings.MAXIMUM_HEIGHT, 1));
        if((gameSettings.getHeight() >= GameSettings.MINIMUM_HEIGHT)
                && (gameSettings.getHeight() <= GameSettings.MAXIMUM_HEIGHT))
            heightSpinner.setValue(gameSettings.getHeight());
        this.add(heightSpinner);

        JLabel bombQuantityLabel = new JLabel("Bomb quantity:", JLabel.RIGHT);
        this.add(bombQuantityLabel);

        bombQuantitySpinner = new JSpinner(new SpinnerNumberModel(GameSettings.getDefaultSettings().getBombQuantity(),
                GameSettings.MINIMUM_BOMB_QUANTITY, GameSettings.MAXIMUM_HEIGHT * GameSettings.MAXIMUM_WIDTH - 1,
                1));
        if((gameSettings.getBombQuantity() >= GameSettings.MINIMUM_BOMB_QUANTITY) &&
                gameSettings.getBombQuantity() <= GameSettings.MAXIMUM_WIDTH * GameSettings.MAXIMUM_HEIGHT - 1)
            bombQuantitySpinner.setValue(gameSettings.getBombQuantity());
        this.add(bombQuantitySpinner);

        JLabel seedLabel = new JLabel("Seed:", JLabel.RIGHT);
        this.add(seedLabel);

        seedSpinner = new JSpinner(new SpinnerNumberModel(GameSettings.getDefaultSettings().getSeed(),
                Integer.MIN_VALUE, Integer.MAX_VALUE, 1));
        seedSpinner.setValue(gameSettings.getSeed());
        this.add(seedSpinner);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(this);
        okButton.setActionCommand("applyConfig");
        this.add(okButton);

        JButton cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(this);
        cancelButton.setActionCommand("cancelConfig");
        this.add(cancelButton);

        this.setSize(400, 400);
        this.setResizable(false);
        this.setAlwaysOnTop(true);
        this.setAutoRequestFocus(true);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("applyConfig")){
            this.gameSettings.setWidth((int)widthSpinner.getValue());
            this.gameSettings.setHeight((int)heightSpinner.getValue());
            this.gameSettings.setBombQuantity((int)bombQuantitySpinner.getValue());
            this.gameSettings.setSeed((int)seedSpinner.getValue());
            parentComponent.actionPerformed(new ActionEvent(parentComponent, 0, "startNewGame"));
            this.dispose();
        }else if(e.getActionCommand().equals("cancelConfig")) {
            this.dispose();
        }
    }
}
