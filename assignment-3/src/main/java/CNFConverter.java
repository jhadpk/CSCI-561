import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * @author deepakjha on 11/16/19
 * @project ai-assignments
 */
public class CNFConverter {
    private List<HashMap<String, String>> predicateMapList = new ArrayList<>();
    private final KnowledgeBase kb;

    public CNFConverter(final KnowledgeBase kb) {
        this.kb = kb;
    }

    public void convertToCnfAndPopulateKb(final ArrayList<String> inputSentences) {
        for (int i = 0; i < inputSentences.size(); i++) {
            inputSentences.set(i, inputSentences.get(i).replaceAll(" ", "").trim());
            inputSentences.set(i, findPredicates(inputSentences.get(i)));
            inputSentences.set(i, eliminateImplications(inputSentences.get(i)));
            inputSentences.set(i, standardiseParenthesis(inputSentences.get(i)));
            inputSentences.set(i, moveNegationInward(inputSentences.get(i)));
            inputSentences.set(i, distributeOrOverAnd(inputSentences.get(i)));
            extractClauses(inputSentences.get(i), i);
        }
        int index = 0;
        for (String clause : kb.getClauseSet()) {
            index++;
            clause = standardiseVariables(clause, index);
            populateKB(clause);
        }
    }


    private String findPredicates(String sentence) {
        HashMap<String, String> pMap = new HashMap<>();
        int end = 0;
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) >= 65 && sentence.charAt(i) <= 90) {
                int j = i;
                while (sentence.charAt(j) != ')') {
                    if (sentence.charAt(j) == '(') {
                        end = j;
                    }
                    j++;
                }

                String predicate = sentence.substring(i, end);
                String value = sentence.substring(i, j + 1);
                String append = "";
                while (pMap.get(predicate) != null) {
                    if (pMap.get(predicate).equals(value)) {
                        break;
                    } else {
                        predicate = predicate + "$";
                        append = append + "$";
                    }
                }
                pMap.put(predicate, value);
                pMap.put("~" + predicate, "~" + value);
                sentence = sentence.substring(0, end) + append + sentence.substring(j + 1);
                i = end;
            }
        }
        predicateMapList.add(pMap);
        return sentence;
    }


    //6.There will be no parentheses in the KB except as used to denote arguments of predicates.
    private String eliminateImplications(String sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '=' && sentence.charAt(i + 1) == '>') {
                int leftEnd = i - 1;
                int rightStart = i + 2;
                sentence = "~(" + sentence.substring(0, leftEnd + 1) + ")" + "|" + sentence.substring(rightStart);
                i = rightStart;
            }
        }
        return sentence;
    }


    private String standardiseParenthesis(String sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '(') {
                int j = i + 1;
                int nestedBrackets = 0;
                int insideOpenBracketIndex = -1;
                while (sentence.charAt(j) != ')') {
                    if (sentence.charAt(j) == '(') {
                        nestedBrackets++;
                        insideOpenBracketIndex = j;
                    }
                    j++;
                }
                boolean hasMultiplePredicates;
                if (nestedBrackets > 0) {
                    hasMultiplePredicates = multiplePredicatesPresent(
                            sentence.substring(insideOpenBracketIndex + 1, j));
                    if (!hasMultiplePredicates) {
                        sentence = sentence.substring(0, insideOpenBracketIndex) + sentence.substring(
                                insideOpenBracketIndex + 1, j) + sentence.substring(j + 1);
                    }
                } else {
                    hasMultiplePredicates = multiplePredicatesPresent(sentence.substring(i + 1, j));
                    if (!hasMultiplePredicates) {
                        sentence = sentence.substring(0, i) + sentence.substring(i + 1, j) + sentence.substring(j + 1);
                    }
                }
            }
        }
        return sentence;
    }


    private boolean multiplePredicatesPresent(final String sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '|' || sentence.charAt(i) == '&') {
                return true;
            }
        }
        return false;
    }


    private String moveNegationInward(String sentence) {
        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '~' && sentence.charAt(i + 1) == '~') {
                sentence = sentence.substring(0, i) + sentence.substring(i + 2);
            }
            if (sentence.charAt(i) == '~' && sentence.charAt(i + 1) == '(') {
                sentence = sentence.substring(0, i) + "(~" + sentence.substring(i + 2);
                int j = i + 1;
                int nestedBrackets = 0;
                boolean parentBracketOpen = true;
                while (parentBracketOpen) {
                    if (sentence.charAt(j) == '(') {
                        nestedBrackets++;
                    }
                    if (sentence.charAt(j) == ')' && nestedBrackets == 0) {
                        parentBracketOpen = false;
                    } else if (sentence.charAt(j) == ')') {
                        nestedBrackets--;
                    }
                    if (sentence.charAt(j) == '|' && nestedBrackets == 0) {
                        sentence = sentence.substring(0, j) + "&~" + sentence.substring(j + 1);
                    } else if (sentence.charAt(j) == '&' && nestedBrackets == 0) {
                        sentence = sentence.substring(0, j) + "|~" + sentence.substring(j + 1);
                    }
                    j++;
                }
            }
        }
        return sentence;
    }


    private String distributeOrOverAnd(String sentence) {
        if (sentence.isEmpty()) {
            return sentence;
        }
        if (sentence.charAt(0) == '(' && sentence.charAt(sentence.length() - 1) == ')') {
            sentence = sentence.substring(1, sentence.length() - 1);
        }
        if (sentence.split("&").length == 1) {
            return sentence;
        }

        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '|') {
                ArrayList<String> leftPredicates = new ArrayList<>();
                ArrayList<String> rightPredicates = new ArrayList<>();
                int leftPredicateIndex, rightPredicateIndex;
                if (sentence.charAt(i - 1) == ')') {
                    int left = i - 2;
                    int nestedBrackets = 1;
                    while (left >= 0) {
                        if (sentence.charAt(left) == '(') {
                            nestedBrackets--;
                            if (nestedBrackets == 0) {
                                break;
                            }
                        } else if (sentence.charAt(left) == ')') {
                            nestedBrackets++;
                        }
                        left--;
                    }
                    leftPredicateIndex = left;
                    leftPredicates.addAll(
                            Arrays.asList(distributeOrOverAnd(sentence.substring(left + 1, i - 1)).split("&")));
                } else {
                    int left = i - 1;
                    while (left >= 0) {
                        if (sentence.charAt(left) == '(') {
                            break;
                        }
                        left--;
                    }
                    leftPredicateIndex = left + 1;
                    leftPredicates.add(sentence.substring(left + 1, i));
                }


                if (sentence.charAt(i + 1) == '(') {
                    int right = i + 2;
                    int nestedBrackets = 1;
                    while (right <= sentence.length() - 1) {
                        if (sentence.charAt(right) == ')') {
                            nestedBrackets--;
                            if (nestedBrackets == 0) {
                                break;
                            }
                        } else if (sentence.charAt(right) == '(') {
                            nestedBrackets++;
                        }
                        right++;
                    }

                    rightPredicateIndex = right;
                    rightPredicates.addAll(Arrays.asList(
                            distributeOrOverAnd(sentence.substring(i + 2, rightPredicateIndex)).split("&")));
                } else {
                    int right = i + 1;
                    while (right < sentence.length()) {
                        if (sentence.charAt(right) == ')') {
                            break;
                        }
                        right++;
                    }
                    rightPredicateIndex = right;
                    if (right + 1 >= sentence.length()) {
                        rightPredicateIndex = sentence.length();
                    }
                    rightPredicates.add(sentence.substring(i + 1, rightPredicateIndex));
                }


                String leftClause, rightClause;
                if (leftPredicateIndex == -1) {
                    leftClause = "";
                } else {
                    leftClause = sentence.substring(0, leftPredicateIndex);
                }
                if (rightPredicateIndex >= sentence.length()) {
                    rightClause = "";
                } else {
                    rightClause = sentence.substring(rightPredicateIndex);
                }

                StringBuilder demorgan = new StringBuilder();
                for (String leftPredicate : leftPredicates) {
                    for (String rightPredicate : rightPredicates) {
                        String newClause = leftPredicate + "|" + rightPredicate;
                        demorgan.append(newClause).append("&");
                    }
                }
                StringBuilder result = new StringBuilder();
                String[] splitClauses = demorgan.toString().split("&");
                for (int j = 0; j < splitClauses.length; j++) {
                    if (!splitClauses[j].equals("")) {
                        result.append(splitClauses[j]);
                        if (j != splitClauses.length - 1) {
                            result.append("&");
                        }
                    }
                }
                sentence = leftClause + result + rightClause;
                sentence = standardiseParenthesis(sentence);
            }
        }
        return sentence;
    }


    private void extractClauses(String sentence, int index) {
        HashMap<String, String> map = predicateMapList.get(index);
        String[] clauses = sentence.split("&");
        for (String clause : clauses) {
            StringBuilder predicateClause = new StringBuilder();
            String[] predicates = clause.split("\\|");
            for (String predicate : predicates) {
                predicate = predicate.trim();
                if (predicate.startsWith("(")) {
                    predicate = predicate.substring(1);
                }
                if (predicate.endsWith(")")) {
                    predicate = predicate.substring(0, predicate.length() - 1);
                }
                predicateClause.append(map.get(predicate)).append("|");
            }
            kb.addClause(predicateClause.toString().substring(0, predicateClause.length() - 1));
        }
    }


    private String standardiseVariables(String clause, int index) {
        int startIndex = -1;
        boolean isVariable = false;
        for (int i = 0; i < clause.length(); i++) {
            if (clause.charAt(i) == '(') {
                startIndex = i + 1;
                isVariable = true;
            } else if (clause.charAt(i) == ')') {
                if (isVariable) {
                    int j = startIndex - 1;
                    while (j <= i) {
                        if (clause.charAt(j) == ',' || clause.charAt(j) == ')') {
                            if (clause.charAt(j) == ')') {
                                isVariable = false;
                            }
                            int varStartIndex = j - 1;
                            while (varStartIndex >= 0) {
                                if (clause.charAt(varStartIndex) == ',' || clause.charAt(varStartIndex) == '(') {
                                    break;
                                }
                                varStartIndex--;
                            }
                            if (clause.charAt(varStartIndex + 1) >= 96 && clause.charAt(varStartIndex + 1) <= 122) {
                                clause = clause.substring(0, j) + index + clause.substring(j);
                                j++;
                                i++;
                            }
                        }
                        j++;
                    }

                }
            } else if (clause.charAt(i) == '|') {
                isVariable = false;
            }
        }
        return clause;
    }


    private void populateKB(String clause) {
        ArrayList<String> predicates = new ArrayList<>();
        String[] predicateClauses = clause.split("\\|");
        for (String pClause : predicateClauses) {
            predicates.add(pClause.split("\\(")[0]);
        }
        for (String predicate : predicates) {
            kb.addToKbMap(predicate, clause);
        }
    }
}
