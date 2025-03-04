package tetris;

import java.awt.Color;  // Use specific Timer import
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import javax.swing.Timer;

/**
 * Main controller class that manages the game logic
 */
class GameController {
    // Constants for the game board dimensions
    public static final int BOARD_WIDTH = 10;
    public static final int BOARD_HEIGHT = 20;
    
    // Game state variables
    private Color[][] board;
    private Block currentBlock;
    private final Queue<Block> blockQueue;
    private final Stack<Color[]> boardStack;
    private boolean isGameOver;
    private boolean isPaused;
    private int score;
    private int level;
    private int linesCleared;
    private final Timer gameTimer;
    private final List<GameObserver> observers = new ArrayList<>();
    
    /**
     * Constructor to initialize the game controller
     */
    public GameController() {
        // Initialize the game board
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        
        // Initialize the block queue
        blockQueue = new LinkedList<>();
        
        // Initialize the board stack
        boardStack = new Stack<>();
        
        // Initialize the observers list
        
        // Initialize game state
        isGameOver = false;
        isPaused = false;
        score = 0;
        level = 1;
        linesCleared = 0;
        
        // Generate initial blocks for the queue
        for (int i = 0; i < 3; i++) {
            blockQueue.add(generateRandomBlock());
        }
        
        // Initialize the game timer
        gameTimer = new javax.swing.Timer(calculateDropInterval(), e -> {
            if (!isPaused && !isGameOver) {
                if (!moveDown()) {
                    placeBlock();
                    checkLines();
                    spawnBlock();
                    checkGameOver();
                }
                notifyObservers();
            }
        });
    }
    
    /**
     * Start the game
     */
    public void startGame() {
        // Reset the game state
        board = new Color[BOARD_HEIGHT][BOARD_WIDTH];
        blockQueue.clear();
        boardStack.clear();
        isGameOver = false;
        isPaused = false;
        score = 0;
        level = 1;
        linesCleared = 0;
        
        // Generate initial blocks for the queue
        for (int i = 0; i < 3; i++) {
            blockQueue.add(generateRandomBlock());
        }
        
        // Spawn the first block
        spawnBlock();
        
        // Start the game timer
        gameTimer.setDelay(calculateDropInterval());
        gameTimer.start();
        
        // Notify observers of the game state change
        notifyObservers();
    }
    
    /**
     * Toggle the pause state of the game
     */
    public void togglePause() {
        isPaused = !isPaused;
    }
    
    /**
     * Move the current block to the left
     * @return true if the move was successful, false otherwise
     */
    public boolean moveLeft() {
        if (isPaused || isGameOver || currentBlock == null) {
            return false;
        }
        
        // Try to move the block left
        currentBlock.moveLeft();
        
        // Check if the move is valid
        if (isCollision()) {
            // If there's a collision, move the block back
            currentBlock.moveRight();
            return false;
        }
        
        return true;
    }
    
    /**
     * Move the current block to the right
     * @return true if the move was successful, false otherwise
     */
    public boolean moveRight() {
        if (isPaused || isGameOver || currentBlock == null) {
            return false;
        }
        
        // Try to move the block right
        currentBlock.moveRight();
        
        // Check if the move is valid
        if (isCollision()) {
            // If there's a collision, move the block back
            currentBlock.moveLeft();
            return false;
        }
        
        return true;
    }
    
    /**
     * Move the current block down
     * @return true if the move was successful, false otherwise
     */
    public boolean moveDown() {
        if (isPaused || isGameOver || currentBlock == null) {
            return false;
        }
        
        // Try to move the block down
        currentBlock.moveDown();
        
        // Check if the move is valid
        if (isCollision()) {
            // If there's a collision, move the block back
            currentBlock.moveUp();
            return false;
        }
        
        return true;
    }
    
    /**
     * Rotate the current block
     * @return true if the rotation was successful, false otherwise
     */
    public boolean rotate() {
        if (isPaused || isGameOver || currentBlock == null) {
            return false;
        }
        
        // Try to rotate the block
        currentBlock.rotate();
        
        // Check if the rotation is valid
        if (isCollision()) {
            // If there's a collision, rotate the block back
            currentBlock.rotateBack();
            return false;
        }
        
        return true;
    }
    
    /**
     * Perform a hard drop of the current block
     */
    public void hardDrop() {
        if (isPaused || isGameOver || currentBlock == null) {
            return;
        }
        
        // Move the block down until it collides
        while (moveDown()) {
            // Increment score for each cell dropped
            score += 2;
        }
        
        // Place the block, check for completed lines, and spawn a new block
        placeBlock();
        checkLines();
        spawnBlock();
        checkGameOver();
        
        // Notify observers of the game state change
        notifyObservers();
    }
    
    /**
     * Check if the current block collides with the board or other blocks
     * @return true if there's a collision, false otherwise
     */
    private boolean isCollision() {
        if (currentBlock == null) {
            return false;
        }
        
        int[][] shape = currentBlock.getShape();
        int blockX = currentBlock.getX();
        int blockY = currentBlock.getY();
        
        // Check each cell of the block
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int boardX = blockX + col;
                    int boardY = blockY + row;
                    
                    // Check if the cell is out of bounds
                    if (boardX < 0 || boardX >= BOARD_WIDTH || boardY < 0 || boardY >= BOARD_HEIGHT) {
                        return true;
                    }
                    
                    // Check if the cell collides with a placed block
                    if (boardY >= 0 && board[boardY][boardX] != null) {
                        return true;
                    }
                }
            }
        }
        
        return false;
    }
    
    /**
     * Place the current block on the board
     */
    private void placeBlock() {
        if (currentBlock == null) {
            return;
        }
        
        int[][] shape = currentBlock.getShape();
        int blockX = currentBlock.getX();
        int blockY = currentBlock.getY();
        Color color = currentBlock.getColor();
        
        // Place each cell of the block on the board
        for (int row = 0; row < shape.length; row++) {
            for (int col = 0; col < shape[row].length; col++) {
                if (shape[row][col] == 1) {
                    int boardX = blockX + col;
                    int boardY = blockY + row;
                    
                    // Check if the cell is within bounds
                    if (boardY >= 0 && boardY < BOARD_HEIGHT && boardX >= 0 && boardX < BOARD_WIDTH) {
                        board[boardY][boardX] = color;
                    }
                }
            }
        }
        
        // Add the current state of the board to the stack
        pushBoardToStack();
        
        // Increment score for placing a block
        score += 10;
    }
    
    /**
     * Push the current state of the board to the stack
     */
    private void pushBoardToStack() {
        // Create a copy of each row and push it to the stack
        for (int row = 0; row < BOARD_HEIGHT; row++) {
            Color[] rowCopy = new Color[BOARD_WIDTH];
            System.arraycopy(board[row], 0, rowCopy, 0, BOARD_WIDTH);
            boardStack.push(rowCopy);
        }
    }
    
    /**
     * Check for completed lines and remove them
     */
    private void checkLines() {
        int linesRemoved = 0;
        
        // Check each row from bottom to top
        for (int row = BOARD_HEIGHT - 1; row >= 0; row--) {
            boolean isLineComplete = true;
            
            // Check if the row is complete
            for (int col = 0; col < BOARD_WIDTH; col++) {
                if (board[row][col] == null) {
                    isLineComplete = false;
                    break;
                }
            }
            
            // If the row is complete, remove it
            if (isLineComplete) {
                // Remove the row
                for (int r = row; r > 0; r--) {
                    for (int col = 0; col < BOARD_WIDTH; col++) {
                        board[r][col] = board[r - 1][col];
                    }
                }
                
                // Clear the top row
                for (int col = 0; col < BOARD_WIDTH; col++) {
                    board[0][col] = null;
                }
                
                // Pop the completed row from the stack
                if (!boardStack.isEmpty()) {
                    boardStack.pop();
                }
                
                // Increment the counter and check the same row again (since rows above have moved down)
                linesRemoved++;
                row++;
            }
        }
        
        // Update score and level based on lines removed
        if (linesRemoved > 0) {
            // Calculate score based on number of lines removed
            // 1 line = 100, 2 lines = 300, 3 lines = 500, 4 lines = 800
            int[] scoreValues = {0, 100, 300, 500, 800};
            score += scoreValues[linesRemoved] * level;
            
            // Update lines cleared and check for level up
            linesCleared += linesRemoved;
            level = (linesCleared / 10) + 1;
            
            // Update game speed based on level
            gameTimer.setDelay(calculateDropInterval());
        }
    }
    
    /**
     * Spawn a new block at the top of the board
     */
    private void spawnBlock() {
        // Get the next block from the queue
        currentBlock = blockQueue.poll();
        
        // Add a new block to the queue
        blockQueue.add(generateRandomBlock());
        
        // Position the block at the top center of the board
        if (currentBlock != null) {
            currentBlock.setPosition((BOARD_WIDTH - currentBlock.getWidth()) / 2, 0);
        }
    }
    
    /**
     * Check if the game is over
     */
    private void checkGameOver() {
        // Game is over if the current block collides immediately after spawning
        if (currentBlock != null && isCollision()) {
            isGameOver = true;
            gameTimer.stop();
        }
    }
    
    /**
     * Generate a random Tetris block
     * @return A new random Block
     */
    private Block generateRandomBlock() {
        // Define the shapes of the Tetris blocks (I, J, L, O, S, T, Z)
        int[][][] shapes = {
            // I shape
            {
                {1, 1, 1, 1}
            },
            // J shape
            {
                {1, 0, 0},
                {1, 1, 1}
            },
            // L shape
            {
                {0, 0, 1},
                {1, 1, 1}
            },
            // O shape
            {
                {1, 1},
                {1, 1}
            },
            // S shape
            {
                {0, 1, 1},
                {1, 1, 0}
            },
            // T shape
            {
                {0, 1, 0},
                {1, 1, 1}
            },
            // Z shape
            {
                {1, 1, 0},
                {0, 1, 1}
            }
        };
        
        // Define the colors of the Tetris blocks
        Color[] colors = {
            Color.CYAN,    // I shape
            Color.BLUE,    // J shape
            Color.ORANGE,  // L shape
            Color.YELLOW,  // O shape
            Color.GREEN,   // S shape
            Color.MAGENTA, // T shape
            Color.RED      // Z shape
        };
        
        // Generate a random index
        int index = (int) (Math.random() * shapes.length);
        
        // Create and return a new block with the selected shape and color
        return new Block(shapes[index], colors[index]);
    }
    
    /**
     * Calculate the drop interval based on the current level
     * @return The drop interval in milliseconds
     */
    private int calculateDropInterval() {
        // Base interval is 1000ms (1 second)
        // Decrease by 50ms for each level, with a minimum of 100ms
        return Math.max(1000 - (level - 1) * 50, 100);
    }
    
    /**
     * Add an observer to be notified of game state changes
     * @param observer The observer to add
     */
    public void addObserver(GameObserver observer) {
        observers.add(observer);
    }
    
    /**
     * Notify all observers of game state changes
     */
    private void notifyObservers() {
        for (GameObserver observer : observers) {
            observer.onGameStateChanged(score, level);
        }
    }
    
    /**
     * Get the current game board
     * @return The game board
     */
    public Color[][] getBoard() {
        return board;
    }
    
    /**
     * Get the current falling block
     * @return The current block
     */
    public Block getCurrentBlock() {
        return currentBlock;
    }
    
    /**
     * Get the next block from the queue without removing it
     * @return The next block
     */
    public Block getNextBlock() {
        return blockQueue.peek();
    }
    
    /**
     * Check if the game is over
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        return isGameOver;
    }
}

