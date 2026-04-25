import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    // record to save state / return state of board
    public record MancalaRecord(boolean gameOver, Player currentPlayer, int undoLeft,
                                List<Integer> p1_side, List<Integer> p2_side,
                                int p1_end, int p2_end) {}

    /**
     * Construct the Mancala backend.
     * Postcondition: set the starting player to P1; construct the MancalaBoard with pits and the end mancalas.
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

        return new MancalaRecord(gameOver, currentPlayer, undoLeft, p1_side, p2_side, p1_end, p2_end);
    }

    /**
     *
     * @param record
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
     * Clear out the stones from the pits.
     */
    private void clearBoard() {
        for (Player player : board.keySet()) {
            for (MancalaPit pit : board.get(player))
                pit.setStones(0);
            ends.put(player, 0);
        }
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
     * @param stones
     */
    private void addStonesToEnd(Player player, int stones) {
        int newStoneCount = this.ends.get(player) + stones;
        this.ends.put(player, newStoneCount);
    }

    /**
     *
     * @param player
     * @param stones
     */
    private void setStonesOfEnd(Player player, int stones) {
        this.ends.put(player, stones);
    }

    /**
     * Returns the stones in a player's end.
     *
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
     *
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
     *
     * Precondition: assumes the game is not over.
     *
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