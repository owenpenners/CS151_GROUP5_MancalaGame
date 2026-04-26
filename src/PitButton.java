import javax.swing.*;
import java.awt.*;

public class PitButton extends JButton {
    private int stones;
    private final String player;
    private final int pitNumber;

    /**
     * Create a PitButton with player and a pitNumber
     * @param player
     * @param pitNumber
     */
    public PitButton(String player, int pitNumber) {
        this.player = player;
        this.pitNumber = pitNumber;
        this.stones = 0;
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

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        //Draw pit label
        String pitString = (getPlayer().equals("P1") ? "A": "B") + (pitNumber+1);
        g2.drawString(pitString,20, 20);

        if(stones == 0) return;

        int stoneSize = 20;
        int padding = 8;
        int cols = (int) Math.ceil(Math.sqrt(stones));
        int rows = (int) Math.ceil((double) stones / cols);

        int gridW = (getWidth() - 2 * padding) / cols;
        int gridH = (getHeight() - 2 * padding) / rows;

        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (count >= stones) break;
                int x = padding + c * gridW + (gridW - stoneSize) / 2;
                int y = padding + r * gridH + (gridH - stoneSize) / 2;

                g2.fillOval(x, y, stoneSize, stoneSize);
                count++;
            }
        }
    }
}
