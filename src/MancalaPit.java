public class MancalaPit {
    private int stones = 0;

    public int getStones() { return this.stones; }
    public void addStone() { this.stones++; }
    public void addStones(int stones) { this.stones += stones; }
    public void reset() { this.stones = 0; }

    public int grabStones() {
        int grabbedStones = this.stones;
        this.stones = 0;
        return grabbedStones;
    }

}
