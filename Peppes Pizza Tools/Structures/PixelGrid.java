package Structures;

import java.util.*;

public class PixelGrid {

    private int cellWidth;
    private int cellHeight;
    private int gridWidth;
    private int gridHeight;
    private int[][] gridCellCounts;

    public PixelGrid(int gridWidth, int gridHeight, int imgWidth, int imgHeight) {
        this.cellWidth = imgWidth / imgWidth * 5;
        this.cellHeight = 5;
        this.gridWidth = gridWidth; //The map width and heigh are off (if divided by 5 to 15 the highlighted pixels will be visible)
        this.gridHeight = gridHeight;
        gridCellCounts = new int[gridWidth][gridHeight]; // Initialize count matrix
    }

    public int getGridWidth() {
        return gridWidth;
    }

    public int getGridHeight() {
        return gridHeight;
    }

    public void setGridWidth(int gridWidth) {
        this.gridWidth = gridWidth;
    }

    public void setGridHeight(int gridHeight) {
        this.gridHeight = gridHeight;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void incrementCell(int x, int y) {
        gridCellCounts[x][y]++;
    }

    public int getCellCount(int x, int y) {
        return gridCellCounts[x][y];
    }
}
