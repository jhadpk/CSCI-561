/**
 * @author deepakjha on 10/13/19
 * @project ai-assignments
 */
public enum PlayerType {
    BLACK("B", "BLACK"),
    WHITE("W", "WHITE"),
    NONE(".", "NONE");

    private final String player;
    private final String color;

    PlayerType(String player, String color) {
        this.player = player;
        this.color = color;
    }

    public static PlayerType getPlayerByValue(final String player) {
        for (PlayerType pt : PlayerType.values()) {
            if (pt.player.equals(player)) {
                return pt;
            }
        }
        return null;
    }

    public static PlayerType getPlayerByColor(final String color) {
        for (PlayerType pt : PlayerType.values()) {
            if (pt.color.equals(color)) {
                return pt;
            }
        }
        return null;
    }
}
