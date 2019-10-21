/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class Cell {
    private int row;
    private int col;
    private PlayerType playerType;


    public Cell(final int row, final int col) {
        this.row = row;
        this.col = col;
    }


    public int getRow() {
        return this.row;
    }


    public int getCol() {
        return this.col;
    }


    public void setPlayerType(final PlayerType playerType) {
        this.playerType = playerType;
    }


    public PlayerType getPlayerType() {
        return this.playerType;
    }


    public Cell getLeft() {
        return Halma.getLeft(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getRight() {
        return Halma.getRight(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getTop() {
        return Halma.getTop(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getBottom() {
        return Halma.getBottom(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getTopLeft() {
        return Halma.getTopLeft(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getTopRight() {
        return Halma.getTopRight(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getBottomLeft() {
        return Halma.getBottomLeft(new Coordinates(this.getRow(), this.getCol()));
    }


    public Cell getBottomRight() {
        return Halma.getBottomRight(new Coordinates(this.getRow(), this.getCol()));
    }

}
