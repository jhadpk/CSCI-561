import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class BFSAdapter extends SearchServiceImpl {

    public BFSAdapter(final Input input) {
        super(input);
    }


    @Override
    public ArrayList<Cell> getOptimalPathToTarget(Cell target) {
        final Queue<Cell> bfsQueue = new LinkedList<>();
        bfsQueue.add(landingCell);
        return getOptimalPath(bfsQueue, target);
    }


    private ArrayList<Cell> getOptimalPath(Queue<Cell> bfsQueue, Cell target) {
        while (bfsQueue.size() != 0) {
            Cell poppedCell = bfsQueue.poll();
            if (isTarget(poppedCell, target)) {
                parentInfo[target.getX()][target.getY()] = poppedCell;
                break;
            }
            ArrayList<Cell> neighbours = findNeighbours(poppedCell);
            if (containsTarget(neighbours, target)) {
                parentInfo[target.getX()][target.getY()] = poppedCell;
                break;
            }
            for (final Cell cell : neighbours) {
                if (isTarget(cell, target)) {
                    parentInfo[target.getX()][target.getY()] = poppedCell;
                    break;
                }
                if (!visited[cell.getX()][cell.getY()]) {
                    visited[cell.getX()][cell.getY()] = true;
                    bfsQueue.add(cell);
                    parentInfo[cell.getX()][cell.getY()] = poppedCell;
                }
            }
        }
        return retracePath(parentInfo, target);
    }


    @Override
    protected ArrayList<Cell> findNeighbours(final Cell current) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        for (int x = current.getX() - 1; x <= current.getX() + 1; x++) {
            for (int y = current.getY() - 1; y <= current.getY() + 1; y++) {
                if (!(x == current.getX() && y == current.getY())) {
                    Cell neighbour = new Cell(x, y);
                    if (nodeInMap(neighbour)) {
                        if (movementAllowed(neighbour, current)) {
                            neighbours.add(neighbour);
                        }
                    }
                }
            }
        }
        return neighbours;
    }
}
