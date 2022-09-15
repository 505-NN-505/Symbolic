package compilation.terror.symbolic;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.MotionBlur;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;

public class ArtboardController implements Initializable {
    @FXML
    private Canvas canvas;
    GraphicsContext brushTool;
    @FXML
    private Button buttonBrush;
    @FXML
    private ToggleButton buttonEraser;
    @FXML
    private Button buttonClear;

    boolean isBrushSelected, isEraserSelected;
    double brushStrokeWidth = 6.0;
    double eraserStrokeWidth = 10.0;
    boolean canDraw;

    double boundaryX, rectX, boundaryY, rectY;
    double boundaryRangeX, boundaryRangeY;

    MotionBlur motionBlur = new MotionBlur();

    public Canvas getCanvas() {
        return canvas;
    }

    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
    }

    public GraphicsContext getBrushTool() {
        return brushTool;
    }

    public void setBrushTool(GraphicsContext brushTool) {
        this.brushTool = brushTool;
    }

    public Button getButtonBrush() {
        return buttonBrush;
    }

    public void setButtonBrush(Button buttonBrush) {
        this.buttonBrush = buttonBrush;
    }

    public ToggleButton getButtonEraser() {
        return buttonEraser;
    }

    public void setButtonEraser(ToggleButton buttonEraser) {
        this.buttonEraser = buttonEraser;
    }

    public Button getButtonClear() {
        return buttonClear;
    }

    public void setButtonClear(Button buttonClear) {
        this.buttonClear = buttonClear;
    }

    public boolean isBrushSelected() {
        return isBrushSelected;
    }

    public void setBrushSelected(boolean brushSelected) {
        isBrushSelected = brushSelected;
    }

    public boolean isEraserSelected() {
        return isEraserSelected;
    }

    public void setEraserSelected(boolean eraserSelected) {
        isEraserSelected = eraserSelected;
    }

    public double getBrushStrokeWidth() {
        return brushStrokeWidth;
    }

    public void setBrushStrokeWidth(double brushStrokeWidth) {
        this.brushStrokeWidth = brushStrokeWidth;
    }

    public double getEraserStrokeWidth() {
        return eraserStrokeWidth;
    }

    public void setEraserStrokeWidth(double eraserStrokeWidth) {
        this.eraserStrokeWidth = eraserStrokeWidth;
    }

    public boolean isCanDraw() {
        return canDraw;
    }

    public void setCanDraw(boolean canDraw) {
        this.canDraw = canDraw;
    }

    public double getBoundaryX() {
        return boundaryX;
    }

    public void setBoundaryX(double boundaryX) {
        this.boundaryX = boundaryX;
    }

    public double getRectX() {
        return rectX;
    }

    public void setRectX(double rectX) {
        this.rectX = rectX;
    }

    public double getBoundaryY() {
        return boundaryY;
    }

    public void setBoundaryY(double boundaryY) {
        this.boundaryY = boundaryY;
    }

    public double getRectY() {
        return rectY;
    }

    public void setRectY(double rectY) {
        this.rectY = rectY;
    }

    public double getBoundaryRangeX() {
        return boundaryRangeX;
    }

    public void setBoundaryRangeX(double boundaryRangeX) {
        this.boundaryRangeX = boundaryRangeX;
    }

    public double getBoundaryRangeY() {
        return boundaryRangeY;
    }

    public void setBoundaryRangeY(double boundaryRangeY) {
        this.boundaryRangeY = boundaryRangeY;
    }

    public MotionBlur getMotionBlur() {
        return motionBlur;
    }

    public void setMotionBlur(MotionBlur motionBlur) {
        this.motionBlur = motionBlur;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        brushTool = canvas.getGraphicsContext2D();
        clearTheCanvus();
        buttonBrush.setOnAction(e -> brushSelected());
        buttonEraser.setOnAction(e -> eraserSelected());
        buttonClear.setOnAction(e -> clearTheCanvus());
        canvas.setOnMousePressed(e -> {
            double x = e.getX() - 17.5;
            double y = e.getY() - 25;
            drawBoundary(x, y);
        });
        canvas.setOnMouseReleased(e -> {
            if (!isEraserSelected && isBrushSelected && !canDraw) {
                canDraw = true;
            }
        });
        canvas.setOnMouseDragged(e -> {
            if (canDraw && !isEraserSelected) {
                double size = brushStrokeWidth;
                double x = e.getX() - size / 2, y = e.getY() - size / 2;
                double cx = e.getX(), cy = e.getY();
                drawOnArtboard(x, y, cx, cy, size);
            }
            if (isEraserSelected) {
                double eraserSize = eraserStrokeWidth;
                double x = e.getX() - eraserSize / 2, y = e.getY() - eraserSize / 2;
                double cx = e.getX(), cy = e.getY();
                eraseFromArtboard(x, y, cx, cy, eraserSize);

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

    void drawBoundary(double x, double y) {
        if (!isEraserSelected && isBrushSelected && !canDraw) {
            brushTool.setFill(Color.BLACK);
            brushTool.strokeRect(x, y, 70.0, 100.0);
            boundaryX = x + brushStrokeWidth;
            boundaryY = y + brushStrokeWidth;
            rectX = x; rectY = y;
            boundaryRangeX = x + 70.0 - brushStrokeWidth;
            boundaryRangeY = y + 100.0 - brushStrokeWidth;
        }
    }

    void drawOnArtboard(double x, double y, double cx, double cy, double size) {
        if (cx > boundaryX && cx < boundaryRangeX && cy > boundaryY && cy < boundaryRangeY) {
            brushTool.setFill(Color.BLACK);
            brushTool.fillRoundRect(x, y, size, size, size, size);
            motionBlur.setRadius(3);
            brushTool.setEffect(motionBlur);
            brushTool.setGlobalAlpha(5.0);
        }
    }

    void eraseFromArtboard(double x, double y, double cx, double cy, double eraserSize) {
        if (cx - 1.0 > boundaryX && cx + 1.0 < boundaryRangeX && cy - 1.0 > boundaryY && cy + 1.0 < boundaryRangeY) {
            brushTool.clearRect(x, y, eraserSize, eraserSize);
            brushTool.setFill(Color.WHITE);
            brushTool.fillRect(x, y, eraserSize, eraserSize);
        }
    }
}
