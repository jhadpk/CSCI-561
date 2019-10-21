import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


/**
 * @author deepakjha on 10/15/19
 * @project ai-assignments
 */
public class GameInitializer {

    private static HashMap<PlayerType, String> sMap = new HashMap<>();


    public static void init() {
        try {
            Class.forName(BlackPlayer.class.getCanonicalName());
            Class.forName(WhitePlayer.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        sMap.put(PlayerType.BLACK, BlackPlayer.class.getCanonicalName());
        sMap.put(PlayerType.WHITE, WhitePlayer.class.getCanonicalName());
    }


    /**
     * Returns the search adapter for a specified type
     *
     * @param input using which search adapter is to be found.
     * @return Fully qualified classname for the search adapter for the type
     */
    public static Player getPlayer(final Input input)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException,
            InvocationTargetException {
        String className = getAdapterFor(input.getPlayerType());
        if (null != className) {
            Class<?> adapterClass = Class.forName(className);
            return (Player) adapterClass.getConstructor(Input.class).newInstance(input);
        }
        return null;
    }


    private static String getAdapterFor(final PlayerType playerType) {
        return sMap.get(playerType);
    }
}
