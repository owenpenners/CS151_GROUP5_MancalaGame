/**
 * Implement a PitButton class which acts as a pit for the view
 * @author Owen Penners
 */

import javax.swing.*;
import java.awt.*;

public class PitButton extends JButton {
    private int stones;
    private final String player;
    private final int pitNumber;
    private BoardDisplayStrategy displayStrategy;

    /**
     * Create a PitButton with player and a pitNumber
     * @param player
     * @param pitNumber
     */
    public PitButton(String player, int pitNumber, BoardDisplayStrategy displayStrategy) {
        this.player = player;
        this.pitNumber = pitNumber;
        this.stones = 0;
        this.displayStrategy = displayStrategy;
    }

    /**
     * Get string representation of this PitButton's player
     * @return a String representing a plyer
     */
    public String getPlayer() {
        return this.player;
    }

    /**
     * Return pit number of this PitButton
     * @return an int representing pit number
     */
    public int getPit() {
        return this.pitNumber;
    }

    /**
     * Set the stones to a new value and repaints PitButton
     * @param stones - int value to set new stones to
     */
    public void setStones(int stones) {
        this.stones = stones;
        repaint();
    }

    /**
     * Get number of stones
     * @return int representing number of stones in PitButton
     */
    public int getStones() {return this.stones;}

    /**
     * Set the displayStrategy of this PitButton and repaint
     * @param displayStrategy a display strategy to use
     */
    public void setDisplayStrategy(BoardDisplayStrategy displayStrategy) {
        this.displayStrategy = displayStrategy;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(displayStrategy != null) {
            displayStrategy.paintPit(g2, this);
        }
    }
}
