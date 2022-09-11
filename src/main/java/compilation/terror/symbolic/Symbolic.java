package compilation.terror.symbolic;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Symbolic extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Symbolic.class.getResource("Symbolic.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Symbolic");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}