import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author deepakjha on 11/19/19
 * @project ai-assignments
 */
public class ResolutionEngine {
    private final HashMap<String, Set<String>> kbMap;
    private final Set<String> clauseSet;
    //private final List<String> clauseList;

    public ResolutionEngine(HashMap<String, Set<String>> kbMap, Set<String> clauseSet) {
        this.kbMap = kbMap;
        this.clauseSet = clauseSet;
        //this.clauseList = new ArrayList<>(clauseSet);
    }


    public boolean resolve(String query) {
        String predicate = getPredicate(query);
        List<String> arguments = getArguments(query);
        String negatedQuery = negateQuery(query);
        //clauseList.add(0, negatedQuery);

        Set<String> validClauses = kbMap.get(predicate);
        for (String clause : validClauses) {
            List<String> clauses = getClauses(clause);
            for (String c : clauses) {
                if (getPredicate(c).equals(predicate)) {
                    List<String> argsInC = getArguments(c);
                    if (arguments.size() == argsInC.size()) {

                    }
                }
            }

            //unify(clause, negatedQuery);
        }

        clauseSet.add(negatedQuery);
        return false;
    }

    private List<String> getClauses(final String clause) {
        return Arrays.asList(clause.split("\\|"));
    }

    private String getPredicate(final String query) {
        return query.split("\\(")[0];
    }

    private List<String> getArguments(final String query) {
        return Arrays.asList(query.split("\\(")[1].split("\\)")[0].split(","));
    }


    public static void main(String[] args) {
        Map<String, String> theta = new HashMap<>();
        Map<String, String> result = new ResolutionEngine(null, null).unify("Deepak", "x", theta);

    }



    private Map<String, String> unify(String x, String y, Map<String, String> theta) {
        if (theta == null) {
            return null;
        } else if (x.equals(y)) {
            return theta;
        } else if (isVariable(x)) {
            return unifyVar(x, y, theta);
        } else if (isVariable(y)) {
            return unifyVar(y, x, theta);
        } else if (isCompound(x) && isCompound(y)) {
            return unify(getArguments(x), getArguments(y), unifyOps(getPredicate(x), getPredicate(y), theta));
        } else {
            return null;
        }
    }


    public Map<String, String> unify(List<String> x, List<String> y, Map<String, String> theta) {
        if (theta == null) {
            return null;
        } else if (x.size() != y.size()) {
            return null;
        } else if (x.size() == 0) {
            return theta;
        } else if (x.size() == 1) {
            return unify(x.get(0), y.get(0), theta);
        } else {
            return unify(x.subList(1, x.size()), y.subList(1, y.size()),
                    unify(x.get(0), y.get(0), theta));
        }
    }


    private Map<String, String> unifyOps(String x, String y, Map<String, String> theta) {
        if (theta == null) {
            return null;
        } else if (x.equals(y)) {
            return theta;
        } else {
            return null;
        }
    }

    private Map<String, String> unifyVar(String var, String x, Map<String, String> theta) {
        if (!isConstant(x)) {
            return null;
        } else if (theta.containsKey(var)) {
            return unify(theta.get(var), x, theta);
        } else if (theta.containsKey(x)) {
            return unify(var, theta.get(x), theta);
        } else {

            theta.put(var, x);

            return theta;
        }
    }


    private boolean isVariable(String var) {
        return var.charAt(0) >= 96 && var.charAt(0) <= 122;
    }

    private boolean isConstant(String var) {
        return var.charAt(0) >= 65 && var.charAt(0) <= 90 && !var.contains("(") && !var.contains(")");
    }

    private boolean isCompound(String var) {
        return !isConstant(var) && !isVariable(var);
    }

    private static String negateQuery(String query) {
        if (query.startsWith("~")) {
            return query.substring(1);
        } else {
            return "~" + query;
        }
    }
}
