import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author deepakjha on 11/15/19
 * @project ai-assignments
 */
public class Controller {
    private static final String INPUT_FILE = "/Users/deepakjha/input.txt";
    private static final String OUTPUT_FILE = "/Users/deepakjha/output.txt";
    private static final String NEW_LINE = "\n";
    private static final String NO_OUTPUT = "";

    private KnowledgeBase kb = new KnowledgeBase();

    protected void run() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE));
            Input input = readInput(br);
            if (null != input) {
                new CNFConverter(kb).convertToCnfAndPopulateKb(input.getSentencesInKb());
                ArrayList<Boolean> results = new ArrayList<>();
                for (String query : input.getQueries()) {
                    boolean result = new ResolutionEngine(kb.getKbMap(), kb.getClauseSet()).resolve(query);
                    results.add(result);
                    if (result) {
                        kb.getClauseSet().add(query);
                        kb.addToKbMap(query.split("\\(")[0], query);
                    }
                }
                writeOutput(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private Input readInput(final BufferedReader br) {
        try {
            Input input = new Input();
            input.setNumberOfQueries(br.readLine());
            for (int i = 0; i < input.getNumberOfQueries(); i++) {
                input.addQuery(br.readLine().trim());
            }
            input.setNumberOfSentencesInKb(br.readLine());
            for (int i = 0; i < input.getNumberOfSentencesInKb(); i++) {
                input.addSentenceInKb(br.readLine().trim());
            }
            return input;
        } catch (IOException e) {
            return null;
        }
    }


    private void writeOutput(final ArrayList<Boolean> results) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(OUTPUT_FILE, false);
            if (results.size() != 0) {
                StringBuilder output = new StringBuilder();
                for (Boolean result : results) {
                    output.append(result.toString().toUpperCase()).append(NEW_LINE);
                }
                fw.write(output.substring(0, output.toString().length() - 1));
            } else {
                fw.write(NO_OUTPUT);
            }
        } catch (IOException e) {
            try {
                System.out.println("IOException occurred");
                fw.write(NO_OUTPUT);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {fw.close();} catch (Exception ex) {System.out.println("Exception in closing fw");}
        }
    }
}
