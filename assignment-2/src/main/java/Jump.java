/**
 * @author deepakjha on 10/14/19
 * @project ai-assignments
 */
public class Jump {
    public final Cell parent;
    public final Cell current;


    public Jump(final Cell parent, final Cell current) {
        this.parent = parent;
        this.current = current;
    }


    public Cell getParent() {
        return this.parent;
    }


    public Cell getCurrent() {
        return this.current;
    }
}
