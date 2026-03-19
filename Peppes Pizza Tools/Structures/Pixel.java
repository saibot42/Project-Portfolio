package Structures;

public class Pixel {
    private int x; //Latitidue
    private int y; //Longitide

    
    /**
     * Stores a pixel / point on a grid. 
     * X is the same as latitude and y is the same as longtidude
     * @param x
     * @param y
     */
    public Pixel(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int newX) {
        this.x = newX;
    }

    public void setY(int newY) {
        this.y = newY;
    }

}
