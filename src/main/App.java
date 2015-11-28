package main;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by frostornge on 15. 11. 24.
 */
public class App extends Application {

    public static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        App.stage = stage;

        Parent root = FXMLLoader.load(getClass().getResource("../views/main.fxml"));

        App.stage.setScene(new Scene(root));
        App.stage.setTitle("Knock");
		App.stage.setResizable(false);

        App.stage.show();

    }

}
