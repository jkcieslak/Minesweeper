package com.jkcieslak.minesweeper;

import java.util.ArrayList;
import java.util.Random;

public class Board {
    private int width;
    private int height;
    private int bombQuantity;
    private int seed;
    private Cell[][] cells;
    private int revealedCellsQuantity;
    private int nonBombCells;
    private boolean isWon;
    private boolean isLost;

    public Board(int width, int height, int bombQuantity, int seed){
        this.width = width;
        this.height = height;
        this.bombQuantity = bombQuantity;
        if(seed != 0)
            this.seed = seed;
        else{
            Random rand = new Random();
            this.seed = rand.nextInt();
        }
        this.cells = new Cell[height][width];
        for(int i = 0; i < height; ++i){      //constructing Cells 2D array
            for(int j = 0; j < width; ++j){
                cells[i][j] = new Cell(i*width + j, j, i);
                if(j == 0 || j == (width - 1) || i == 0 || i == (height - 1))
                    cells[i][j].setOnEdge(true);
            }
        }
        this.isWon = false;
        this.isLost = false;
        this.revealedCellsQuantity = 0;
        this.nonBombCells = width*height-bombQuantity;
        System.out.println("Total cells: " + width*height);
        System.out.println("Non bomb cells: " + nonBombCells);
        System.out.println("Bomb cells: " + bombQuantity);
        //populate Cells with bombs
        populateWithBombs();
        //set Cells with proper neighboring bomb quantities
        calculateCellValues();
    }
    public Board(GameSettings gameSettings){
        this(gameSettings.getWidth(), gameSettings.getHeight(),
                gameSettings.getBombQuantity(), gameSettings.getSeed());
    }
    private void populateWithBombs(){
        int bombsPlaced = 0;
        int idLimit = width*height;
        Random rand = new Random(seed);

        while (bombsPlaced < bombQuantity){
            //pick a random cell
            int id = rand.nextInt(idLimit);
            Cell chosenCell = findCellById(id);
            if(chosenCell.isBomb())
                continue;
            chosenCell.setBomb(true);
            bombsPlaced++;
        }
    }

    private Cell findCellById(int id){
        int row = id/width;
        int column = id - row*width;
        if(row >= height || row < 0 || column >= width || column < 0){
            System.out.println("Something went wrong in findCellById function.");
        }
        return cells[row][column];
    }
    public Cell findCellByCoordinates(int x, int y){
        if(x < 0 || x >= width || y < 0 || y >= height)
            return null;
        return cells[y][x];
    }

    private int getNeighboringBombsQuantity(int x, int y){
        return getNeighboringBombsQuantity(findCellByCoordinates(x, y));
    }

    private int getNeighboringBombsQuantity(Cell cell){
        int bombCounter = 0;
        for(int i = -1; i < 2; ++i){
            for(int j = -1; j < 2; ++j){
                Cell considered = findCellByCoordinates(cell.getX()+j,cell.getY()+i);
                if(considered == null || considered == cell)
                    continue;
                if(considered.isBomb()){
                    bombCounter++;
                }
            }
        }
        return bombCounter;
    }
    private int getCellValue(int x, int y){
        return findCellByCoordinates(x, y).getValue();
    }
    private int getCellValue(Cell cell){
        return cell.getValue();
    }

    private void calculateCellValues(){
        for(Cell[] cellRow: cells){
            for(Cell cell: cellRow){
                cell.setValue(getNeighboringBombsQuantity(cell));
            }
        }
    }

    //for debug purposes
    public void printToConsole(){
        for (Cell[] cellRow: cells) {
            for (Cell cell: cellRow) {
                if(cell.isBomb())
                    System.out.print("█");
                else
                    System.out.print(" ");
            }
            System.out.println();
        }
        for (Cell[] cellRow: cells) {
            for (Cell cell: cellRow) {
                System.out.print(cell.getValue());
            }
            System.out.println();
        }
    }
    public Cell[][] getCells() {
        return cells;
    }
    public int getWidth() {
        return width;
    }
    public int getHeight() {
        return height;
    }
    public int getBombQuantity() {
        return bombQuantity;
    }
    public Cell[] getBombCells(){
        int i = 0;
        Cell[] bombCells = new Cell[bombQuantity];
        for(Cell[] cellRow: cells){
            for(Cell cell: cellRow){
                if(cell.isBomb()) {
                    bombCells[i] = cell;
                    ++i;
                }
            }
        }
        return bombCells;
    }
    public ArrayList<Cell> getNeighboringCells(Cell cell){
        ArrayList<Cell> neighborCells = new ArrayList<>();
        for(int i = -1; i < 2; ++i){
            for(int j = -1; j < 2; ++j){
                Cell considered = findCellByCoordinates(cell.getX()+j,cell.getY()+i);
                if(considered == null || considered == cell)
                    continue;
                neighborCells.add(considered);
            }
        }
        return neighborCells;
    }
    public void revealCell(Cell cell){
        if(cell.isRevealed() || cell.isFlagged()){
            return;
        }
        cell.setRevealed(true);
        if(cell.isBomb()){
            setLost(true);
            Cell[] bombCells = getBombCells();
            for(Cell bombCell : bombCells){
                revealCell(bombCell);
            }
        }
        if(cell.getValue() == 0 && !cell.isBomb()){
            for(int i = -1; i < 2; ++i){
                for(int j = -1; j < 2; ++j){
                    Cell considered = findCellByCoordinates(cell.getX()+j,cell.getY()+i);
                    if(considered == null || considered == cell)
                        continue;
                    revealCell(considered);
                }
            }
        }
        revealedCellsQuantity++;
        if(revealedCellsQuantity == nonBombCells)
            setWon(true);
    }

    public boolean isLost() {
        return isLost;
    }
    public void setLost(boolean lost) {
        isLost = lost;
    }
    public boolean isWon() {
        return isWon;
    }
    public void setWon(boolean won) {
        isWon = won;
    }

    public GameSettings getGameSettings(){
        return new GameSettings(width, height, bombQuantity, seed);
    }

    public int getRevealedCellsQuantity() {
        return revealedCellsQuantity;
    }

    public double getCompletionPercentage(){
        return (double)revealedCellsQuantity/nonBombCells;
    }
    public int countHiddenCellsAround(Cell cell){
        int hiddenCells = 0;
        ArrayList<Cell> neighboringCells = getNeighboringCells(cell);
        for(Cell neighboringCell : neighboringCells){
            if(!neighboringCell.isRevealed())
                hiddenCells++;
        }
        return hiddenCells;
    }
    public int countFlaggedCellsAround(Cell cell){
        int flaggedCells = 0;
        ArrayList<Cell> neighboringCells = getNeighboringCells(cell);
        for(Cell neighboringCell : neighboringCells){
            if(neighboringCell.isFlagged())
                flaggedCells++;
        }
        return flaggedCells;
    }
    public void flagHiddenCellsAround(Cell cell){
        ArrayList<Cell> neighboringCells = getNeighboringCells(cell);
        for(Cell neighboringCell : neighboringCells){
            if(!neighboringCell.isRevealed())
                neighboringCell.setFlagged(true);
        }
    }

    public void revealHiddenCellsAround(Cell cell){
        ArrayList<Cell> neighboringCells = getNeighboringCells(cell);
        for(Cell neighboringCell : neighboringCells){
            if(!neighboringCell.isRevealed() && !neighboringCell.isFlagged())
                revealCell(neighboringCell);
        }
    }
}
