package com.jkcieslak.minesweeper;

import java.io.Serializable;

public class GameSettings implements Serializable {
    public static final int MINIMUM_WIDTH = 10;
    public static final int MAXIMUM_WIDTH = 100;
    public static final int MINIMUM_HEIGHT = 10;
    public static final int MAXIMUM_HEIGHT = 50;
    public static final int MINIMUM_BOMB_QUANTITY = 10;
    private int width;
    private int height;
    private int bombQuantity;
    private int seed;

    public GameSettings(){
        setDefaultValues();
    }
    public GameSettings(int width, int height, int bombQuantity, int seed){
        this.width = width;
        this.height = height;
        this.bombQuantity = bombQuantity;
        this.seed = seed;
    }

    public void setDefaultValues(){ //default values, corresponding to ms minesweeper expert settings
        this.width = 30;
        this.height = 16;
        this.bombQuantity = 99;
        this.seed = 0;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getBombQuantity() {
        return bombQuantity;
    }

    public void setBombQuantity(int bombQuantity) {
        this.bombQuantity = bombQuantity;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public static GameSettings getDefaultSettings(){
        return new GameSettings(30, 16, 99 ,0);
    }
}
