import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.List;

/**
 *
 */
public class MancalaView extends JFrame {
    private final String[] STYLES = {"Default Board", "Color Board"};
    private final PitDisplayStrategy DEFAULT_STRATEGY = new DefaultConcretePitStrategy();
    private final JPanel player2Mancala;
    private final JPanel player1Mancala;

    private final PitButton[] player1PitButtons;
    private final PitButton[] player2PitButtons;

    private final JButton undoButton;
    private final JButton newGameButton;

    private final JComboBox<String> styleChoiceComboBox;


    /**
     *
     */
    public MancalaView() {
        super("Mancala Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        int DEFAULT_END = 0;
        player1Mancala = createStorePanel("Mancala A", DEFAULT_END);
        player2Mancala = createStorePanel("Mancala B", DEFAULT_END);

        add(player1Mancala, BorderLayout.EAST);
        add(player2Mancala, BorderLayout.WEST);
        JPanel topRowPanel = new JPanel(new GridLayout(1,6));
        JPanel bottomRowPanel = new JPanel(new GridLayout(1,6));

        //Add list of JButtons to top / bottom panels
        player2PitButtons = createPitRow("P2", topRowPanel, true);
        player1PitButtons = createPitRow("P1", bottomRowPanel, false);

        //Add top/bottom panels to center panel for lay
        JPanel centerPanel = new JPanel(new GridLayout(2, 1));
        centerPanel.add(topRowPanel, BorderLayout.NORTH);
        centerPanel.add(bottomRowPanel, BorderLayout.SOUTH);

        //Add bottom panel
        JPanel bottomPanel = new JPanel();
        this.undoButton = new JButton("Undo");
        this.newGameButton = new JButton("New Game");
        this.styleChoiceComboBox = new JComboBox<>(STYLES);

        bottomPanel.add(undoButton);
        bottomPanel.add(newGameButton);
        bottomPanel.add(styleChoiceComboBox);

        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
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
    private PitButton[] createPitRow(String player, JPanel panel, boolean reverse) {
        PitButton[] buttons = new PitButton[6];

        for (int i = 0; i < 6; i++) {
            buttons[i] = createPitButton(player, i, this.DEFAULT_STRATEGY);
        }

        if (reverse) {
            for (int i = 5; i >= 0; i--)
                panel.add(buttons[i]);
        }
        else {
            for (int i = 0; i < 6; i++) {
                panel.add(buttons[i]);
            }
        }

        return buttons;
    }

//    private JButton createPitJButton(String player, int pitNumber, int stones) {
//        JButton pit = new JButton() {
//            public String player = stones;
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Graphics2D g2 = (Graphics2D) g;
//
//
//                // Paint stones in
//                if (stones == 0) return;
//                int stoneSize = 20;
//                int padding = 8;
//                int cols = (int) Math.ceil(Math.sqrt(stones));
//                int rows = (int) Math.ceil((double) stones / cols);
//
//                int gridW = (getWidth() - 2 * padding) / cols;
//                int gridH = (getHeight() - 2 * padding) / rows;
//
//                int count = 0;
//
//                for (int r = 0; r < rows; r++) {
//                    for (int c = 0; c < cols; c++) {
//                        if (count >= stones) break;
//                        int x = padding + c * gridW + (gridW - stoneSize) / 2;
//                        int y = padding + r * gridH + (gridH - stoneSize) / 2;
//
//                        g2.fillOval(x, y, stoneSize, stoneSize);
//                        count++;
//                    }
//                }
//            }
//        };
//        return pit;
//    }

    /**
     *
     * @param player
     * @param pitNumber
     * @return
     */
    private PitButton createPitButton(String player, int pitNumber, PitDisplayStrategy strategy) {
        return new PitButton(player, pitNumber, strategy);
    }

    /**
     * Loop through player1 and player2 Pit buttons to add ActionListeners.
     * Sets the action command to "P1_" + index 0-5 representing the pit
     * @param listener - ActionListener to add
     */
    public void addPitListeners(ActionListener listener) {
        for(int i =0; i < 6; i++) {
            player1PitButtons[i].addActionListener(listener);
            player2PitButtons[i].addActionListener(listener);
        }
    }

    /**
     *
     * @param listener
     */
    public void addUndoListener(ActionListener listener) {
        this.undoButton.addActionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void addNewGameListener(ActionListener listener) {
        this.newGameButton.addActionListener(listener);
    }

    /**
     *
     * @param listener
     */
    public void addStyleChoiceComboBoxListener(ActionListener listener) {styleChoiceComboBox.addActionListener(listener);}

    /**
     * Return selected style choice
     * @return - a string representing which list item is selected
     */
    public String getSelectedStyleChoice() {
        return (String) styleChoiceComboBox.getSelectedItem();
    }

    /**
     * Update PitButtons for both players with given pit counts
     * @param player1PitCounts
     * @param player2PitCounts
     */
    private void updatePits(List<Integer> player1PitCounts, List<Integer> player2PitCounts) {

        for(int i = 0; i < 6; i++) {
            this.player1PitButtons[i].setStones(player1PitCounts.get(i));
            this.player2PitButtons[i].setStones(player2PitCounts.get(i));
        }
    }

    /**
     * Update mancala JLabels for both players
     * @param player1MancalaCount
     * @param player2MancalaCount
     */
    private void updateMancalas(int player1MancalaCount, int player2MancalaCount) {
        // delete old Mancala JPanels
//        JLabel player1Mancala = (JLabel) this.player1Mancala.getComponent(0);
//        foo.setText(Integer.toString(player1MancalaCount));
        ((JLabel) this.player1Mancala.getComponent(0)).setText(Integer.toString(player1MancalaCount));
        ((JLabel) this.player2Mancala.getComponent(0)).setText(Integer.toString(player2MancalaCount));
    }

    /**
     * Updates board with given pit and store counts for both players
     * @param player2PitCounts
     * @param player1PitCounts
     * @param player2MancalaCount
     * @param player1MancalaCount
     */
    public void updateBoard(List<Integer> player1PitCounts, List<Integer> player2PitCounts,
                            int player1MancalaCount, int player2MancalaCount) {
        updatePits(player1PitCounts,player2PitCounts);
        updateMancalas(player1MancalaCount, player2MancalaCount);
    }

    /**
     * Sets each PitButton on the board to a PitDisplayStrategy
     * @param strategy - a PitDisplayStrategy
     */
    public void setPitDisplayStrategy(PitDisplayStrategy strategy) {
        for(PitButton pit: player1PitButtons){
            pit.setDisplayStrategy(strategy);
        }

        for(PitButton pit: player2PitButtons){
            pit.setDisplayStrategy(strategy);
        }
        repaint();
    }

    /**
     * Prompt user for number of starting stones (3 or 4)
     * @return {@code int} Number of starting stones
     */
    public int promptForStartingStones() {
        String input = JOptionPane.showInputDialog("Enter number of stones per pit (3 or 4):");
        int DEFAULT_STONES = 4;
        // simple validation
        try {
            return switch (input) {
                case "3" -> 3;
                case "4" -> 4;
                default -> throw new IllegalStateException("Unexpected value: " + input);
            };
        }
        catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(this, "Invalid input. Defaulting to 4.");
            return DEFAULT_STONES;
        }
    }

    /**
     *
     * @param error_message
     */
    public void toastError(String error_message) {
        JOptionPane.showMessageDialog(this, error_message);
    }
}
