package tetris;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Main class that starts the Tetris game application
 */
public class TetrisGame {
    public static void main(String[] args) {
        // Use SwingUtilities to ensure UI is created on the Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            // Create and display the main game frame
            TetrisFrame frame = new TetrisFrame();
            frame.setVisible(true);
        });
    }
}

/**
 * Main frame that contains all UI components of the game
 */
class TetrisFrame extends JFrame {
    private final GamePanel gamePanel;
    private final PreviewPanel previewPanel;
    private final JLabel scoreLabel;
    private final JLabel levelLabel;
    private final JButton startButton;
    private final JButton pauseButton;
    private final GameController gameController;
    
    /**
     * Constructor to initialize the main game frame
     */
    public TetrisFrame() {
        // Set up the frame properties
        setTitle("Tetris Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Initialize the game controller
        gameController = new GameController();
        
        // Set up the layout
        setLayout(new BorderLayout());
        
        // Create the game panel
        gamePanel = new GamePanel(gameController);
        add(gamePanel, BorderLayout.CENTER);
        
        // Create the side panel for preview, score, and controls
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Create the preview panel
        previewPanel = new PreviewPanel(gameController);
        previewPanel.setPreferredSize(new Dimension(150, 150));
        previewPanel.setBorder(BorderFactory.createTitledBorder("Next Block"));
        sidePanel.add(previewPanel);
        
        // Add some spacing
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create the score panel
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.Y_AXIS));
        scorePanel.setBorder(BorderFactory.createTitledBorder("Stats"));
        
        // Create score label
        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(scoreLabel);
        
        // Add some spacing
        scorePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        
        // Create level label
        levelLabel = new JLabel("Level: 1");
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.add(levelLabel);
        
        sidePanel.add(scorePanel);
        
        // Add some spacing
        sidePanel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // Create the control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(2, 1, 10, 10));
        controlPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
        
        // Create start button
        startButton = new JButton("Start");
        startButton.addActionListener(e -> {
            gameController.startGame();
            gamePanel.requestFocus(); // Focus on game panel for key events
        });
        controlPanel.add(startButton);
        
        // Create pause button
        pauseButton = new JButton("Pause");
        pauseButton.addActionListener(e -> {
            gameController.togglePause();
            gamePanel.requestFocus(); // Focus on game panel for key events
        });
        controlPanel.add(pauseButton);
        
        sidePanel.add(controlPanel);
        
        // Add the side panel to the frame
        add(sidePanel, BorderLayout.EAST);
        
        // Set up key bindings for game controls
        setupKeyBindings();
        
        // Register the frame as an observer of the game controller
        gameController.addObserver((score, level) -> {
            scoreLabel.setText("Score: " + score);
            levelLabel.setText("Level: " + level);
        });
    }
    
    /**
     * Set up key bindings for game controls
     */
    private void setupKeyBindings() {
        // Get the input map and action map of the game panel
        InputMap inputMap = gamePanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = gamePanel.getActionMap();
        
        // Set up left arrow key
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "moveLeft");
        actionMap.put("moveLeft", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.moveLeft();
                gamePanel.repaint();
            }
        });
        
        // Set up right arrow key
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "moveRight");
        actionMap.put("moveRight", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.moveRight();
                gamePanel.repaint();
            }
        });
        
        // Set up down arrow key
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "moveDown");
        actionMap.put("moveDown", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.moveDown();
                gamePanel.repaint();
            }
        });
        
        // Set up up arrow key for rotation
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "rotate");
        actionMap.put("rotate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.rotate();
                gamePanel.repaint();
            }
        });
        
        // Set up space key for hard drop
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "hardDrop");
        actionMap.put("hardDrop", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gameController.hardDrop();
                gamePanel.repaint();
            }
        });
    }
}

/**
 * Panel that displays the game board
 */
class GamePanel extends JPanel {
    private final GameController gameController;
    private static final int CELL_SIZE = 30;
    
    /**
     * Constructor to initialize the game panel
     * @param gameController The game controller
     */
    public GamePanel(GameController gameController) {
        this.gameController = gameController;
        setPreferredSize(new Dimension(
            GameController.BOARD_WIDTH * CELL_SIZE,
            GameController.BOARD_HEIGHT * CELL_SIZE
        ));
        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setFocusable(true);
    }
    
    /**
     * Paint the game board
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the background grid
        g.setColor(Color.LIGHT_GRAY);
        for (int row = 0; row < GameController.BOARD_HEIGHT; row++) {
            for (int col = 0; col < GameController.BOARD_WIDTH; col++) {
                g.drawRect(col * CELL_SIZE, row * CELL_SIZE, CELL_SIZE, CELL_SIZE);
            }
        }
        
        // Draw the placed blocks
        for (int row = 0; row < GameController.BOARD_HEIGHT; row++) {
            for (int col = 0; col < GameController.BOARD_WIDTH; col++) {
                if (gameController.getBoard()[row][col] != null) {
                    drawBlock(g, col, row, gameController.getBoard()[row][col]);
                }
            }
        }
        
        // Draw the current falling block
        Block currentBlock = gameController.getCurrentBlock();
        if (currentBlock != null) {
            int[][] shape = currentBlock.getShape();
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        drawBlock(g, currentBlock.getX() + j, currentBlock.getY() + i, currentBlock.getColor());
                    }
                }
            }
        }
        
        // Draw game over message if the game is over
        if (gameController.isGameOver()) {
            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            String gameOver = "GAME OVER";
            FontMetrics fm = g.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(gameOver)) / 2;
            int y = getHeight() / 2;
            g.drawString(gameOver, x, y);
        }
    }
    
    /**
     * Draw a block at the specified position with the given color
     * @param g Graphics context
     * @param x X position in grid coordinates
     * @param y Y position in grid coordinates
     * @param color Color of the block
     */
    private void drawBlock(Graphics g, int x, int y, Color color) {
        int pixelX = x * CELL_SIZE;
        int pixelY = y * CELL_SIZE;
        
        // Fill the block with the specified color
        g.setColor(color);
        g.fillRect(pixelX + 1, pixelY + 1, CELL_SIZE - 2, CELL_SIZE - 2);
        
        // Draw highlights for 3D effect
        g.setColor(color.brighter());
        g.drawLine(pixelX + 1, pixelY + 1, pixelX + CELL_SIZE - 2, pixelY + 1);
        g.drawLine(pixelX + 1, pixelY + 1, pixelX + 1, pixelY + CELL_SIZE - 2);
        
        // Draw shadows for 3D effect
        g.setColor(color.darker());
        g.drawLine(pixelX + 1, pixelY + CELL_SIZE - 2, pixelX + CELL_SIZE - 2, pixelY + CELL_SIZE - 2);
        g.drawLine(pixelX + CELL_SIZE - 2, pixelY + 1, pixelX + CELL_SIZE - 2, pixelY + CELL_SIZE - 2);
    }
}

/**
 * Panel that displays the next block preview
 */
class PreviewPanel extends JPanel {
    private final GameController gameController;
    private static final int CELL_SIZE = 20;
    
    /**
     * Constructor to initialize the preview panel
     * @param gameController The game controller
     */
    public PreviewPanel(GameController gameController) {
        this.gameController = gameController;
        setPreferredSize(new Dimension(120, 120));
    }
    
    /**
     * Paint the preview panel
     * @param g Graphics context
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Get the next block from the queue
        Block nextBlock = gameController.getNextBlock();
        if (nextBlock != null) {
            int[][] shape = nextBlock.getShape();
            
            // Calculate the center position for the preview
            int centerX = getWidth() / 2 - (shape[0].length * CELL_SIZE) / 2;
            int centerY = getHeight() / 2 - (shape.length * CELL_SIZE) / 2;
            
            // Draw the next block
            for (int i = 0; i < shape.length; i++) {
                for (int j = 0; j < shape[i].length; j++) {
                    if (shape[i][j] == 1) {
                        int x = centerX + j * CELL_SIZE;
                        int y = centerY + i * CELL_SIZE;
                        
                        // Fill the block with the specified color
                        g.setColor(nextBlock.getColor());
                        g.fillRect(x + 1, y + 1, CELL_SIZE - 2, CELL_SIZE - 2);
                        
                        // Draw highlights for 3D effect
                        g.setColor(nextBlock.getColor().brighter());
                        g.drawLine(x + 1, y + 1, x + CELL_SIZE - 2, y + 1);
                        g.drawLine(x + 1, y + 1, x + 1, y + CELL_SIZE - 2);
                        
                        // Draw shadows for 3D effect
                        g.setColor(nextBlock.getColor().darker());
                        g.drawLine(x + 1, y + CELL_SIZE - 2, x + CELL_SIZE - 2, y + CELL_SIZE - 2);
                        g.drawLine(x + CELL_SIZE - 2, y + 1, x + CELL_SIZE - 2, y + CELL_SIZE - 2);
                    }
                }
            }
        }
    }
}

/**
 * Interface for observing game state changes
 */
interface GameObserver {
    void onGameStateChanged(int score, int level);
}

