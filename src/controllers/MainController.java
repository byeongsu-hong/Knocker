package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import main.App;

import java.io.IOException;

/**
 *
 * Created by frostornge on 15. 11. 24.
 */
public class MainController {
    public void handleStartButtonAction(ActionEvent event) {
        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getResource("../views/game.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (root != null) {
            App.stage.setScene(new Scene(root));
        }

        App.stage.show();
    }
}
