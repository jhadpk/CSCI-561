import java.util.List;


/**
 * @author deepakjha on 11/26/19
 * @project ai-assignments
 */
public class PredicateInfo {
    private int index;
    private List<String> predicates;


    PredicateInfo(int index, List<String> predicates) {
        this.index = index;
        this.predicates = predicates;
    }


    public int getIndex() {
        return this.index;
    }


    public List<String> getPredicates() {
        return this.predicates;
    }
}
