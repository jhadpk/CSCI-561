import java.util.Comparator;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class CellComparator implements Comparator<Cell> {

    @Override
    public int compare(Cell cell1, Cell cell2) {
        return Integer.compare(cell1.getDistance(), cell2.getDistance());
    }
}
