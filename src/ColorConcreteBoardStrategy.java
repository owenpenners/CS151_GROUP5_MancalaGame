/**
 * Implements a colored in mancala BoardDisplayStrategy
 * @author Owen Penners
 */
import java.awt.*;

public class ColorConcreteBoardStrategy implements BoardDisplayStrategy{
    private final Color BACKGROUND_COLOR = new Color(225, 220, 160);
    /**
     * Paints PitButton according to graphics context
     * @param g2 Graphics2D context
     * @param pit PitButton to paint
     */
    @Override
    public void paintPit(Graphics2D g2, PitButton pit) {
        configurePitButton(pit);

        g2.setColor(BACKGROUND_COLOR);
        g2.fillRoundRect(5,5, pit.getWidth()-10, pit.getHeight()-10, 30, 30);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(5,5, pit.getWidth()-10, pit.getHeight()-10, 30, 30);
        // Write pit label
        String pitString = (pit.getPlayer().equals("P1") ? "A" : "B") + (pit.getPit() + 1);
        g2.drawString(pitString, 20, 20);

        drawStones(g2, pit.getStones(), pit.getWidth(), pit.getHeight(), Color.ORANGE);
    }
    /**
     * Paints Mancala Store according to graphics context
     * @param g2 Graphics2D context
     * @param store MancalaStoreComponent to paint
     */
    @Override
    public void paintStore(Graphics2D g2, MancalaStoreComponent store) {
        g2.setColor(BACKGROUND_COLOR);
        g2.fillOval(8, 8, store.getWidth() - 12, store.getHeight()- 12);

        g2.setColor(Color.BLACK);
        g2.drawOval(8, 8, store.getWidth()  - 12,  store.getHeight() - 12);

        drawStones(g2, store.getStones(), store.getWidth(), store.getHeight(), Color.ORANGE);
    }

    @Override
    public void configurePitButton(PitButton pit) {
        pit.setContentAreaFilled(false);
        pit.setBorderPainted(false);
        pit.setFocusPainted(false);
        pit.setOpaque(false);
    }

    /**
     * Draw a number of stones according to a Graphics context
     * @param g2
     * @param stones - int number of stones
     * @param width - int width
     * @param height - int height
     * @param stoneColor - A Color to color the stones
     */
    private void drawStones(Graphics2D g2, int stones, int width,  int height, Color stoneColor) {
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
                g2.setColor(stoneColor); // basic repaint
                g2.fillOval(x, y, stoneSize, stoneSize);
                count++;
            }
        }
    }
}
