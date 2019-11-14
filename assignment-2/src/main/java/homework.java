import java.io.IOException;


/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public class homework extends Controller {

    public static long START_TIME = System.currentTimeMillis();


    public static void main(String[] args) throws IOException {

        //new homework().play();

        for (int i = 0; i < 150; i++) {
            if (i == 0) {
                new homework().resetPlayground();
            }
            new homework().play();
            System.out.println(i);
        }
        System.out.println(System.currentTimeMillis() - START_TIME);
    }
}
