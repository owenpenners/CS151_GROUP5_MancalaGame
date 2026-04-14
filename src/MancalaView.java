import javax.swing.*;
import java.awt.*;

public class MancalaView extends JFrame {
    JLabel leftMancala;
    JLabel rightMancala;

    private JButton[] player1PitButtons;
    private JButton[] player2PitButtons;

    public MancalaView() {
        super("Mancala Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        add(createStorePanel("Mancala A",0), BorderLayout.WEST);
        add(createStorePanel("Mancala B",0), BorderLayout.EAST);

        JPanel topRowPanel = new JPanel(new GridLayout(1,6));
        JPanel bottomRowPanel = new JPanel(new GridLayout(1,6));

        //Add list of JButtons to top / bottom panels
        player1PitButtons = createPitRow(topRowPanel);
        player2PitButtons = createPitRow(bottomRowPanel);

        //Add top/bottom panels to center panel for lay
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(topRowPanel, BorderLayout.NORTH);
        centerPanel.add(bottomRowPanel, BorderLayout.SOUTH);

        add(centerPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    /**
     * Create a panel that represents a mancala
     * @param title - The title of the mancala
     * @param stones - The number of stones in the store
     * @return a JPanel with a JLabel and a border displaying number of stones and title
     */
    private JPanel createStorePanel(String title, int stones) {
        JPanel storePanel = new JPanel(new BorderLayout());
        JLabel mancala = new JLabel(String.valueOf(stones), SwingConstants.CENTER);

        storePanel.setPreferredSize(new Dimension(120, 180));
        storePanel.setBorder(BorderFactory.createTitledBorder(title));
        storePanel.add(mancala,BorderLayout.CENTER);

        return storePanel;
    }

    /**
     * Create and attach a row of 6 JButtons representing pits to a JPanel
     * @param panel - The panel to attach the buttons to
     * @return a collection of JButtons
     */
    private JButton[] createPitRow(JPanel panel) {
        JButton[] buttons = new JButton[6];

        for (int i = 0; i < 6; i++) {
            buttons[i] = createPit(5);
            panel.add(buttons[i]);
        }
        return buttons;
    }

    private JButton createPit(int stones) {
        JButton pit = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;


                // Paint stones in
                if (stones == 0) return;
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
        };



        return pit;
    }
}
