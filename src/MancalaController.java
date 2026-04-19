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

    model.fillPits(view.promptForStartingStones());
    refreshBoard();

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
        
        // Get Player 2 pit counts from the model
        int[] player2PitCounts = model.getPitCountsByPlayer(MancalaModel.Player.PLAYER_2);
        // Get Player 1 pit counts from the model
        int[] player1PitCounts = model.getPitCountsByPlayer(MancalaModel.Player.PLAYER_1);
        
        // Get the number of stones in each player's Mancala
        int player2MancalaCount = model.getStonesFromMancala(MancalaModel.Player.PLAYER_2);
        int player1MancalaCount = model.getStonesFromMancala(MancalaModel.Player.PLAYER_1);
        
        /* can be used for debugging, print to console no longer needed
        System.out.println("Player A Mancala Score: " + model.getStonesFromMancala(MancalaModel.Player.PLAYER_1));
        System.out.println("Player B Mancala Score: " + model.getStonesFromMancala(MancalaModel.Player.PLAYER_2));
        */
        
        // Tell the view to update what is shown on screen
        view.updateBoard(player2PitCounts, player1PitCounts,
            player2MancalaCount, player1MancalaCount);
    }
}
