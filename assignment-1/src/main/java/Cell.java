/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class Cell {
    private int x;
    private int y;

    private int distance;


    public Cell(final int x, final int y) {
        this.x = x;
        this.y = y;
    }


    public int getX() {
        return this.x;
    }


    public int getY() {
        return this.y;
    }


    public void setDistance(int distance) {
        this.distance = distance;
    }


    public int getDistance() {
        return this.distance;
    }
}
