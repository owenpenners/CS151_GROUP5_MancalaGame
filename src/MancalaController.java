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
    private final MancalaModel model;
    private final MancalaView view;

    /**
     *
     * @param model
     * @param view
     */
    public MancalaController(MancalaModel model, MancalaView view) {
        this.model = model;
        this.model.addChangeListener(this);
        this.view = view;

        this.attachPitListeners();
        this.attachUndoListener();
        this.attachNewGameListener();
    }

    /**
     *
     */
    public void attachPitListeners() {
        // Future work:
        // - Attach listeners to pit buttons
        // - Handle user clicks
        // - Update the view after each move
        this.view.addPitListeners(e -> {
            PitButton b = (PitButton) e.getSource();
            String p = b.getPlayer();
            int n = b.getPit();
            System.out.println("Player " + p + " clicked " + n);
            try {
                switch (p) {
                    case "P1" -> model.moveStones(MancalaModel.Player.PLAYER_1, n);
                    case "P2" -> model.moveStones(MancalaModel.Player.PLAYER_2, n);
                }
            }
            catch (IllegalArgumentException err) {
                System.err.println(err.getMessage());
                view.toastError(err.getMessage());
            }

        });
    }

    /**
     *
     */
    public void attachUndoListener() {
        this.view.addUndoListener(e -> {
            try {
                MancalaController.this.model.undo();
            }
            catch (IllegalStateException err) {
                System.err.println(err.getMessage());
                view.toastError(err.getMessage());
            }
        });
    }

    /**
     *
     */
    public void attachNewGameListener() {
        this.view.addNewGameListener(e -> {
            MancalaController.this.model.resetBoard();
            model.setPits(view.promptForStartingStones());
        });
    }

    /**
     * Refresh the mancala board by getting pit counts
     */
    public void stateChanged(ChangeEvent e) {
        MancalaModel.MancalaRecord record = ((MancalaModel) e.getSource()).getRecord();
        // Tell the view to update what is shown on screen
        view.updateBoard(record.p1_side(), record.p2_side(), record.p1_end(), record.p2_end());
    }
}
