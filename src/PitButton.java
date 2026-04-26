import javax.swing.*;
import java.awt.*;

public class PitButton extends JButton {
    private int stones;
    private final String player;
    private final int pitNumber;
    private PitDisplayStrategy displayStrategy;

    /**
     * Create a PitButton with player and a pitNumber
     * @param player
     * @param pitNumber
     */
    public PitButton(String player, int pitNumber, PitDisplayStrategy displayStrategy) {
        this.player = player;
        this.pitNumber = pitNumber;
        this.stones = 0;
        this.displayStrategy = displayStrategy;
    }



    public String getPlayer() {
        return this.player;
    }

    public int getPit() {
        return this.pitNumber;
    }

    public void setStones(int stones) {
        this.stones = stones;
        repaint();
    }

    public int getStones() {return this.stones;}

    public void setDisplayStrategy(PitDisplayStrategy displayStrategy) {
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
