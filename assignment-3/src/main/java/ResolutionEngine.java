import java.util.ArrayList;
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
    private final HashMap<String, Set<String>> kbMapCopy;
    private final List<String> clauseSetCopy;


    public ResolutionEngine(HashMap<String, Set<String>> kbMap, Set<String> clauseSet) {
        this.kbMap = kbMap;
        this.clauseSet = clauseSet;
        this.clauseSetCopy = new ArrayList<>(clauseSet);
        this.kbMapCopy = new HashMap<>(kbMap);
    }

    public boolean resolve(String query) {
        String negatedQuery = negateQuery(query);
        return resolve(negatedQuery, 500);
    }

    public boolean resolve(String query, int threshold) {


        // A | B , ~B | C  => A | C


        threshold--;
        if (threshold == 0) {
            return false;
        }


        List<String> queryClauses = Arrays.asList(query.split("\\|"));
        if (queryClauses.size() == 1) {

        } else {
            for (String clause : queryClauses) {
                String queryPredicate = getPredicate(negateQuery(clause));
                List<String> queryArguments = getArguments(query);
                Set<String> matchingKbRules = kbMapCopy.get(queryPredicate);
                if (null != matchingKbRules) {
    
                }




            }
        }


        String queryPredicate = getPredicate(query);
        List<String> queryArguments = getArguments(query);
        //clauseList.add(0, negatedQuery);

        Set<String> matchingKbRules = kbMapCopy.get(queryPredicate);
        if (null != matchingKbRules) {
            for (String rule : matchingKbRules) {
                String updatedRule = rule;
                List<String> clauses = getClauses(rule);
                for (String clause : clauses) {
                    Map<String, String> theta = new HashMap<>();
                    final String clausePredicate = getPredicate(clause);
                    boolean unified = false;
                    List<String> argsInClause = getArguments(clause);
                    if (clausePredicate.equals(queryPredicate)) {
                        if (queryArguments.size() == argsInClause.size()) {
                            for (int i = 0; i < queryArguments.size(); i++) {
                                theta = unify(queryArguments.get(i), argsInClause.get(i), theta);
                                if (null == theta) {
                                    unified = false;
                                    break;
                                } else {
                                    unified = true;
                                    //if (theta.containsKey(queryArguments.get(i))) {
                                    //    //queryArguments.set(i, theta.get(queryArguments.get(i)));
                                    //    rule = rule.replaceAll(queryArguments.get(i),theta.get(queryArguments.get(i)));
                                    //} else if (theta.containsKey(argsInClause.get(i))) {
                                    //    //argsInClause.set(i, theta.get(argsInClause.get(i)));
                                    //    rule = rule.replaceAll(argsInClause.get(i),theta.get(argsInClause.get(i)));
                                    //}
                                }
                            }
                        }
                    }
                    if (unified) {
                        List<String> updatedClauses = new ArrayList<>(clauses);
                        updatedClauses.remove(clause);
                        updatedRule = String.join("|", updatedClauses);

                        for (String var : theta.keySet()) {
                            updatedRule = updatedRule.replaceAll(var, theta.get(var));
                        }
                        //clauseSetCopy.remove(rule);
                        //clauseSetCopy.add(updatedRule);

                        for (String c : getClauses(rule)) {
                            final String p = getPredicate(c);
                            kbMapCopy.get(p).remove(rule);
                            if (containsPredicate(updatedRule, p)) {
                                kbMapCopy.get(getPredicate(c)).add(updatedRule);
                            }
                        }
                        //kbMapCopy.get(queryPredicate).remove(rule);
                        //kbMapCopy.get(queryPredicate).add(updatedRule);
                        break;
                    }
                }
                if (updatedRule.isEmpty()) {
                    return true;
                }
                if (!updatedRule.equals(rule)) {
                    List<String> queries = Arrays.asList(updatedRule.split("\\|"));






                    if (queries.size() == 1) {
                        return resolve(negateQuery(queries.get(0)), threshold);
                    } else {
                        boolean resolution = true;
                        for (String newQuery : queries) {
                            resolution = resolution && resolve(negateQuery(newQuery), threshold);
                        }
                        return resolution;
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
        return Arrays.asList(clause.split("\\|"));
    }

    private String getPredicate(final String query) {
        return query.split("\\(")[0];
    }

    private List<String> getArguments(final String query) {
        return Arrays.asList(query.split("\\(")[1].split("\\)")[0].split(","));
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
            query = query.substring(1);
            if (query.startsWith("(") && query.endsWith(")")) {
                return query.substring(1, query.length()-1);
            }
            return query;
        } else {
            return "~" + query;
        }

        //List<String> clauses = Arrays.asList(query.split("\\|"));
        //if (clauses.size() == 1) {
        //    if (query.startsWith("~")) {
        //        query = query.substring(1);
        //        if (query.startsWith("(") && query.endsWith(")")) {
        //            return query.substring(1, query.length()-1);
        //        }
        //        return query;
        //    } else {
        //        return "~" + query;
        //    }
        //} else {
        //    for (String clause : clauses) {
        //        if (clause.startsWith("~")) {
        //            clause = clause.substring(1);
        //            if (clause.startsWith("(") && clause.endsWith(")")) {
        //                return clause.substring(1, clause.length()-1);
        //            }
        //        } else {
        //
        //        }
        //    }
        //}
    }
}
