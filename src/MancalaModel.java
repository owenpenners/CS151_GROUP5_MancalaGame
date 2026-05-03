import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Model class that represents the backend of a Mancala game.
 * <p>
 * Constructor:
 */
public class MancalaModel {
    // mvc connections
    private final ArrayList<ChangeListener> listeners = new ArrayList<>();

    // constants
    public enum Player {PLAYER_1, PLAYER_2}
    private final static int PITS_PER_SIDE = 6;
    private final int MAX_NUMBER_OF_UNDOS = 3;

    // state of the board
    private boolean gameOver = true;
    private Player currentPlayer = Player.PLAYER_1;;
    private MancalaRecord previousState = null;
    private int undoCount = 0;
    final private HashMap<Player, ArrayList<MancalaPit>> board = new HashMap<>();
    final private HashMap<Player, Integer> ends = new HashMap<>();

    /**
     * MancalaRecord is an immutable object that records information about the state of the board. Used to
     * return the state of the model, or the save the current state of the model.
     * @param gameOver {@code boolean} true if the mancala game is over.
     * @param currentPlayer {@link Player} enum type Player of MancalaModel.
     * @param undoLeft {@code int} number of undo left to the current player.
     * @param p1_side {@link List} of {@code int} representing the stones in the pits on P1's side.
     * @param p2_side {@link List} of {@code int} representing the stones in the pits on P2's side.
     * @param p1_end {@code int} repr. the stones in the mancala on P1's side.
     * @param p2_end {@code int} repr. the stones in the mancala on P2's side.
     */
    public record MancalaRecord(boolean gameOver, Player currentPlayer, int undoLeft,
                                List<Integer> p1_side, List<Integer> p2_side,
                                int p1_end, int p2_end) {}

    /**
     * Construct the Mancala backend; for both Player1 and Player2, create sides, and populate them with
     * empty {@link MancalaPit}, as well as instantiating empty ends (mancalas).
     */
    public MancalaModel() {
        for (Player player : Player.values()) {
            board.put(player, new ArrayList<>());
            for (int i = 0; i < PITS_PER_SIDE; i++)
                board.get(player).add(new MancalaPit());
            ends.put(player, 0);
        }
    }

    /**
     * Connect a change listener to this model object.
     * @param listener {@link ChangeListener} to add to the listeners to update when a change occurs.
     */
    public void addChangeListener(ChangeListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Send an update event to the change listeners stored in the model.
     */
    private void updateChangeListeners() {
        ChangeEvent event = new ChangeEvent(this);
        for (ChangeListener listener : listeners)
            listener.stateChanged(event);
    }

    /**
     * Generate a {@link MancalaRecord} object, representing the current state of the Mancala board.
     * @return the state of the board as a {@link MancalaRecord}
     */
    public MancalaRecord getRecord() {
        List<Integer> p1_side = new ArrayList<>();
        for (MancalaPit pit : this.getPits(Player.PLAYER_1))
            p1_side.add(pit.getStones());

        List<Integer> p2_side = new ArrayList<>();
        for (MancalaPit pit : this.getPits(Player.PLAYER_2))
            p2_side.add(pit.getStones());

        int p1_end = this.getStonesFromEnd(Player.PLAYER_1);
        int p2_end = this.getStonesFromEnd(Player.PLAYER_2);

        int undoLeft = MAX_NUMBER_OF_UNDOS - undoCount;

        // ensure immutable lists
        return new MancalaRecord(gameOver, currentPlayer, undoLeft, List.copyOf(p1_side), List.copyOf(p2_side),
                p1_end, p2_end);
    }

    /**
     * Using a MancalaRecord, paste/load the state of the game board into the Mancala board.
     * @param record {@link MancalaRecord}
     */
    private void pasteState(MancalaRecord record) {
        for (Player player : Player.values()) {
            List<Integer> side; int end = switch (player) {
                case PLAYER_1 -> {
                    side = record.p1_side;
                    yield record.p1_end;
                }
                case PLAYER_2 -> {
                    side = record.p2_side;
                    yield record.p2_end;
                }
            };
            for (int i = 0; i < PITS_PER_SIDE; i++)
                this.getPit(player, i).setStones(side.get(i));
            this.setStonesOfEnd(player, end);
        }
        this.currentPlayer = record.currentPlayer;
    }

    /**
     * Clear out the stones from the pits and end for both players. Used in conjunction with
     * {@link MancalaModel#newGame(int)} to generate a new board.
     */
    private void clearBoard() {
        for (Player player : board.keySet()) {
            for (MancalaPit pit : board.get(player))
                pit.setStones(0);
            ends.put(player, 0);
        }
    }

    /**
     * Return a list of MancalaPits belonging to a certain player.
     * (Shorten the syntax of this.board.get(player) to this.getPits(player))
     * @param player {@code Player} Player1 or Player2
     * @return {@code List} of {@code MancalaPit} representing the pits on that player's side.
     */
    private List<MancalaPit> getPits(Player player) {
        return this.board.get(player);
    }

    /**
     * Return the pit associated with the player and the pitNumber (0-indexed).
     * (Shorten the syntax of this.board.get(player).get(pitNumber) to this.getPit(player, pitNumber))
     * @param player {@link Player} either 1 or 2 representing Player 1 or Player 2.
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
     * Return the sum of the pits on a players side. Used for {@link MancalaModel#endGame()} in order to
     * calculate how much stones are to be added to the mancala at the end of a game.
     * @param player {@link Player} representing the player's side to sum.
     * @return {@code int} representing the total amount of stones in the pits of that side.
     */
    private int sumPits(Player player) {
        int sum = 0;
        for (MancalaPit pit : this.getPits(player))
            sum += pit.getStones();
        return sum;
    }

    /**
     * Increases the end stone count of a player's mancala.
     * @param player {@link Player} repr. the side to increment the side.
     * @param stones {@code int} repr. the stones to add to the mancala.
     */
    private void addStonesToEnd(Player player, int stones) {
        int newStoneCount = this.ends.get(player) + stones;
        this.ends.put(player, newStoneCount);
    }

    /**
     * Alternative method to {@link MancalaModel#addStonesToEnd(Player, int)} to change end stones.
     * Currently used to set end stones for {@link MancalaModel#pasteState(MancalaRecord)}
     * @param player {@link Player} repr. the side to increment the side.
     * @param stones {@code int} repr. the stones to set mancala count to.
     */
    private void setStonesOfEnd(Player player, int stones) {
        this.ends.put(player, stones);
    }

    /**
     * Returns the stones in a player's end.
     * @param player {@code int} either 1 or 2 representing Player 1 or Player 2.
     * @return {@code int} stones from that player's end
     */
    private int getStonesFromEnd(Player player) {
        return this.ends.get(player);
    }

    /**
     * Swap players by calling the static getOtherPlayer function. Refreshes Undo Limit.
     * Postcondition: players are swapped for the game state.
     */
    private void swapPlayer() {
        this.currentPlayer = getOtherPlayer(this.currentPlayer);
        this.undoCount = 0;
    }

    /**
     * Static function that returns the opposite player.
     * @param player {@code Player} initial player.
     * @return {@code Player} opposite player of the passed-in player.
     */
    private static Player getOtherPlayer(Player player) {
        return switch (player) {
            case Player.PLAYER_1 -> Player.PLAYER_2;
            case Player.PLAYER_2 -> Player.PLAYER_1;
        };
    }

    // GAME LOGIC
    /**
     * Given a selected pit number of the current player's turn, grab all the stones and distribute it across
     * the pits. If the last mancala was dropped in a pit belonging to the player, take another turn. If it lands
     * in an empty hole, and the opponent has stones in the opposing pit, collect both stones.
     * <p>
     * Precondition: assumes the game is not over.
     * @param pitNumber {@code int} representing the pit to perform a mancala move on (0-indexed)
     * @throws IllegalArgumentException if pit selected has no stones in it.
     * @throws IllegalArgumentException if out-of-bounds pit is selected.
     */
    private void moveStones(int pitNumber) throws IllegalArgumentException {
        if (this.getPit(this.currentPlayer, pitNumber).getStones() == 0)
            throw new IllegalArgumentException("Invalid hole; hole contains no stones.");


        // save state of board
        this.previousState = this.getRecord();

        /*
         * Advance to next pit, then deposit stone if it is a valid pit.
         * Upon reaching the mancala/end: deposit a stone, rotate sides, then repeat depositing stones
         * on opponent's pits.
         */
        int grabbedStones = this.getPit(currentPlayer, pitNumber).grabStones();
        Player pitSide = this.currentPlayer;
        while (grabbedStones > 0) {
            pitNumber++;

            // place stone in pit
            if (pitNumber < PITS_PER_SIDE) {
                grabbedStones--;
                this.getPit(pitSide, pitNumber).addStone();
                continue;
            }

            // mancala end has been reached
            pitNumber = -1; //let -1 mean the end of the board
            // if it is the current player's side / mancala, put stone in
            if (pitSide == this.currentPlayer) {
                grabbedStones--;
                this.addStonesToEnd(this.currentPlayer, 1);
            }
            // rotate to other side and position pitNumber at the first
            pitSide = getOtherPlayer(pitSide);
        }
        this.endTurn(pitSide, pitNumber);
        if (this.checkGameOver()) endGame();
    }

    /**
     *
     * @param pitSide
     * @param currentPitNum
     */
    private void endTurn(Player pitSide, int currentPitNum) {
        // take another turn if turn ends in the Mancala end;
        if (currentPitNum == -1)
            return;

        // else pass turn to opponent if last stone placed was on opponent side
        if (this.currentPlayer != pitSide) {
            this.swapPlayer();
            return;
        }

        // currently, last stone placed was on your side. locate pit opposite to where the last stone was placed
        int opposite_pitNumber = PITS_PER_SIDE - currentPitNum - 1; // calculate the reciprocal pit
        MancalaPit currentPit = this.getPit(this.currentPlayer, currentPitNum);
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
        this.swapPlayer();
    }

    /**
     *
     */
    private boolean checkGameOver() {
        for (Player player : Player.values())
            if (this.sumPits(player) == 0)
                return true;
        return false;
    }

    /**
     *
     */
    private void endGame() {
        for (Player player : Player.values()) {
            for (MancalaPit pit : this.getPits(player))
                this.addStonesToEnd(player, pit.grabStones());
        }
        this.gameOver = true;
        this.previousState = null; //wipe save
    }

    // VIEW METHODS
    /**
     * Fill all pits in with the starting stones.
     * Postcondition: Clears the stones from the end (sets to 0 stones) and fills all the
     * stones to 0.
     *
     * @param startingStones - initial amount of stones
     */
    public void newGame(int startingStones) {
        this.clearBoard();
        this.currentPlayer = Player.PLAYER_1;
        for (Player player : board.keySet()) {
            for (MancalaPit pit : this.getPits(player)) {
                pit.setStones(startingStones);
            }
        }
        this.gameOver = false;
        this.updateChangeListeners();
    }

    /**
     * Overloaded method of {@link MancalaModel#moveStones(int pitNumber)}.
     *
     * @param player {@link Player} checks if player is valid.
     * @param pitNumber {@code int} representing the pit to perform a mancala move on (0-indexed).
     * @throws IllegalArgumentException if not the right player's turn.
     * @throws IllegalArgumentException if the pit selected has no stones in it.
     */
    public void moveStones(Player player, int pitNumber) {
        if (this.gameOver)
            throw new IllegalStateException("Game is over.");
        if (player != this.currentPlayer)
            throw new IllegalArgumentException("Wrong player's turn.");
        this.moveStones(pitNumber);
        this.updateChangeListeners();
    }

    /**
     *
     */
    public void undo() {
        if (this.gameOver)
            throw new IllegalStateException("Game is over.");
        if (this.previousState == null)
            throw new IllegalStateException("Cannot undo.");
        if (this.undoCount >= this.MAX_NUMBER_OF_UNDOS)
            throw new IllegalStateException("No undo's left.");
        this.pasteState(this.previousState);
        this.undoCount += 1;
        this.previousState = null;
        this.updateChangeListeners();
    }
}