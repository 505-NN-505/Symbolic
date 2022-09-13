package compilation.terror.symbolic;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.MotionBlur;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.Buffer;
import java.util.ResourceBundle;

public class SymbolicController implements Initializable {
    @FXML
    private Button buttonBrush;
    @FXML
    private ToggleButton buttonEraser;
    @FXML
    private Button buttonClear;

    @FXML
    private Canvas canvas;
    GraphicsContext brushTool;

    boolean isBrushSelected, isEraserSelected;
    double brushStrokeWidth = 6.0;
    double eraserStrokeWidth = 10.0;
    boolean canDraw;

    double boundaryX, rectX, boundaryY, rectY;
    double boundaryRangeX, boundaryRangeY;

    MotionBlur motionBlur = new MotionBlur();

    //text editor
    @FXML
    private TextArea textEditor;
    @FXML
    private Button buttonCopy;
    @FXML
    private Button buttonSelectedCopy;
    @FXML
    private Button buttonTextDelete;
    @FXML
    private Button buttonExit;

    @FXML
    private Button buttonConvert;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        brushTool = canvas.getGraphicsContext2D();
        clearTheCanvus();
        buttonBrush.setOnAction(e -> brushSelected());
        buttonEraser.setOnAction(e -> eraserSelected());
        buttonClear.setOnAction(e -> clearTheCanvus());
        buttonCopy.setOnAction(e -> copyText());
        buttonSelectedCopy.setOnAction(e -> copySelectedText());
        buttonTextDelete.setOnAction(e -> { textEditor.clear(); });
        buttonExit.setOnAction(e -> { Platform.exit(); });
        buttonConvert.setOnAction(e -> {
            try {
                startConversion();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });
        canvas.setOnMousePressed(e -> {
            if (!isEraserSelected && isBrushSelected && !canDraw) {
                double x = e.getX() - 17.5;
                double y = e.getY() - 25;
                brushTool.setFill(Color.BLACK);
                brushTool.strokeRect(x, y, 70.0, 100.0);
                boundaryX = x + brushStrokeWidth;
                boundaryY = y + brushStrokeWidth;
                rectX = x; rectY = y;
                System.out.println("rect");
                System.out.println(x + " " + y);
                System.out.println(boundaryX + " " + boundaryY);
                boundaryRangeX = x + 70.0 - brushStrokeWidth;
                boundaryRangeY = y + 100.0 - brushStrokeWidth;
            }
        });
        canvas.setOnMouseReleased(e -> {
            if (!isEraserSelected && isBrushSelected && !canDraw) {
                canDraw = true;
            }
        });
        canvas.setOnMouseDragged(e -> {
            if (canDraw && !isEraserSelected) {
                double size = brushStrokeWidth;
                double x = e.getX() - size / 2;
                double y = e.getY() - size / 2;
                double cx = e.getX();
                double cy = e.getY();
                    if (cx > boundaryX && cx < boundaryRangeX && cy > boundaryY && cy < boundaryRangeY) {
                    brushTool.setFill(Color.BLACK);
                    brushTool.fillRoundRect(x, y, size, size, size, size);
                    motionBlur.setRadius(3);
                    brushTool.setEffect(motionBlur);
                    brushTool.setGlobalAlpha(5.0);
                }
            }
            if (isEraserSelected) {
                double eraserSize = eraserStrokeWidth;
                double x = e.getX() - eraserSize / 2;
                double y = e.getY() - eraserSize / 2;
                double cx = e.getX();
                double cy = e.getY();
                if (cx - 1.0 > boundaryX && cx + 1.0 < boundaryRangeX && cy - 1.0 > boundaryY && cy + 1.0 < boundaryRangeY) {
                    brushTool.clearRect(x, y, eraserSize, eraserSize);
                    brushTool.setFill(Color.WHITE);
                    brushTool.fillRect(x, y, eraserSize, eraserSize);
                }
            }
        });
    }

    void clearTheCanvus() {
        isBrushSelected = false;
        canDraw = false;
        brushTool.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        brushTool.setFill(Color.WHITE);
        brushTool.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    void brushSelected() {
        clearTheCanvus();
        isBrushSelected = true;
        isEraserSelected = false;
        buttonEraser.setSelected(false);
    }
    void eraserSelected() {
        isEraserSelected ^= true;
        buttonEraser.setSelected((isEraserSelected));
    }

    void copyText() {
        String myText = textEditor.getText().toString();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(myText);
        clipboard.setContent(content);
    }

    void copySelectedText() {
        String myText = textEditor.getSelectedText().toString();
        final Clipboard clipboard = Clipboard.getSystemClipboard();
        final ClipboardContent content = new ClipboardContent();
        content.putString(myText);
        clipboard.setContent(content);
    }

    void startConversion() throws IOException {
        System.out.println(canvas.getLayoutX() + " " + canvas.getLayoutY());
        System.out.println(boundaryX + " " + boundaryY);
        takeSnapShot();
    }

    void takeSnapShot() throws IOException {
        WritableImage image = canvas.snapshot(new SnapshotParameters(), null);
        ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", new File("pic.png"));
        File imageFile = new File("D:/Development/Projects/Symbolic/pic.png");
        BufferedImage bufferedImage = ImageIO.read(imageFile);
        BufferedImage croppedImage = bufferedImage.getSubimage((int)(rectX), (int)(rectY), 71, 101);
        File pathFile = new File("D:/Development/Projects/Symbolic/image-crop.jpg");
        ImageIO.write(croppedImage, "png", pathFile);
    }
}