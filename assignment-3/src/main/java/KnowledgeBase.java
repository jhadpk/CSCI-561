import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


/**
 * @author deepakjha on 11/20/19
 * @project ai-assignments
 */
public class KnowledgeBase {
    public HashMap<String, Set<String>> kbMap = new HashMap<>();
    public Set<String> clauseSet = new HashSet<>();


    public HashMap<String, Set<String>> getKbMap() {
        return kbMap;
    }

    public void addToKbMap(final String predicate, final String clause) {
        if (null != kbMap.get(predicate)) {
            kbMap.get(predicate).add(clause);
        } else {
            Set<String> clauseSet = new HashSet<>();
            clauseSet.add(clause);
            kbMap.put(predicate, clauseSet);
        }
    }

    public Set<String> getClauseSet() {
        return clauseSet;
    }

    public void addClause(final String clause) {
        clauseSet.add(clause);
    }
}
