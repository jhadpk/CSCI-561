/**
 * @author deepakjha on 10/14/19
 * @project ai-assignments
 */
public class Coordinates {
    private final int row;
    private final int col;


    public Coordinates(final int row, final int col) {
        this.row = row;
        this.col = col;
    }


    public Coordinates(String coordinates) {
        this.row = Integer.parseInt(coordinates.split(",")[0]);
        this.col = Integer.parseInt(coordinates.split(",")[1]);
    }


    public int getRow() {
        return this.row;
    }


    public int getCol() {
        return this.col;
    }


    @Override
    public int hashCode() {
        return Integer.parseInt(row + "" + col);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        Coordinates coord = (Coordinates) o;
        return this.row == coord.row && this.col == coord.col;
    }
}
