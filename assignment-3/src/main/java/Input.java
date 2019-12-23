import java.util.ArrayList;


/**
 * @author deepakjha on 11/15/19
 * @project ai-assignments
 */
public class Input {
    private int numberOfQueries;
    private ArrayList<String> queries = new ArrayList<>();
    private int numberOfSentencesInKb;
    private ArrayList<String> sentencesInKb = new ArrayList<>();

    public void setNumberOfQueries(final String numberOfQueries) {
        this.numberOfQueries = Integer.parseInt(numberOfQueries);
    }

    public int getNumberOfQueries() {
        return this.numberOfQueries;
    }

    public void addQuery(final String query) {
        queries.add(query);
    }

    public ArrayList<String> getQueries() {
        return this.queries;
    }

    public void setNumberOfSentencesInKb(final String numberOfSentencesInKb) {
        this.numberOfSentencesInKb = Integer.parseInt(numberOfSentencesInKb);
    }

    public int getNumberOfSentencesInKb() {
        return this.numberOfSentencesInKb;
    }

    public void addSentenceInKb(final String sentenceInKb) {
        sentencesInKb.add(sentenceInKb);
    }

    public ArrayList<String> getSentencesInKb() {
        return this.sentencesInKb;
    }

}
