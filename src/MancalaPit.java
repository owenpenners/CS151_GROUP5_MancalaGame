/**
 * @author Quan Tang
 */
public class MancalaPit {
    private int stones = 0;

    /**
     * Return number of stones
     * @return
     */
    public int getStones() { return this.stones; }

    /**
     * Set number of stones
     * @param stones
     */
    public void setStones(int stones) { this.stones = stones; }

    /**
     * Add one stone
     */
    public void addStone() { this.stones++; }

    /**
     * Add a number of stones
     * @param stones - Number of stones to add
     */
    public void addStones(int stones) { this.stones += stones; }

    /**
     * Grab stones from a pit
     * @return number of stones in the pit
     */
    public int grabStones() {
        int grabbedStones = this.getStones();
        this.setStones(0);
        return grabbedStones;
    }
}
