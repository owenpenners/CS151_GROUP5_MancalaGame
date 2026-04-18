import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class MancalaModel {
    // constants
    public enum Player {PLAYER_1, PLAYER_2};
    private final static int PITS_PER_SIDE = 6;

    // state of the board
    private Player currentPlayer;
    final private HashMap<Player, ArrayList<MancalaPit>> board = new HashMap<>();

    /**
     * Construct the Mancala backend.
     * Postcondition: set the starting player to P1; construct the MancalaBoard with pits and the end mancalas.
     */
    public MancalaModel() {
        currentPlayer = Player.PLAYER_1;
        for (Player player : Player.values()) {
            board.put(player, new ArrayList<>());
            for (int i = 0; i < PITS_PER_SIDE; i++)
                board.get(player).add(new MancalaPit());
            board.get(player).add(new MancalaEnd());
        }
    }

    /**
     * Clear out the stones from the pits.
     */
    public void clearBoard() {
        currentPlayer = Player.PLAYER_1;
        for (Player player: board.keySet())
            for (MancalaPit pit : board.get(player))
                pit.reset();
    }

    /**
     * Fill the pits in
     * Postcondition: Clears the stones from the end (sets to 0 stones) and fills all the
     * stones to 0.
     * @param startingStones - initial amount of stones
     */
    public void fillPits(int startingStones) {
        currentPlayer = Player.PLAYER_1;
        for (Player player: board.keySet())
            for (int i = 0; i < PITS_PER_SIDE; i++) { //ignore ends
                MancalaPit pit = board.get(player).get(i);
                pit.addStones(startingStones);
            }
    }

    /**
     * Return the pit associated with the player and the pitNumber (0-indexed).
     * @param player {@link Player} either 1 or 2 representing Player 1 or Player 2.
     * @param pitNumber {@code int} range [0, 5 or PITS_PER_SIDE], 0-indexed.
     * @return {@code int} stones in that pit.
     * @throws IllegalArgumentException if pitNumber argument is invalid.
     */
    public MancalaPit getPit(Player player, int pitNumber) {
        if (pitNumber < 0 || pitNumber >= PITS_PER_SIDE) {
            throw new IllegalArgumentException("Invalid pit number #" + pitNumber +
                    "; must be between 0 and " + (PITS_PER_SIDE-1) + " inclusive.");
        }

        return switch (player) {
            case Player.PLAYER_1 -> this.board.get(Player.PLAYER_1).get(pitNumber);
            case Player.PLAYER_2 -> this.board.get(Player.PLAYER_2).get(pitNumber);
        };
    }

    /**
     * Returns the stones that are in a pit from a player's side. pitNumber is 0-indexed.
     * @param player {@link Player} either 1 or 2 representing Player 1 or Player 2.
     * @param pitNumber {@code int} range [0, 5 or PITS_PER_SIDE], 0-indexed.
     * @return {@code int} stones in that pit.
     * @throws IllegalArgumentException if pitNumber argument is invalid.
     */
    public int getStonesFromPit(Player player, int pitNumber) {
        if (pitNumber < 0 || pitNumber >= PITS_PER_SIDE) {
            throw new IllegalArgumentException("Invalid pit number #" + pitNumber +
                    "; must be between 0 and " + (PITS_PER_SIDE-1) + " inclusive.");
        }

        return switch (player) {
            case Player.PLAYER_1 -> this.board.get(Player.PLAYER_1).get(pitNumber).getStones();
            case Player.PLAYER_2 -> this.board.get(Player.PLAYER_2).get(pitNumber).getStones();
        };
    }

    /**
     * Returns the stones in a player's end.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @return {@code int} stones from that player's end
     */
    public int getStonesFromMancala(Player player) {
        int end = PITS_PER_SIDE;
        return switch (player) {
            case Player.PLAYER_1 -> this.board.get(Player.PLAYER_1).get(end).getStones();
            case Player.PLAYER_2 -> this.board.get(Player.PLAYER_2).get(end).getStones();
        };
    }

    /**
     * Returns the current player.
     * @return {@code Player} whose turn it is.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Static function that returns the opposite player.
     * @param player {@code Player} initial player.
     * @return {@code Player} opposite player of the passed-in player.
     */
    public static Player swapPlayer(Player player) {
        return switch (player) {
            case Player.PLAYER_1 -> Player.PLAYER_2;
            case Player.PLAYER_2 -> Player.PLAYER_1;
        };
    }

    /**
     * Overloaded method of {@link MancalaModel#moveStones(int pitNumber)}.
     * @param player {@link Player} checks if player is valid.
     * @param pitNumber {@code int} representing the pit to perform a mancala move on (0-indexed).
     * @throws IllegalArgumentException if not the right player's turn.
     * @throws IllegalArgumentException if the pit selected has no stones in it.
     */
    public void moveStones(Player player, int pitNumber) {
        //Check whether current player is making move
        if (player != this.currentPlayer) throw new IllegalArgumentException("Wrong player");
        this.moveStones(pitNumber);
    }

    /**
     * Given a selected pit number of the current player's turn, grab all the stones and distribute it across
     * the pits. If the last mancala was dropped in a pit belonging to the player, take another turn. If it lands
     * in an empty hole, and the opponent has stones in the opposing pit, collect both stones.
     * @param pitNumber {@code int} representing the pit to perform a mancala move on (0-indexed)
     * @throws IllegalArgumentException if pit selected has no stones in it
     */
    public void moveStones(int pitNumber) throws IllegalArgumentException {
        if (this.getStonesFromPit(this.currentPlayer, pitNumber) == 0)
            throw new IllegalArgumentException("Hole is empty");

        int end = PITS_PER_SIDE;
        int grabbedStones = this.board.get(this.currentPlayer).get(pitNumber).grabStones();
        Player currentSide = this.currentPlayer;
        /*
         * Advance to next pit, then deposit stone if it is a valid pit.
         * Upon reaching the end: deposit a stone, then continue depositing stones
         * on opponent's pits.
         */
        while (grabbedStones > 0) {
            ++pitNumber;
            grabbedStones -= 1;
            this.board.get(currentSide).get(pitNumber).addStone();
            if (pitNumber == end && grabbedStones != 0) {
                currentSide = swapPlayer(currentSide);
                pitNumber = -1; //set to -1 before beginning next loop, which increments to 0.
            }
        }

        // free turn if turn ends in the pit
        if (pitNumber == end)
            return;

        // pass turn to opponent
        this.currentPlayer = swapPlayer(this.currentPlayer);
        if (this.currentPlayer == currentSide)
            return;

        // from here on out, currentPlayer is the opponent, currentSide is the current player
        // pass turn if it lands on your side but not an empty pit or opponent pit is empty
        MancalaPit currentPit = this.board.get(currentSide).get(pitNumber);
        int opposite_pitNumber = PITS_PER_SIDE - pitNumber - 1;
        MancalaPit opponentPit = this.board.get(this.currentPlayer).get(opposite_pitNumber);

        if (currentPit.getStones() > 1 || opponentPit.getStones() == 0)
            return;

        // collect stones in both pits
        MancalaPit endPit = this.board.get(this.currentPlayer).get(end);
        endPit.addStones(currentPit.grabStones());
        endPit.addStones(opponentPit.grabStones());
    }

    /**
     * Returns an integer array representing a players pit counts as seen on a mancala board
     * @param player
     * @return pit count of player
     */
    public int[] getPitCountsByPlayer(Player player) {
        System.out.println(player);
        int[] pitCount = new int[6];
        for(int i = 0; i < 6; i++) {
            pitCount[i] = this.getStonesFromPit(player, i);
        }
        if(player == Player.PLAYER_1) {
            System.out.println(Arrays.toString(pitCount));
        } else if(player == Player.PLAYER_2) {
            int[] reversePitCount = new int[6];
            for(int i = 0; i < 6; i++) {
                reversePitCount[i] = pitCount[5-i];
            }
            System.out.println(Arrays.toString(reversePitCount));
            return reversePitCount;
        }
        return pitCount;
    }
}