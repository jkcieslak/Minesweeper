package com.jkcieslak.minesweeper;

public class Cell {
    private final int id;
    private final int x;
    private final int y;
    private int value;
    private boolean isBomb;
    private boolean isRevealed;
    private boolean isFlagged;
    private boolean isChordable;
    private boolean isClickable;
    private boolean isOnEdge;


    public Cell(int id, int x, int y){
        this.id = id;
        this.x = x;
        this.y = y;
        this.value = 0;
        this.isBomb = false;
        this.isRevealed = false;
        this.isChordable = false;
        this.isClickable = true;
        this.isOnEdge = false;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public boolean isBomb() {
        return isBomb;
    }

    public void setBomb(boolean bomb) {
        isBomb = bomb;
    }

    public boolean isRevealed() {
        return isRevealed;
    }

    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }

    public boolean isChordable() {
        return isChordable;
    }

    public void setChordable(boolean chordable) {
        isChordable = chordable;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setClickable(boolean clickable) {
        isClickable = clickable;
    }

    public boolean isOnEdge() {
        return isOnEdge;
    }

    public void setOnEdge(boolean onEdge) {
        isOnEdge = onEdge;
    }

    public int getId() {
        return id;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
}
