import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MancalaModel {
    // mvc connections
    private ArrayList<ChangeListener> listeners = new ArrayList<>();

    // constants
    public enum Player {PLAYER_1, PLAYER_2}

    ;
    private final static int PITS_PER_SIDE = 6;

    // state of the board
    private Player currentPlayer;
    final private HashMap<Player, ArrayList<MancalaPit>> board = new HashMap<>();
    final private HashMap<Player, Integer> ends = new HashMap<>();

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
            ends.put(player, 0);
        }
    }

    /**
     *
     * @param list
     */
    public void addChangeListener(ChangeListener list) {
        this.listeners.add(list);
    }

    /**
     *
     */
    private void updateChangeListeners() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners)
            listener.stateChanged(event);
    }

    /**
     *
     * @param player
     * @return
     */
    private List<MancalaPit> getPits(Player player) {
        return this.board.get(player);
    }

    /**
     * Return the pit associated with the player and the pitNumber (0-indexed).
     *
     * @param player    {@link Player} either 1 or 2 representing Player 1 or Player 2.
     * @param pitNumber {@code int} range [0, 5 or PITS_PER_SIDE], 0-indexed.
     * @return {@code int} stones in that pit.
     * @throws IllegalArgumentException if pitNumber argument is invalid.
     */
    private MancalaPit getPit(Player player, int pitNumber) {
        if (pitNumber < 0 || pitNumber >= PITS_PER_SIDE) {
            throw new IllegalArgumentException("Invalid pit number #" + pitNumber +
                    "; must be between 0 and " + (PITS_PER_SIDE - 1) + " inclusive.");
        }

        return this.getPits(player).get(pitNumber);
    }

    /**
     *
     * @param player
     * @param pitNumber
     * @param stones
     */
    private void addStonesToPit(Player player, int pitNumber, int stones) {
        MancalaPit pit = this.getPit(player, pitNumber);
        pit.addStones(stones);
    }

    /**
     *
     * @param player
     * @return
     */
    private int sumPits(Player player) {
        int sum = 0;
        for (MancalaPit pit : this.getPits(player))
            sum += pit.getStones();
        return sum;
    }

    /**
     *
     * @param player
     * @return
     */
    public List<Integer> getStonesOfPits(Player player) {
        List<Integer> copy = new ArrayList<>();
        for (MancalaPit pit : board.get(player))
            copy.add(pit.getStones());
        return copy;
    }

    /**
     * Returns the stones that are in a pit from a player's side. pitNumber is 0-indexed.
     *
     * @param player    {@link Player} either 1 or 2 representing Player 1 or Player 2.
     * @param pitNumber {@code int} range [0, 5 or PITS_PER_SIDE], 0-indexed.
     * @return {@code int} stones in that pit.
     * @throws IllegalArgumentException if pitNumber argument is invalid.
     */
    public int getStonesFromPit(Player player, int pitNumber) {
        if (pitNumber < 0 || pitNumber >= PITS_PER_SIDE) {
            throw new IllegalArgumentException("Invalid pit number #" + pitNumber +
                    "; must be between 0 and " + (PITS_PER_SIDE - 1) + " inclusive.");
        }

        return switch (player) {
            case Player.PLAYER_1 -> this.board.get(Player.PLAYER_1).get(pitNumber).getStones();
            case Player.PLAYER_2 -> this.board.get(Player.PLAYER_2).get(pitNumber).getStones();
        };
    }

    /**
     *
     * @param player
     * @param stones
     */
    private void addStonesToEnd(Player player, int stones) {
        int newStoneCount = this.ends.get(player) + stones;
        this.ends.put(player, newStoneCount);
    }

    /**
     * Returns the stones in a player's end.
     *
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @return {@code int} stones from that player's end
     */
    public int getStonesFromEnd(Player player) {
        return this.ends.get(player);
    }

    /**
     * Returns the current player.
     *
     * @return {@code Player} whose turn it is.
     */
    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Swap players by calling the static getOtherPlayer function.
     * Postcondition: players are swapped for the game state.
     */
    private void swapPlayer() {
        this.currentPlayer = getOtherPlayer(this.currentPlayer);
    }

    /**
     * Static function that returns the opposite player.
     *
     * @param player {@code Player} initial player.
     * @return {@code Player} opposite player of the passed-in player.
     */
    public static Player getOtherPlayer(Player player) {
        return switch (player) {
            case Player.PLAYER_1 -> Player.PLAYER_2;
            case Player.PLAYER_2 -> Player.PLAYER_1;
        };
    }

    // GAME LOGIC

    /**
     * Overloaded method of {@link MancalaModel#moveStones(int pitNumber)}.
     *
     * @param player    {@link Player} checks if player is valid.
     * @param pitNumber {@code int} representing the pit to perform a mancala move on (0-indexed).
     * @throws IllegalArgumentException if not the right player's turn.
     * @throws IllegalArgumentException if the pit selected has no stones in it.
     */
    public void moveStones(Player player, int pitNumber) {
        //Check whether current player is making move
        if (player != this.currentPlayer) throw new IllegalArgumentException("Wrong player");
        this.moveStones(pitNumber);
        this.updateChangeListeners();
    }

    /**
     * Given a selected pit number of the current player's turn, grab all the stones and distribute it across
     * the pits. If the last mancala was dropped in a pit belonging to the player, take another turn. If it lands
     * in an empty hole, and the opponent has stones in the opposing pit, collect both stones.
     *
     * @param pitNumber {@code int} representing the pit to perform a mancala move on (0-indexed)
     * @throws IllegalArgumentException if pit selected has no stones in it
     */
    private void moveStones(int pitNumber) throws IllegalArgumentException {
        if (this.getStonesFromPit(this.currentPlayer, pitNumber) == 0)
            throw new IllegalArgumentException("Hole is empty");

        if (pitNumber < 0 || pitNumber >= PITS_PER_SIDE)
            throw new IllegalArgumentException("Out of bounds pit number.");

        /*
         * Advance to next pit, then deposit stone if it is a valid pit.
         * Upon reaching the mancala/end: deposit a stone, rotate sides, then repeat depositing stones
         * on opponent's pits.
         */

        int grabbedStones = this.getPit(currentPlayer, pitNumber).grabStones();
        Player pitSideIter = this.currentPlayer;
        while (grabbedStones > 0) {
            pitNumber++;

            // place stone in pit
            if (pitNumber < PITS_PER_SIDE) {
                grabbedStones--;
                this.addStonesToPit(pitSideIter, pitNumber, 1);
                continue;
            }

            // mancala end has been reached
            pitNumber = -1; //let -1 mean the end of the board
            // if it is the current player's side / mancala, put stone in
            if (pitSideIter == this.currentPlayer) {
                grabbedStones--;
                this.addStonesToEnd(this.currentPlayer, 1);
            }
            // rotate to other side and position pitNumber at the first
            pitSideIter = getOtherPlayer(pitSideIter);
        }

        // end game if no more stones on side
        this.endGameIfEmptySide();

        // free turn if turn ends in the Mancala end
        if (pitNumber == -1) {
            return;
        }

        // pass turn to opponent if last stone placed was on opponent side
        if (this.currentPlayer != pitSideIter) {
            this.swapPlayer();
            return;
        }

        // currently, last stone placed was on your side, so pitSideIter == currentPlayer
        int opposite_pitNumber = PITS_PER_SIDE - pitNumber - 1; // calculate the reciprocal pit
        MancalaPit currentPit = this.getPit(this.currentPlayer, pitNumber);
        MancalaPit opponentPit = this.getPit(getOtherPlayer(this.currentPlayer), opposite_pitNumber);

        // pass turn if it lands on your side but not an empty pit or opponent pit is empty
        if (currentPit.getStones() > 1 || opponentPit.getStones() == 0) {
            this.swapPlayer();
            return;
        }

        // collect stones in both pits and place them in endPit if you did land on an empty pit side,
        // and opponent has stones in reciprocal pit
        this.addStonesToEnd(this.currentPlayer, currentPit.grabStones());
        this.addStonesToEnd(this.currentPlayer, opponentPit.grabStones());

        // resolve empty side, then pass turn.
        this.endGameIfEmptySide();
        this.swapPlayer();
    }

    /**
     *
     */
    private void endGameIfEmptySide() {
        // end game if no more stones on the current side, then opponent collects all their stones
        // and game ends
        Player currPlayer = this.currentPlayer;
        Player oppPlayer = getOtherPlayer(this.currentPlayer);

        if (this.sumPits(currPlayer) == 0) {
            for (int i = 0; i < 6; i++) {
                try {
                    int stones = this.getPit(oppPlayer, i).grabStones();
                    this.ends.put(oppPlayer, this.ends.get(oppPlayer) + stones);
                } catch (IllegalArgumentException _) {
                }
                ;
            }
        }

        if (this.sumPits(oppPlayer) == 0) {
            for (int i = 0; i < 6; i++) {
                try {
                    int stones = this.getPit(currPlayer, i).grabStones();
                    this.ends.put(currPlayer, this.ends.get(currPlayer) + stones);
                } catch (IllegalArgumentException _) {
                }
                ;
            }
        }
    }

    /**
     * Fill all pits in with the starting stones.
     * Postcondition: Clears the stones from the end (sets to 0 stones) and fills all the
     * stones to 0.
     *
     * @param startingStones - initial amount of stones
     */
    public void fillPits(int startingStones) {
        for (Player player : board.keySet())
            for (MancalaPit pit : this.getPits(player)) {
                pit.reset();
                pit.addStones(startingStones);
            }

        updateChangeListeners();
    }

    /**
     * Clear out the stones from the pits.
     */
    public void clearBoard() {
        currentPlayer = Player.PLAYER_1;
        for (Player player : board.keySet()) {
            for (MancalaPit pit : board.get(player))
                pit.reset();
            ends.put(player, 0);
        }
        updateChangeListeners();
    }
}