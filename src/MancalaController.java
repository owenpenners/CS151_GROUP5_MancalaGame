/**
 * MancalaController
 *
 * This class connects the Model and View in the MVC pattern.
 * It will handle user interactions (such as button clicks),
 * call the appropriate methods in the model (game logic),
 * and update the view accordingly.
 *
 * Responsibilities:
 * - Listen to user input from the view
 * - Refresh/update the view after changes
 */

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MancalaController {
    private MancalaModel model;
    private MancalaView view;

    public MancalaController(MancalaModel model, MancalaView view) {
    this.model = model;
    this.view = view;

    model.fillPits(4); // need to prompt users for number of stones in pit 3 or 4.

/*
String input = JOptionPane.showInputDialog(view,
    "Enter number of stones per pit (3 or 4):");

int stones = Integer.parseInt(input);

// simple validation
if (stones != 3 && stones != 4) {
    JOptionPane.showMessageDialog(view, "Invalid input. Defaulting to 4.");
    stones = 4;
}

model.fillPits(stones);
refreshBoard();
*/
        
    // Future work:
    // - Attach listeners to pit buttons
    // - Handle user clicks
    // - Update the view after each move
    this.view.addPitListeners(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            String cmd = e.getActionCommand();

            if(cmd.startsWith("P2_")) {
                int pit = 6 - Integer.parseInt(cmd.substring(3)) -1;
                System.out.println("Player 2 clicked " + pit);
                // call moveStones
                model.moveStones(MancalaModel.Player.PLAYER_2,pit);
                //model.moveStones(pit);
            } else if(cmd.startsWith("P1_")) {
                int pit = Integer.parseInt(cmd.substring(3));
                System.out.println("Player 1 clicked " + pit);
                // call moveStones
                model.moveStones(MancalaModel.Player.PLAYER_1,pit);
                //model.moveStones(pit);
            }
            refreshBoard();
        }
    });
    }

    /**
     * Refresh the mancala board by getting pit counts
     */
    public void refreshBoard() {
        int[] player2PitCounts = model.getPitCountsByPlayer(MancalaModel.Player.PLAYER_2);
        int[] player1PitCounts = model.getPitCountsByPlayer(MancalaModel.Player.PLAYER_1);

        System.out.println("Player A Mancala Score: " + model.getStonesFromMancala(MancalaModel.Player.PLAYER_1));
        System.out.println("Player B Mancala Score: " + model.getStonesFromMancala(MancalaModel.Player.PLAYER_2));
    }
}
