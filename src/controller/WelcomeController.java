package controller;

import javafx.event.ActionEvent;
import view.SceneChanger;

import java.io.IOException;

public class WelcomeController {
    private SceneChanger sceneChanger = new SceneChanger();

    public void start(ActionEvent e) throws IOException {
        sceneChanger.changeScene("/view/InputScene.fxml", e);
    }
}
