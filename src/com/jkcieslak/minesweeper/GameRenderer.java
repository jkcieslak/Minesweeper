package com.jkcieslak.minesweeper;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class GameRenderer extends JFrame implements ActionListener, MouseListener {
    private Board board;
    private final BoardPanel boardPanel;
    private BufferedImage[] numericCellTextures;
    private BufferedImage bombCellTexture;
    private BufferedImage flaggedCellTexture;
    private BufferedImage hiddenCellTexture;
    private final GameSettings gameSettings;
    public final int SCALE = 32;
    public GameRenderer(){
        super("Minesweeper");
        this.gameSettings = new GameSettings();
        this.board = new Board(gameSettings);

        JMenuBar menuBar = new JMenuBar();

        JMenu gameMenu = new JMenu("Game");

        JMenuItem newGameMenuItem = new JMenuItem("New game");
        newGameMenuItem.addActionListener(this);
        newGameMenuItem.setActionCommand("startNewGame");

        JMenuItem settingsMenuItem = new JMenuItem("Settings");
        settingsMenuItem.addActionListener(this);
        settingsMenuItem.setActionCommand("openSettings");

        JMenuItem quitMenuItem = new JMenuItem("Quit");
        quitMenuItem.addActionListener(this);
        quitMenuItem.setActionCommand("quitGame");

        gameMenu.add(newGameMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(settingsMenuItem);
        gameMenu.addSeparator();
        gameMenu.add(quitMenuItem);

        menuBar.add(gameMenu);

        JMenu solverMenu = new JMenu("Auto-solve");

        JMenuItem solveTrivialMenuItem = new JMenuItem("Solve trivial cells");
        solveTrivialMenuItem.addActionListener(this);
        solveTrivialMenuItem.setActionCommand("solveTrivial");

        solverMenu.add(solveTrivialMenuItem);
        solverMenu.addSeparator();

        menuBar.add(solverMenu);

        setJMenuBar(menuBar);

        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(board.getWidth()*SCALE+getInsets().left+getInsets().right,
                board.getHeight()*SCALE+getInsets().top+getInsets().bottom + menuBar.getHeight());
        addMouseListener(this);
        loadAllTextures();
        boardPanel = new BoardPanel();
        boardPanel.setBounds(0, menuBar.getHeight(), board.getWidth()*SCALE, board.getHeight()*SCALE + menuBar.getHeight());
        //rootPane.add(boardPanel);
        setContentPane(boardPanel);
        setVisible(true);
        System.out.println("Board panel dimensions: " + boardPanel.getWidth() + ", " + boardPanel.getHeight());
        System.out.println("Window dimensions: " + this.getWidth() + ", " + this.getHeight());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("startNewGame")){
            board = new Board(gameSettings);
            this.setSize(board.getWidth()*SCALE+getInsets().left+getInsets().right,
                    board.getHeight()*SCALE+getInsets().top+getInsets().bottom + this.getJMenuBar().getHeight());
            boardPanel.image = new BufferedImage(board.getWidth()*SCALE,
                    board.getHeight()*SCALE, BufferedImage.TYPE_INT_ARGB);
            boardPanel.setBounds(0, this.getJMenuBar().getHeight(), board.getWidth()*SCALE,
                    board.getHeight()*SCALE + this.getJMenuBar().getHeight());
            boardPanel.renderBoard();
            this.repaint();
            this.setVisible(true);
        }else if(e.getActionCommand().equals("openSettings")){
            SettingsWindow settingsWindow = new SettingsWindow(gameSettings, this);
        }else if(e.getActionCommand().equals("quitGame")){
            this.dispose();
        }else if(e.getActionCommand().equals("solveTrivial")) {
            for(int i = 0; i < 50; ++i){
                for (Cell[] cellRow : board.getCells()) {
                    for (Cell cell : cellRow) {
                        if (cell.isRevealed() && board.countFlaggedCellsAround(cell) == cell.getValue()) {
                            board.revealHiddenCellsAround(cell);
                        } else if (cell.isRevealed() && board.countHiddenCellsAround(cell) == cell.getValue()) {
                            board.flagHiddenCellsAround(cell);
                        }

                    }
                }
            }
            boardPanel.renderBoard();
            repaint();
            System.out.println("Board completed in " + board.getCompletionPercentage()*100 +"%.");
            if(board.isLost()) {
                new FinishBox(false, board, gameSettings, this);
                board.setWon(false);
            }
            if(board.isWon())
                new FinishBox(true, board, gameSettings, this);
        }
    }

    public void loadAllTextures(){
        bombCellTexture = loadTexture("bomb.png");
        flaggedCellTexture = loadTexture("flag.png");
        hiddenCellTexture = loadTexture("hidden.png");
        numericCellTextures = new BufferedImage[9];
        for(int i = 0; i < 9; ++i){
            numericCellTextures[i] = loadTexture(i + ".png");
        }
    }

    public BufferedImage loadTexture(String path){
        BufferedImage loadedTexture;
        BufferedImage texture;
        try{
            loadedTexture = ImageIO.read(getClass().getClassLoader().getResource(path));
            texture = new BufferedImage(SCALE, SCALE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D textureGraphics = texture.createGraphics();
            textureGraphics.drawImage(loadedTexture.getScaledInstance(SCALE, SCALE, Image.SCALE_DEFAULT), null, null);
            //texture = ImageIO.read(new File(path));
        } catch (IOException | IllegalArgumentException e) {
            texture = new BufferedImage(SCALE, SCALE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D TextureGraphics = texture.createGraphics();
            TextureGraphics.setColor(Color.MAGENTA);
            TextureGraphics.fillRect(0, 0, SCALE/2, SCALE/2);
            TextureGraphics.fillRect(SCALE/2, SCALE/2, SCALE/2, SCALE/2);
            TextureGraphics.setColor(Color.BLACK);
            TextureGraphics.fillRect(0, SCALE/2, SCALE/2, SCALE/2);
            TextureGraphics.fillRect(SCALE/2, 0, SCALE/2, SCALE/2);
        }
        return texture;
    }

    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(board.isLost())
            return;
        int clickX = e.getX() - this.getInsets().left;
        int clickY = e.getY() - this.getJMenuBar().getHeight() - this.getInsets().top;
        Cell cellClicked = board.findCellByCoordinates(clickX/SCALE, clickY/SCALE);
        System.out.println("Clicked on coordinates: ["+clickX +", "+clickY+"]");
        System.out.println("Corresponding cell: ["+cellClicked.getX()+", "+cellClicked.getY()+"]");
        if(e.getButton() == MouseEvent.BUTTON3){
            if(!cellClicked.isRevealed())
                cellClicked.setFlagged(!cellClicked.isFlagged());
            else{   //chording functionality for rmb click
                System.out.println("Hidden cells around: " + board.countHiddenCellsAround(cellClicked));
                System.out.println("Flagged cells around: " + board.countFlaggedCellsAround(cellClicked));
                System.out.println("Cell value: " + cellClicked.getValue());
                if(board.countHiddenCellsAround(cellClicked) == cellClicked.getValue())
                    board.flagHiddenCellsAround(cellClicked);
            }
        }else {
            if(!cellClicked.isRevealed())
                board.revealCell(cellClicked);
            else {
                if(board.countFlaggedCellsAround(cellClicked) == cellClicked.getValue()){
                    board.revealHiddenCellsAround(cellClicked);
                }
            }
        }
        boardPanel.renderBoard();
        repaint();
        System.out.println("Board completed in " + board.getCompletionPercentage()*100 +"%.");
        if(board.isLost()) {
            new FinishBox(false, board, gameSettings, this);
            board.setWon(false);
        }
        if(board.isWon())
            new FinishBox(true, board, gameSettings, this);
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public void renderBoard(){
        this.boardPanel.renderBoard();
    }

    public BoardPanel getBoardPanel() {
        return boardPanel;
    }

    class BoardPanel extends JComponent{
        private BufferedImage image;

        public BoardPanel(){
            this.image = new BufferedImage(board.getWidth()*SCALE, board.getHeight()*SCALE, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics = image.createGraphics();
            for(Cell[] cellRow : board.getCells()){
                for(Cell cell : cellRow){
                    graphics.drawImage(hiddenCellTexture, null, cell.getX()*SCALE, cell.getY()*SCALE);
                }
            }
        }
        @Override
        protected void paintComponent(Graphics g){
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this);
        }
        void renderBoard(){
            Graphics2D graphics = (Graphics2D)this.image.createGraphics();
            for(Cell[] cellRow : board.getCells()){
                for(Cell cell : cellRow){
                    if(!cell.isRevealed()) {
                        if(cell.isFlagged()){
                            graphics.drawImage(flaggedCellTexture, null, cell.getX() * SCALE, cell.getY() * SCALE);
                            continue;
                        }
                        graphics.drawImage(hiddenCellTexture, null, cell.getX() * SCALE, cell.getY() * SCALE);
                        continue;
                    }
                    if(cell.isBomb()) {
                        graphics.drawImage(bombCellTexture, null, cell.getX() * SCALE, cell.getY() * SCALE);
                        continue;
                    }
                    graphics.drawImage(numericCellTextures[cell.getValue()], null, cell.getX() * SCALE, cell.getY() * SCALE);
                }
            }
        }
        public BufferedImage getImage(){
            return this.image;
        }
        public void setImage(BufferedImage image){
            this.image = image;
        }
    }
}
