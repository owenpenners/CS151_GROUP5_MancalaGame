import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MancalaView extends JFrame {
    private JPanel player2Mancala;
    private JPanel player1Mancala;

    private PitButton[] player1PitButtons;
    private PitButton[] player2PitButtons;

    public MancalaView() {
        super("Mancala Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        player1Mancala = createStorePanel("Mancala A", 0);
        player2Mancala = createStorePanel("Mancala B", 0);

        add(player1Mancala, BorderLayout.EAST);
        add(player2Mancala, BorderLayout.WEST);

        JPanel topRowPanel = new JPanel(new GridLayout(1,6));
        JPanel bottomRowPanel = new JPanel(new GridLayout(1,6));

        //Add list of JButtons to top / bottom panels
        player2PitButtons = createPitRow(topRowPanel);
        player1PitButtons = createPitRow(bottomRowPanel);

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
    private PitButton[] createPitRow(JPanel panel) {
        PitButton[] buttons = new PitButton[6];

        for (int i = 0; i < 6; i++) {
            buttons[i] = createPitButton(0);
            panel.add(buttons[i]);
        }
        return buttons;
    }

    private JButton createPitJButton(int stones) {
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

    /**
     * Create a PitButton with given number of stones
     * @param stones
     * @return a PitButton with stones
     */
    private PitButton createPitButton(int stones) {
        return new PitButton(stones);
    }



    /**
     * Loop through player1 and player2 Pit buttons to add ActionListeners.
     * Sets the action command to "P1_" + index 0-5 representing the pit
     * @param listener - ActionListener to add
     */
    public void addPitListeners(ActionListener listener) {
        for(int i =0; i < 6; i++) {
            player1PitButtons[i].setActionCommand("P1_" + i);
            player1PitButtons[i].addActionListener(listener);

            player2PitButtons[i].setActionCommand("P2_" + i);
            player2PitButtons[i].addActionListener(listener);
        }
    }

    //public void updatePitCounts();
}
