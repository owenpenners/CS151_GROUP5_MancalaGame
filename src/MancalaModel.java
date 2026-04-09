import java.util.ArrayList;

public class MancalaModel {
    // constants
    private final static int DEFAULT_STARTING_STONES = 4;
    private final static int DEFAULT_HOLES_PER_SIDE = 6;

    // state of the board
    private final int startingStones;
    private final int numHoles;
    private int endP1 = 0;
    private int endP2 = 0;
    final private ArrayList<Integer> sideP1 = new ArrayList<>();
    final private ArrayList<Integer> sideP2 = new ArrayList<>();

    public MancalaModel() {
        this.startingStones = DEFAULT_STARTING_STONES;
        this.numHoles = DEFAULT_HOLES_PER_SIDE;
        this.initBoard();
    }

    /**
     * Initialize the board; allows for board resetting. <p>
     * Postcondition: Clears the stones from the end (sets to 0 stones) and fills all the
     * stones to 0.
     */
    public void initBoard() {
        this.endP1 = 0; this.endP2 = 0;
        this.sideP1.clear(); this.sideP2.clear();
        for (int i = 0; i < this.numHoles; i++) {
            this.sideP1.add(this.startingStones);
            this.sideP2.add(this.startingStones);
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
        if (hole_number < 1 || hole_number > this.numHoles) {
            throw new IllegalArgumentException("Invalid hole number #" + hole_number +
                    "; must be between 1 and " + this.numHoles + " inclusive.");
        }

        return switch (player) {
            case 1 -> this.sideP1.get(hole_number);
            case 2 -> this.sideP2.get(hole_number);
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
            case 1 -> this.endP1;
            case 2 -> this.endP2;
            default -> throw new IllegalArgumentException("Invalid player argument; must be 1 or 2.");
        };
    }
}
