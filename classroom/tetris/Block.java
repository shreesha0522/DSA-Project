package tetris;

import java.awt.Color;

/**
 * Class representing a Tetris block (tetromino)
 */
class Block {
    private int[][] shape;
    private final int[][] originalShape; // Store the original shape for rotation
    private final Color color;
    private int x;
    private int y;
    
    /**
     * Constructor to initialize a block with a shape and color
     * @param shape The shape of the block as a 2D array
     * @param color The color of the block
     */
    public Block(int[][] shape, Color color) {
        // Create a deep copy of the shape
        this.shape = new int[shape.length][];
        for (int i = 0; i < shape.length; i++) {
            this.shape[i] = new int[shape[i].length];
            System.arraycopy(shape[i], 0, this.shape[i], 0, shape[i].length);
        }
        
        // Create a deep copy of the original shape for rotation
        this.originalShape = new int[shape.length][];
        for (int i = 0; i < shape.length; i++) {
            this.originalShape[i] = new int[shape[i].length];
            System.arraycopy(shape[i], 0, this.originalShape[i], 0, shape[i].length);
        }
        
        this.color = color;
        this.x = 0;
        this.y = 0;
    }
    
    /**
     * Get the shape of the block
     * @return The shape as a 2D array
     */
    public int[][] getShape() {
        return shape;
    }
    
    /**
     * Get the color of the block
     * @return The color
     */
    public Color getColor() {
        return color;
    }
    
    /**
     * Get the x position of the block
     * @return The x position
     */
    public int getX() {
        return x;
    }
    
    /**
     * Get the y position of the block
     * @return The y position
     */
    public int getY() {
        return y;
    }
    
    /**
     * Get the width of the block
     * @return The width
     */
    public int getWidth() {
        return shape[0].length;
    }
    
    /**
     * Get the height of the block
     * @return The height
     */
    public int getHeight() {
        return shape.length;
    }
    
    /**
     * Set the position of the block
     * @param x The x position
     * @param y The y position
     */
    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    /**
     * Move the block left
     */
    public void moveLeft() {
        x--;
    }
    
    /**
     * Move the block right
     */
    public void moveRight() {
        x++;
    }
    
    /**
     * Move the block down
     */
    public void moveDown() {
        y++;
    }
    
    /**
     * Move the block up
     */
    public void moveUp() {
        y--;
    }
    
    /**
     * Rotate the block clockwise
     */
    public void rotate() {
        // Get the dimensions of the current shape
        int rows = shape.length;
        int cols = shape[0].length;
        
        // Create a new array for the rotated shape
        int[][] rotated = new int[cols][rows];
        
        // Perform the rotation
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotated[col][rows - 1 - row] = shape[row][col];
            }
        }
        
        // Update the shape
        shape = rotated;
    }
    
    /**
     * Rotate the block counter-clockwise (used to undo a rotation)
     */
    public void rotateBack() {
        // Get the dimensions of the current shape
        int rows = shape.length;
        int cols = shape[0].length;
        
        // Create a new array for the rotated shape
        int[][] rotated = new int[cols][rows];
        
        // Perform the rotation
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                rotated[cols - 1 - col][row] = shape[row][col];
            }
        }
        
        // Update the shape
        shape = rotated;
    }
}

