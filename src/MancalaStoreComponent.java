/**
 * @author Owen Penners
 */

import javax.swing.*;
import java.awt.*;

public class MancalaStoreComponent extends JPanel {
    private int stones;
    private BoardDisplayStrategy strategy;

    /**
     * Create a MancalaStoreComponent
     * @param stones number of stones associated with this MancalaStore
     * @param strategy
     */
    public MancalaStoreComponent(int stones, BoardDisplayStrategy strategy) {
        this.stones = stones;
        this.strategy = strategy;
        setOpaque(false);
    }

    /**
     * Get the number of stones in this store
     */
    public int getStones() {return this.stones;}

    /**
     * Set the number of stones of this store
     * @param stones
     */
    public void setStones(int stones) {
        this.stones = stones;
        repaint();
    }

    /**
     * Set this StoreComponent's DisplayStrategy
     * @param strategy
     */
    public void setDisplayStrategy(BoardDisplayStrategy strategy) {
        this.strategy = strategy;
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.strategy != null) {
            strategy.paintStore((Graphics2D) g, this);
        }
    }

}
