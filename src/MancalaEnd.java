/**
 * The end, or mancala, functionally is the same as a pit in Mancala, with the exception
 * that you cannot grab stones from the mancala.
 */
public class MancalaEnd extends MancalaPit {
    @Override
    public int grabStones() {
        throw new IllegalArgumentException("Cannot grab stones from the end.");
    }
}
