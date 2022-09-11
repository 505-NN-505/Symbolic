package compilation.terror.symbolic;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.MotionBlur;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.paint.Color;

import java.net.URL;
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

    double boundaryX, boundaryY;
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
        canvas.setOnMousePressed(e -> {
            if (!isEraserSelected && isBrushSelected && !canDraw) {
                double x = e.getX() - 17.5;
                double y = e.getY() - 25;
                brushTool.setFill(Color.BLACK);
                brushTool.strokeRect(x, y, 70.0, 100.0);
                boundaryX = x + brushStrokeWidth;
                boundaryY = y + brushStrokeWidth;
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
    }
    void eraserSelected() {
        isEraserSelected ^= true;
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
}