import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

/**
 * MancalaController
 * <p>
 * This class connects the Model and View in the MVC pattern.
 * It will handle user interactions (such as button clicks),
 * call the appropriate methods in the model (game logic),
 * and update the view accordingly.
 * <p>
 * Responsibilities:
 * <ul>- Listen to user input from the view</ul>
 * <ul>- Refresh/update the view after changes</ul>
 */
public class MancalaController implements ChangeListener {
    private MancalaModel model;
    private MancalaView view;

    public MancalaController(MancalaModel model, MancalaView view) {
        this.model = model;
        this.model.addChangeListener(this);
        this.view = view;

        model.fillPits(view.promptForStartingStones());

        // Future work:
        // - Attach listeners to pit buttons
        // - Handle user clicks
        // - Update the view after each move
        this.view.addPitListeners(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PitButton b = (PitButton) e.getSource();
                String p = b.getPlayer();
                int n = b.getPit();
                System.out.println("Player " + p + " clicked " + n);
                switch (p) {
                    case "P1" -> model.moveStones(MancalaModel.Player.PLAYER_1, n);
                    case "P2" -> model.moveStones(MancalaModel.Player.PLAYER_2, n);
                }
    //            String cmd = e.getActionCommand();
    //
    //
    //            if(cmd.startsWith("P2_")) {
    //                int pit = 6 - Integer.parseInt(cmd.substring(3)) -1;
    //                System.out.println("Player 2 clicked " + pit);
    //                // call moveStones
    //                model.moveStones(MancalaModel.Player.PLAYER_2,pit);
    //                //model.moveStones(pit);
    //            } else if(cmd.startsWith("P1_")) {
    //                int pit = Integer.parseInt(cmd.substring(3));
    //                System.out.println("Player 1 clicked " + pit);
    //                // call moveStones
    //                model.moveStones(MancalaModel.Player.PLAYER_1,pit);
    //                //model.moveStones(pit);
    //            }
    //            MancalaController.this.stateChanged(new ChangeEvent(this));
            }
        });
    }

    /**
     * Refresh the mancala board by getting pit counts
     */
    public void stateChanged(ChangeEvent e) {
        // Get Player 2 pit counts from the model
        int[] player2PitCounts = model.getStonesOfPits(MancalaModel.Player.PLAYER_2)
                .stream().mapToInt(i -> i).toArray();
        // Get Player 1 pit counts from the model
        int[] player1PitCounts = model.getStonesOfPits(MancalaModel.Player.PLAYER_1)
                .stream().mapToInt(i -> i).toArray();
        
        // Get the number of stones in each player's Mancala
        int player2MancalaCount = model.getStonesFromEnd(MancalaModel.Player.PLAYER_2);
        int player1MancalaCount = model.getStonesFromEnd(MancalaModel.Player.PLAYER_1);
        
        /* can be used for debugging, print to console no longer needed
        System.out.println("Player A Mancala Score: " + model.getStonesFromMancala(MancalaModel.Player.PLAYER_1));
        System.out.println("Player B Mancala Score: " + model.getStonesFromMancala(MancalaModel.Player.PLAYER_2));
        */

        // Tell the view to update what is shown on screen
        view.updateBoard(player2PitCounts, player1PitCounts,
            player2MancalaCount, player1MancalaCount);
    }
}
