public class MancalaModel {
    // constants
    private static int DEFAULT_STARTING_STONES = 4;
    private static int HOLES_PER_SIDE = 6;

    // state of the board
    private int endP1 = 0;
    private int endP2 = 0;
    final private int[] sideP1 = new int[HOLES_PER_SIDE];
    final private int[] sideP2 = new int[HOLES_PER_SIDE];

    public MancalaModel() {
        initBoard();
    }

    public void initBoard() {
        endP1 = 0; endP2 = 0;
        for (int i = 0; i < HOLES_PER_SIDE; i++) {
            sideP1[i] = DEFAULT_STARTING_STONES;
            sideP2[i] = DEFAULT_STARTING_STONES;
        }
    }

    /**
     * Returns the stones that are in a hole from a player's side. Holes are 1-indexed.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @param hole_number {@code int} range [1, 6 or HOLES_PER_SIDE].
     * @return {@code int} stones in that hole.
     * @throws IllegalArgumentException if player or hole_number arguments are invalid.
     */
    public int getStonesFromHole(int player, int hole_number) {
        if (hole_number < 1 || hole_number > HOLES_PER_SIDE) {
            throw new IllegalArgumentException("Invalid hole number #" + hole_number +
                    "; must be between 1 and " + HOLES_PER_SIDE + " inclusive.");
        }

        return switch (player) {
            case 1 -> sideP1[hole_number];
            case 2 -> sideP2[hole_number];
            default -> throw new IllegalArgumentException("Invalid player argument; must be 1 or 2.");
        };
    }

    /**
     * Returns the stones in a player's end.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @return {@code int} stones from that player's end
     * @throws IllegalArgumentException if player is invalid.
     */
    public int getStonesFromEnd(int player) {
        return switch (player) {
            case 1 -> endP1;
            case 2 -> endP2;
            default -> throw new IllegalArgumentException("Invalid player argument; must be 1 or 2.");
        };
    }
}
