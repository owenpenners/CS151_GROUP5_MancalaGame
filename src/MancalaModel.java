import java.util.ArrayList;
import java.util.HashMap;

public class MancalaModel {
    // constants
    public enum Player {PLAYER_1, PLAYER_2};
    public boolean inProgress = false;
    private final static int HOLES_PER_SIDE = 6;

    // state of the board
    private Player currentPlayer;
    final private HashMap<Player, ArrayList<MancalaPit>> sides = new HashMap<>();

    /**
     *
     */
    public MancalaModel() {
        currentPlayer = Player.PLAYER_1;
        for (Player player : Player.values()) {
            sides.put(player, new ArrayList<MancalaPit>());
            for (int i = 0; i < HOLES_PER_SIDE; i++)
                sides.get(player).add(new MancalaPit());
            sides.get(player).add(new MancalaEnd());
        }
    }

    public void clearBoard() {
        currentPlayer = Player.PLAYER_1;
        inProgress = false;
        for (Player player: sides.keySet())
            for (MancalaPit pit : sides.get(player))
                pit.reset();
    }

    /**
     * Initialize the board; allows for board resetting. <p>
     * Postcondition: Clears the stones from the end (sets to 0 stones) and fills all the
     * stones to 0.
     */
    public void initBoard() {

    }

    /**
     * Returns the stones that are in a hole from a player's side. Holes are 1-indexed.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @param hole_number {@code int} range [0, 5 or HOLES_PER_SIDE].
     * @return {@code int} stones in that hole.
     * @throws IllegalArgumentException if player or hole_number arguments are invalid.
     */
    public int getStonesFromPit(Player player, int hole_number) {
        if (hole_number < 0 || hole_number >= HOLES_PER_SIDE) {
            throw new IllegalArgumentException("Invalid hole number #" + hole_number +
                    "; must be between 0 and " + (HOLES_PER_SIDE-1) + " inclusive.");
        }

        return switch (player) {
            case Player.PLAYER_1 -> this.sides.get(Player.PLAYER_1).get(hole_number).getStones();
            case Player.PLAYER_2 -> this.sides.get(Player.PLAYER_2).get(hole_number).getStones();
        };
    }

    /**
     * Returns the stones in a player's end.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @return {@code int} stones from that player's end
     * @throws IllegalArgumentException if player is invalid.
     */
    public int getStonesFromMancala(Player player) {
        int end = HOLES_PER_SIDE;
        return switch (player) {
            case Player.PLAYER_1 -> this.sides.get(Player.PLAYER_1).get(end).getStones();
            case Player.PLAYER_2 -> this.sides.get(Player.PLAYER_2).get(end).getStones();
        };
    }

    /**
     *
     * @return
     */
    public Player getCurrentPlayer() { return this.currentPlayer; }

    public static Player swapPlayer(Player player) {
        return switch (player) {
            case Player.PLAYER_1 -> Player.PLAYER_2;
            case Player.PLAYER_2 -> Player.PLAYER_1;
        };
    }

    /**
     * @throws IllegalArgumentException if hole selected has no stones in it
     */
    public void moveStones(int hole_number) throws IllegalArgumentException {
        int end = HOLES_PER_SIDE;
        int grabbedStones = this.sides.get(this.currentPlayer).get(hole_number).grabStones();
        Player currentSide = this.currentPlayer;
        /*
         * Advance to next pit, then deposit stone if it is a valid pit.
         * Upon reaching the end: deposit a stone, then continue depositing stones
         * on opponent's pits.
         */
        while (grabbedStones > 0) {
            ++hole_number;
            grabbedStones -= 1;
            this.sides.get(currentSide).get(end).addStone();

            if (hole_number == end && grabbedStones != 0) {
                currentSide = swapPlayer(currentSide);
                hole_number %= HOLES_PER_SIDE;
            }
        }

        // free turn if turn ends in the pit
        if (hole_number == end)
            return;

        // pass turn to opponent
        this.currentPlayer = swapPlayer(this.currentPlayer);
        if (this.currentPlayer == currentSide)
            return;

        // from here on out, currentPlayer is the opponent, currentSide is the current player
        // pass turn if it lands on your side but not an empty pit or opponent pit is empty
        MancalaPit currentPit = this.sides.get(currentSide).get(hole_number);
        int opposite_hole_number = HOLES_PER_SIDE - hole_number - 1;
        MancalaPit opponentPit = this.sides.get(this.currentPlayer).get(opposite_hole_number);

        if (currentPit.getStones() > 1 || opponentPit.getStones() == 0)
            return;

        // collect stones in both pits
        MancalaPit endPit = this.sides.get(this.currentPlayer).get(end);
        endPit.addStone(currentPit.grabStones());
        endPit.addStone(opponentPit.grabStones());
    }
}
