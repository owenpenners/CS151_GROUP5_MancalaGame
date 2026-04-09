public class MancalaModel {
    private static int DEFAULT_STARTING_STONES = 4;
    private static int HOLES_PER_SIDE = 6;

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
     * 1-indexed.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @param hole_number {@code int} range [1, 6 or HOLES_PER_SIDE]
     * @return {@code int} stones in that hole.
     */
    public int getStones(int player, int hole_number) {
        if (player != 1 && player != 2)
            throw new IllegalArgumentException("Invalid player argument; must be 1 or 2.");
        if (hole_number < 1 || hole_number > HOLES_PER_SIDE) {
            throw new IllegalArgumentException("Invalid hole number #" + hole_number +
                    "; must be between 1 and " + HOLES_PER_SIDE + " inclusive.");
        }
    }
}
