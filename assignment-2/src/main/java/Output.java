/**
 * @author deepakjha on 10/17/19
 * @project ai-assignments
 */
public class Output {
    private MoveType moveType;
    private Cell startingCell;
    private Cell destinationCell;


    public Output(MoveType moveType, Cell startingCell, Cell destinationCell) {
        this.moveType = moveType;
        this.startingCell = startingCell;
        this.destinationCell = destinationCell;
    }


    public MoveType getMoveType() {return this.moveType;}


    public Cell getStartingCell() {return this.startingCell;}


    public Cell getDestinationCell() {return this.destinationCell;}

    public boolean isOutputNotNull() {
        return null != moveType && null != startingCell && null != destinationCell;
    }
}
