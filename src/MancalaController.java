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
import java.awt.event.ActionListener;

public class MancalaController {
    private MancalaModel model;
    private MancalaView view;

    public MancalaController(MancalaModel model, MancalaView view) {
    this.model = model;
    this.view = view;

    // Future work:
    // - Attach listeners to pit buttons
    // - Handle user clicks
    // - Update the view after each move
}
}
