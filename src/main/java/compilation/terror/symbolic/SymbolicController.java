package compilation.terror.symbolic;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SymbolicController implements Initializable {
    @FXML
    private Group paneGroup;
    @FXML
    private Button buttonConvert;

    ArtboardController artboardController;
    TextEditorController textEditorController;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            loadArtboard();
            loadTextEditor();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        buttonConvert.setOnAction(e -> {
            try {
                startConversion();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
    }

    void loadArtboard() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("Artboard.fxml"));
        BorderPane artboardPane = fxmlloader.load();
        artboardController = fxmlloader.getController();
        paneGroup.getChildren().add(artboardPane);
    }

    void loadTextEditor() throws IOException {
        FXMLLoader fxmlloader = new FXMLLoader();
        fxmlloader.setLocation(getClass().getResource("TextEditor.fxml"));
        BorderPane textEditorPane = fxmlloader.load();
        textEditorPane.setLayoutX(0);
        textEditorPane.setLayoutY(350);
        textEditorController = fxmlloader.getController();
        paneGroup.getChildren().add(textEditorPane);
    }

    void startConversion() throws IOException {
        takeSnapShot();
    }

    void takeSnapShot() throws IOException {
        WritableImage image = artboardController.getCanvas().snapshot(new SnapshotParameters(), null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("src/main/out/artboard.png"));
        File imageFile = new File("D:/Development/Projects/Symbolic/src/main/out/artboard.png");
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        BufferedImage croppedImage = bufferedImage.getSubimage((int)(artboardController.getRectX()), (int)(artboardController.getRectY()), 71, 101);
        File pathFile = new File("D:/Development/Projects/Symbolic/src/main/out/symbol.png");
        ImageIO.write(croppedImage, "png", pathFile);
    }
}