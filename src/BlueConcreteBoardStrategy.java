import java.awt.*;
import java.awt.geom.Path2D;

public class BlueConcreteBoardStrategy implements BoardDisplayStrategy{
    private final Color BACKGROUND_COLOR = new Color(49, 105, 168);
    @Override
    public void paintPit(Graphics2D g2, PitButton pit) {
        configurePitButton(pit);
        g2.setColor(BACKGROUND_COLOR);
        Path2D.Double diamond = new Path2D.Double();

        diamond.moveTo((double) pit.getWidth()/2, 0);
        diamond.lineTo(pit.getWidth(), (double) pit.getHeight()/2);
        diamond.lineTo((double) pit.getWidth() /2, (double)pit.getHeight());
        diamond.lineTo(0, (double) pit.getHeight()/2);
        diamond.closePath();

        g2.fill(diamond);


        g2.setColor(Color.BLACK);
        g2.draw(diamond);

        Shape oldClip = g2.getClip();
        g2.setClip(diamond);
        drawStones(g2, pit.getStones(), pit.getWidth(), pit.getHeight(), Color.WHITE);
        g2.setClip(oldClip);

    }

    @Override
    public void paintStore(Graphics2D g2, MancalaStoreComponent store) {
        g2.setColor(BACKGROUND_COLOR);
        g2.fillRoundRect(8, 8, store.getWidth() - 12, store.getHeight()- 12, 30, 30);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(8, 8, store.getWidth() - 12, store.getHeight()- 12, 30, 30);

        drawStones(g2, store.getStones(), store.getWidth(), store.getHeight(), Color.WHITE);
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
     * @param color - A Color to color the stones
     */
    private void drawStones(Graphics2D g2, int stones, int width, int height, Color color) {
        if (stones == 0) return;

        g2.setColor(color);

        int stoneSize = 20;
        int gap = 4;

        int cols = (int) Math.ceil(Math.sqrt(stones));
        int rows = (int) Math.ceil((double) stones / cols);

        int totalWidth = cols * stoneSize + (cols - 1) * gap;
        int totalHeight = rows * stoneSize + (rows - 1) * gap;

        int startX = (width - totalWidth) / 2;
        int startY = (height - totalHeight) / 2;

        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (count >= stones) return;

                int x = startX + c * (stoneSize + gap);
                int y = startY + r * (stoneSize + gap);

                g2.fillOval(x, y, stoneSize, stoneSize);
                count++;
            }
        }
    }
}
