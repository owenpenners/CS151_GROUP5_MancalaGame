/**
 * Implements the default basic mancala BoardDisplayStrategy
 * @author Owen Penners
 */

import java.awt.*;

public class DefaultConcreteBoardStrategy implements BoardDisplayStrategy{
    /**
     * Paints PitButton according to graphics context
     * @param g2 Graphics2D context
     * @param pit PitButton to paint
     */
    @Override
    public void paintPit(Graphics2D g2, PitButton pit) {
        configurePitButton(pit);
        // Write pit label
        String pitString = (pit.getPlayer().equals("P1") ? "A": "B") + (pit.getPit()+1);
        g2.drawString(pitString,20, 20);

        drawStones(g2, pit.getStones(), pit.getWidth(), pit.getHeight());
    }

    /**
     * Paints Mancala Store according to graphics context
     * @param g2 Graphics2D context
     * @param store MancalaStoreComponent to paint
     */
    @Override
    public void paintStore(Graphics2D g2, MancalaStoreComponent store) {
        drawStones(g2, store.getStones(), store.getWidth(), store.getHeight());
    }

    @Override
    public void configurePitButton(PitButton pit) {
        pit.setContentAreaFilled(true);
        pit.setBorderPainted(true);
        pit.setFocusPainted(true);
        pit.setOpaque(true);
    }

    /**
     * Draws a certain amount of stones
     * @param g2
     * @param stones
     * @param width
     * @param height
     */
    private void drawStones(Graphics2D g2, int stones, int width,  int height) {
        if(stones == 0) return;

        int stoneSize = 20;
        int padding = 8;
        int cols = (int) Math.ceil(Math.sqrt(stones));
        int rows = (int) Math.ceil((double) stones / cols);

        int gridW = (width - 2 * padding) / cols;
        int gridH = (height - 2 * padding) / rows;

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
