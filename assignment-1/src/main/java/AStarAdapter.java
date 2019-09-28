import java.util.ArrayList;
import java.util.PriorityQueue;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class AStarAdapter extends SearchServiceImpl {

    public AStarAdapter(final Input input) {
        super(input);
    }


    @Override
    public ArrayList<Cell> getOptimalPathToTarget(Cell target) {
        final PriorityQueue<Cell> pQueue = new PriorityQueue<>(new CellComparator());
        heuristic = createHeuristic(target);
        pQueue.add(landingCell);
        return getOptimalPath(pQueue, target);
    }


    private ArrayList<Cell> getOptimalPath(PriorityQueue<Cell> pQueue, Cell target) {
        while (pQueue.size() != 0) {
            final Cell poppedCell = pQueue.poll();
            if (isTarget(poppedCell, target)) {
                break;
            }
            final ArrayList<Cell> neighbours = findNeighbours(poppedCell);
            for (final Cell cell : neighbours) {
                final int travelDistance = cell.getDistance() - heuristic[cell.getX()][cell.getY()];
                if (!visited[cell.getX()][cell.getY()]) {
                    visited[cell.getX()][cell.getY()] = true;
                    pQueue.add(cell);
                    parentInfo[cell.getX()][cell.getY()] = poppedCell;
                    distanceMatrix[cell.getX()][cell.getY()] = travelDistance;
                } else {
                    if (travelDistance < distanceMatrix[cell.getX()][cell.getY()]) {
                        pQueue.remove(cell);
                        pQueue.add(cell);
                        parentInfo[cell.getX()][cell.getY()] = poppedCell;
                        distanceMatrix[cell.getX()][cell.getY()] = travelDistance;
                    }
                }
            }
        }
        return retracePath(parentInfo, target);
    }


    @Override
    protected ArrayList<Cell> findNeighbours(Cell current) {
        ArrayList<Cell> neighbours = new ArrayList<>();
        for (int x = current.getX() - 1; x <= current.getX() + 1; x++) {
            for (int y = current.getY() - 1; y <= current.getY() + 1; y++) {
                if (!(x == current.getX() && y == current.getY())) {
                    Cell neighbour = new Cell(x, y);
                    if (nodeInMap(neighbour)) {
                        if (movementAllowed(neighbour, current)) {
                            neighbour.setDistance(getDistance(current, neighbour, SearchType.AStar,
                                    distanceMatrix[current.getX()][current.getY()], Math.abs(
                                            elevationMap.get(neighbour.getX()).get(neighbour.getY()) - elevationMap
                                                    .get(current.getX()).get(current.getY()))) + heuristic[neighbour
                                    .getX()][neighbour.getY()]);
                            neighbours.add(neighbour);
                        }
                    }
                }
            }
        }
        return neighbours;
    }
}