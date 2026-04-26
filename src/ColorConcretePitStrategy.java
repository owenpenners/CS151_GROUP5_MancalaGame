import java.awt.*;

public class ColorConcretePitStrategy implements PitDisplayStrategy{
    @Override
    public void paintPit(Graphics2D g2, PitButton pit) {
        // Write pit label
        String pitString = (pit.getPlayer().equals("P1") ? "A": "B") + (pit.getPit()+1);
        g2.drawString(pitString,20, 20);

        if(pit.getStones() == 0) return;

        int stoneSize = 20;
        int padding = 8;
        int cols = (int) Math.ceil(Math.sqrt(pit.getStones()));
        int rows = (int) Math.ceil((double) pit.getStones() / cols);

        int gridW = (pit.getWidth() - 2 * padding) / cols;
        int gridH = (pit.getHeight() - 2 * padding) / rows;

        int count = 0;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (count >= pit.getStones()) break;
                int x = padding + c * gridW + (gridW - stoneSize) / 2;
                int y = padding + r * gridH + (gridH - stoneSize) / 2;
                g2.setColor(Color.ORANGE); // basic repaint
                g2.fillOval(x, y, stoneSize, stoneSize);
                count++;
            }
        }
    }
}
