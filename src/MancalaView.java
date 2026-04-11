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

        for(int i = 0;i < 6; i++) {
            buttons[i] = new JButton();
            panel.add(buttons[i]);
        }
        return buttons;
    }
// for testing
//    public static void main(String[] args){
//        MancalaView view = new MancalaView();
//    }
}
