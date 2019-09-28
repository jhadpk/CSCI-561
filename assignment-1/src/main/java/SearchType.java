/**
 * @author deepakjha on 9/14/19
 * @project ai-assignments
 */
public enum SearchType {
    BFS("BFS"),
    UCS("UCS"),
    AStar("A*");

    private final String searchTypeString;


    SearchType(final String searchTypeString) {this.searchTypeString = searchTypeString;}


    public static SearchType getById(final String searchId) {
        for (SearchType searchType : values()) {
            if (searchType.searchTypeString.equals(searchId)) { return searchType; }
        }
        return null;
    }
}
