package controller;

import javafx.event.ActionEvent;
import view.SceneChanger;

import java.io.IOException;

/**
 * Controller class for the Welcome screen.
 */
public class WelcomeController {
    /**
     * Helper object for changing scenes.
     */
    private final SceneChanger sceneChanger = new SceneChanger();

    /**
     * Starts the application by changing to the InputScene view.
     *
     * @param e the ActionEvent triggered by a user interaction
     * @throws IOException if the FXML file cannot be loaded
     */
    public void start(ActionEvent e) throws IOException {
        sceneChanger.changeScene("/view/InputScene.fxml", e);
    }
}
