import java.util.ArrayList;
import java.util.List;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class Input {
    private SearchType searchType = null;
    private List<Integer> mapSize = null;
    private Cell landingCell = null;
    private Integer allowedSteepness = null;
    private Integer targetCount = null;
    private ArrayList<Cell> targetList = new ArrayList<>();
    private ArrayList<ArrayList<Integer>> elevationMap = new ArrayList<>();


    public SearchType getSearchType() {
        return this.searchType;
    }

    public void setSearchType(final SearchType searchType) {
        this.searchType = searchType;
    }

    public List<Integer> getMapSize() {
        return this.mapSize;
    }

    public void setMapSize(final List<Integer> mapSize) {
        this.mapSize = mapSize;
    }

    public Cell getLandingCell() {
        return this.landingCell;
    }

    public void setLandingCell(final Cell landingCell) {
        this.landingCell = landingCell;
    }

    public Integer getAllowedSteepness() {
        return this.allowedSteepness;
    }

    public void setAllowedSteepness(final Integer allowedSteepness) {
        this.allowedSteepness = allowedSteepness;
    }

    public Integer getTargetCount() {
        return this.targetCount;
    }

    public void setTargetCount(final Integer targetCount) {
        this.targetCount = targetCount;
    }

    public ArrayList<Cell> getTargetList() {
        return this.targetList;
    }

    public void setTargetList(final ArrayList<Cell> targetList) {
        this.targetList = targetList;
    }

    public ArrayList<ArrayList<Integer>> getElevationMap() {
        return this.elevationMap;
    }

    public void setElevationMap(final ArrayList<ArrayList<Integer>> elevationMap) {
        this.elevationMap = elevationMap;
    }


}
