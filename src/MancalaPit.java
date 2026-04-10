public class MancalaPit {
    private int stones = 0;

    public int getStones() { return this.stones; }
    public void addStone() { this.stones++; }
    public void addStone(int stones) { this.stones += stones; }
    public void reset() { this.stones = 0; }

    public int grabStones() throws IllegalArgumentException{
        if (this.stones == 0)
            throw new IllegalArgumentException("Hole is empty; cannot select this hole.");
        int grabbedStones = this.stones;
        this.stones = 0;
        return grabbedStones;
    }

}
