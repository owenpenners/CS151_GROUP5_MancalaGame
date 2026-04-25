public class MancalaPit {
    private int stones = 0;

    /**
     *
     * @return
     */
    public int getStones() { return this.stones; }

    /**
     *
     * @param stones
     */
    public void setStones(int stones) { this.stones = stones; }

    /**
     *
     */
    public void addStone() { this.stones++; }

    /**
     *
     * @param stones
     */
    public void addStones(int stones) { this.stones += stones; }

    /**
     *
     * @return
     */
    public int grabStones() {
        int grabbedStones = this.getStones();
        this.setStones(0);
        return grabbedStones;
    }
}
