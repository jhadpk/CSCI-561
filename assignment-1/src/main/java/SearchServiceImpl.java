import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public abstract class SearchServiceImpl implements SearchService {
    protected final Cell landingCell;
    private final Integer targetCount;
    private final ArrayList<Cell> targetList;
    private final List<Integer> mapSize;
    protected final ArrayList<ArrayList<Integer>> elevationMap;
    private final Integer allowedSteepness;

    protected Cell[][] parentInfo;
    protected Boolean[][] visited;
    protected Integer[][] distanceMatrix;
    protected Integer[][] heuristic;


    public SearchServiceImpl(final Input input) {
        this.landingCell = input.getLandingCell();
        this.targetCount = input.getTargetCount();
        this.targetList = input.getTargetList();
        this.mapSize = input.getMapSize();
        this.elevationMap = input.getElevationMap();
        this.allowedSteepness = input.getAllowedSteepness();
    }


    @Override
    public ArrayList<ArrayList<Cell>> getOptimalPathToTargets() {
        final ArrayList<ArrayList<Cell>> optimalPathToTargets = new ArrayList<>();
        if (targetList.isEmpty()) {
            optimalPathToTargets.add(null);
            return optimalPathToTargets;
        }

        if (isNull(mapSize) || isNull(allowedSteepness) || isNull(targetCount) || isNull(landingCell) || !nodeInMap(
                landingCell) || elevationMap.size() == 0) {
            for (int i = 0; i < targetList.size(); i++) {
                optimalPathToTargets.add(null);
            }
            return optimalPathToTargets;
        }

        for (final Cell target : targetList) {
            if (null == target || !nodeInMap(target)) {
                optimalPathToTargets.add(null);
            } else {
                setupMeta();
                optimalPathToTargets.add(getOptimalPathToTarget(target));
            }
        }
        return optimalPathToTargets;
    }


    /***
     * Returns path of cells to target from landing cell
     *
     * @param target target cell
     * @return list of cells
     */
    protected abstract ArrayList<Cell> getOptimalPathToTarget(Cell target);


    /***
     * Returns the list of neighbours with cost to move, if movement is allowed.
     *
     * @param current current node of which neighbours are to be found
     * @return list of neighbours
     */
    protected abstract ArrayList<Cell> findNeighbours(final Cell current);


    protected void setupMeta() {
        parentInfo = new Cell[mapSize.get(0)][mapSize.get(1)];
        visited = new Boolean[mapSize.get(0)][mapSize.get(1)];
        distanceMatrix = new Integer[mapSize.get(0)][mapSize.get(1)];

        for (int i = 0; i < mapSize.get(0); i++) {
            for (int j = 0; j < mapSize.get(1); j++) {
                visited[i][j] = false;
            }
        }
        visited[landingCell.getX()][landingCell.getY()] = true;
        distanceMatrix[landingCell.getX()][landingCell.getY()] = 0;
    }


    private boolean isNull(Object object) {
        return null == object;
    }


    protected Boolean nodeInMap(final Cell cell) {
        return cell.getX() >= 0 && cell.getX() < mapSize.get(0) && cell.getY() >= 0 && cell.getY() < mapSize.get(1);
    }


    protected Boolean movementAllowed(final Cell neighbour, final Cell current) {
        return Math.abs(elevationMap.get(neighbour.getX()).get(neighbour.getY()) - elevationMap.get(current.getX())
                .get(current.getY())) <= allowedSteepness;
    }


    protected boolean isTarget(final Cell node, final Cell target) {
        return node.getX() == target.getX() && node.getY() == target.getY();
    }


    protected boolean containsTarget(final ArrayList<Cell> neighbours, final Cell target) {
        if (!neighbours.isEmpty()) {
            for (Cell neighbour : neighbours) {
                if (isTarget(neighbour, target)) {
                    return true;
                }
            }
        }
        return false;
    }


    protected ArrayList<Cell> retracePath(final Cell[][] parentInfo, final Cell target) {
        final ArrayList<Cell> path = new ArrayList<>();
        path.add(target);
        if (target.getX() == landingCell.getX() && target.getY() == landingCell.getY()) {
            path.add(landingCell);
            return path;
        }
        Cell cell = target;
        while (cell != landingCell) {
            if (cell.getX() < 0 || cell.getX() >= mapSize.get(0) || cell.getY() < 0 || cell.getY() >= mapSize.get(1)
                    || parentInfo[cell.getX()][cell.getY()] == null) {
                //cell not in mesh / no path lead to target / cell doesnt have a parent / target not in given mesh
                return null;
            }
            cell = parentInfo[cell.getX()][cell.getY()];
            path.add(cell);
        }
        Collections.reverse(path);
        return path;
    }


    protected int getDistance(final Cell parent, final Cell current, final SearchType searchType, int parentDistance,
            int heightDifference) {
        int pathCost = isDiagonal(parent, current) ? 14 : 10;
        int distance2D = parentDistance + pathCost;
        if (searchType == SearchType.AStar) {
            return distance2D + heightDifference;
        }
        return distance2D;
    }


    private boolean isDiagonal(final Cell currentNode, final Cell neighbour) {
        if (neighbour.getX() == currentNode.getX() - 1) {
            return neighbour.getY() == currentNode.getY() - 1 || neighbour.getY() == currentNode.getY() + 1;
        } else if (neighbour.getX() == currentNode.getX() + 1) {
            return neighbour.getY() == currentNode.getY() - 1 || neighbour.getY() == currentNode.getY() + 1;
        }
        return false;
    }


    protected Integer[][] createHeuristic(final Cell target) {
        Integer[][] heuristic = new Integer[mapSize.get(0)][mapSize.get(1)];
        for (int x = 0; x < mapSize.get(0); x++) {
            for (int y = 0; y < mapSize.get(1); y++) {
                heuristic[x][y] = findHeuristicDistance(new Cell(x, y), target);
            }
        }
        return heuristic;
    }


    private int findHeuristicDistance(final Cell source, final Cell target) {
        //return (int) Math.round(Math.sqrt(
        //        Math.pow((target.getX() - source.getX()), 2) + Math.pow((target.getY() - source.getY()), 2) + Math
        //                .pow(elevationMap.get(target.getX()).get(target.getY()) - elevationMap.get(source.getX())
        //                        .get(source.getY()), 2)
        //
        //));
        return Math.abs(target.getX() - source.getX()) + Math.abs(target.getY() - source.getY());
    }
}
