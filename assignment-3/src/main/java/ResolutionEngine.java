import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author deepakjha on 11/19/19
 * @project ai-assignments
 */
public class ResolutionEngine {
    private final HashMap<String, Set<String>> kbMap;
    private HashMap<String, Set<String>> kbMapCopy;


    public ResolutionEngine(HashMap<String, Set<String>> kbMap) {
        this.kbMap = kbMap;
        kbMapCopy = deepcopy(this.kbMap);
    }


    public boolean resolve(String query) {
        String negatedQuery = negateQuery(query);
        String predicate = getPredicate(negatedQuery);
        Set<String> value = new HashSet<>();
        value.add(negatedQuery);
        if (null != kbMapCopy.get(predicate)) {
            kbMapCopy.get(predicate).add(negatedQuery);
        } else {
            kbMapCopy.put(predicate, value);
        }
        return resolve(negatedQuery, 500);
    }


    public boolean resolve(String query, int threshold) {
        threshold--;
        if (threshold == 0) {
            return false;
        }
        final List<String> queryClauses = getClauses(query);
        for (String queryClause : queryClauses) {
            final String queryPredicate = getPredicate(negateQuery(queryClause));
            final List<String> queryArguments = getArguments(queryClause);
            final Set<String> matchingKbRules = kbMapCopy.get(queryPredicate);
            if (null != matchingKbRules) {
                for (String rule : matchingKbRules) {
                    String newKnowledge = "";

                    final List<String> clauses = getClauses(rule);
                    boolean partialResolved = false;

                    for (String clause : clauses) {
                        Map<String, String> theta = new HashMap<>();
                        final String clausePredicate = getPredicate(clause);
                        boolean unified = false;
                        List<String> clauseArguments = getArguments(clause);
                        if (clausePredicate.equals(queryPredicate)) {
                            if (queryArguments.size() == clauseArguments.size()) {
                                for (int i = 0; i < queryArguments.size(); i++) {
                                    theta = unify(queryArguments.get(i).trim(), clauseArguments.get(i).trim(), theta);
                                    if (null == theta) {
                                        unified = false;
                                        break;
                                    } else {
                                        unified = true;
                                    }
                                }
                            }
                        }
                        if (unified) {
                            String updatedQuery = updateStatement(queryClauses, queryClause);
                            String updatedRule = updateStatement(clauses, clause);
                            for (String var : theta.keySet()) {
                                updatedQuery = updatedQuery.replaceAll(var, theta.get(var));
                                updatedRule = updatedRule.replaceAll(var, theta.get(var));
                            }
                            newKnowledge = binaryResolve(updatedQuery, updatedRule);
                            //updateKb(query, newKnowledge);
                            //updateKb(rule, newKnowledge);
                            partialResolved = true;
                            break;
                        }
                    }
                    if (partialResolved && newKnowledge.isEmpty()) {
                        return true;
                    } else if (!newKnowledge.isEmpty()) {
                        //System.out.println("Resolving : " + newKnowledge);
                        return resolve(newKnowledge, threshold);
                    }
                }
            }
        }
        return false;
    }


    private boolean containsPredicate(String rule, String predicate) {
        List<String> clauses = getClauses(rule);
        for (String clause : clauses) {
            if (getPredicate(clause).equals(predicate)) {
                return true;
            }
        }
        return false;
    }


    private List<String> getClauses(final String clause) {
        List<String> clauses = new ArrayList<>();
        for (String c : clause.split("\\|")) {
            clauses.add(c.trim());
        }
        return clauses;
    }


    private String getPredicate(final String query) {
        return query.split("\\(")[0].trim();
    }


    private List<String> getArguments(final String query) {
        List<String> arguments = new ArrayList<>();
        for (String a : query.split("\\(")[1].trim().split("\\)")[0].trim().split(",")) {
            arguments.add(a.trim());
        }
        return arguments;
    }


    private String updateStatement(List<String> clauses, String clause) {
        List<String> updatedClauses = new ArrayList<>(clauses);
        updatedClauses.remove(clause);
        return String.join("|", updatedClauses);
    }

    private Map<String, String> unify(String x, String y, Map<String, String> theta) {
        if (theta == null) {
            return null;
        } else if (x.equals(y)) {
            return theta;
        } else if (isVariable(x) && isVariable(y)) {
            theta.put(x, y);
            return theta;
        } else if (isVariable(x)) {
            return unifyVar(x, y, theta);
        } else if (isVariable(y)) {
            return unifyVar(y, x, theta);
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


    private static String negateQuery(String query) {
        if (query.startsWith("~")) {
            query = query.substring(1);
            if (query.startsWith("(") && query.endsWith(")")) {
                return query.substring(1, query.length() - 1);
            }
            return query;
        } else {
            return "~" + query;
        }
    }

    private HashMap<String, Set<String>> deepcopy(HashMap<String, Set<String>> kbMap) {
        HashMap<String, Set<String>> kbMapCopy = new HashMap<>();
        for (Map.Entry<String, Set<String>> entry : kbMap.entrySet()) {
            kbMapCopy.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return kbMapCopy;
    }


    @Deprecated
    private void updateKb(final String rule, final String newKnowledge) {
        for (String c : getClauses(rule)) {
            final String p = getPredicate(c);
            kbMapCopy.get(p).remove(rule);
            if (containsPredicate(newKnowledge, p)) {
                kbMapCopy.get(p).add(newKnowledge);
            }

        }
    }


    private String binaryResolve(String s1, String s2) {
        if (!s1.isEmpty() && !s2.isEmpty()) {
            return s1 + "|" + s2;
        } else if (s1.isEmpty()) {
            return s2;
        } else {
            return s1;
        }
    }
}
