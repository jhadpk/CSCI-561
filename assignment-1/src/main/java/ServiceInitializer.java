import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;


/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public class ServiceInitializer {
    private static HashMap<SearchType, String> sMap = new HashMap<>();


    public static void init() {
        try {
            Class.forName(BFSAdapter.class.getCanonicalName());
            Class.forName(UCSAdapter.class.getCanonicalName());
            Class.forName(AStarAdapter.class.getCanonicalName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();

        }
        sMap.put(SearchType.BFS, BFSAdapter.class.getCanonicalName());
        sMap.put(SearchType.UCS, UCSAdapter.class.getCanonicalName());
        sMap.put(SearchType.AStar, AStarAdapter.class.getCanonicalName());
    }


    /**
     * Returns the search adapter for a specified type
     *
     * @param input using which search adapter is to be found.
     * @return Fully qualified classname for the search adapter for the type
     */
    public static SearchService getSearchAdapter(final Input input)
            throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException,
            InvocationTargetException {
        String className = getAdapterFor(input.getSearchType());
        if (null != className) {
            Class<?> adapterClass = Class.forName(className);
            return (SearchService) adapterClass.getConstructor(Input.class).newInstance(input);
        }
        return null;
    }


    private static String getAdapterFor(final SearchType searchType) {
        return sMap.get(searchType);
    }
}
