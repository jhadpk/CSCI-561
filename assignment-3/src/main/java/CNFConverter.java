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
            setClauseSet(inputSentences.get(i), i);
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
            int opened = -1;
            if (sentence.charAt(i) >= 65 && sentence.charAt(i) <= 90) {
                int j = i;

                while (opened != 0) {
                    if (sentence.charAt(j) == '(') {
                        opened++;
                        if (opened == 0) {
                            opened = 1;
                            end = j;
                        }
                    }
                    if (sentence.charAt(j) == ')') {
                        opened--;
                    }
                    j++;
                }

                String predicate = sentence.substring(i, end);
                String value = sentence.substring(i, j);
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
                sentence = sentence.substring(0, end) + append + sentence.substring(j);
                i = end - 1;
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
        if (sentence.split("&").length == 1) {
            return sentence;
        }

        for (int i = 0; i < sentence.length(); i++) {
            if (sentence.charAt(i) == '|') {
                PredicateInfo left = getLeftPredicates(sentence, i);
                PredicateInfo right = getRightPredicates(sentence, i);
                String leftClause = left.getIndex() == -1 ? "" : sentence.substring(0, left.getIndex());
                String rightClause = right.getIndex() >= sentence.length() ? "" : sentence.substring(right.getIndex());
                if (leftClause.equals("(")) {
                    leftClause = "";
                }
                if (rightClause.equals(")")) {
                    rightClause = "";
                }

                String result;
                StringBuilder sb = new StringBuilder();
                for (String leftPredicate : left.getPredicates()) {
                    for (String rightPredicate : right.getPredicates()) {
                        sb.append(leftPredicate).append("|").append(rightPredicate).append("&");
                    }
                }
                result = sb.toString();
                if (result.charAt(result.length() - 1) == '&') {
                    result = result.substring(0, result.length() - 1);
                }
                sentence = leftClause + result + rightClause;
                sentence = standardiseParenthesis(sentence);
            }
        }
        return sentence;
    }


    private PredicateInfo getLeftPredicates(String sentence, int i) {
        ArrayList<String> leftPredicates = new ArrayList<>();
        int leftPredicateIndex;
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
            leftPredicates.addAll(Arrays.asList(distributeOrOverAnd(sentence.substring(left + 1, i - 1)).split("&")));
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
        return new PredicateInfo(leftPredicateIndex, leftPredicates);
    }


    private PredicateInfo getRightPredicates(String sentence, int i) {
        ArrayList<String> rightPredicates = new ArrayList<>();
        int rightPredicateIndex;
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
            rightPredicates.addAll(
                    Arrays.asList(distributeOrOverAnd(sentence.substring(i + 2, rightPredicateIndex)).split("&")));
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
        return new PredicateInfo(rightPredicateIndex, rightPredicates);
    }


    private void setClauseSet(String sentence, int index) {
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


    private String standardiseVariables(String rule, int index) {
        for (int i = 0; i < rule.length(); i++) {
            if (rule.charAt(i) == '(') {
                if (rule.charAt(i + 1) >= 96 && rule.charAt(i + 1) <= 122) {
                    rule = rule.substring(0, i + 2) + index + rule.substring(i + 2);
                }
            }
            if (rule.charAt(i) == ',') {
                if (rule.charAt(i + 1) >= 96 && rule.charAt(i + 1) <= 122) {
                    rule = rule.substring(0, i + 2) + index + rule.substring(i + 2);
                }
            }
        }
        return rule;
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
