import javax.swing.*;
import java.awt.*;

public class MancalaStoreComponent extends JPanel {
    private int stones;
    private BoardDisplayStrategy strategy; // TODO REPLACE WITH BOARD DISPLAY STRATEGY

    public MancalaStoreComponent(int stones, BoardDisplayStrategy strategy) {
        this.stones = stones;
        this.strategy = strategy;
        setOpaque(false);
    }

    public int getStones() {return this.stones;}

    public void setStones(int stones) {
        this.stones = stones;
        repaint();
    }

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
