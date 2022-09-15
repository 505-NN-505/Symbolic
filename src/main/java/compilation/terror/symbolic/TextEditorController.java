package compilation.terror.symbolic;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;

import java.net.URL;
import java.util.ResourceBundle;

public class TextEditorController implements Initializable {
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

    public TextArea getTextEditor() {
        return textEditor;
    }

    public void setTextEditor(TextArea textEditor) {
        this.textEditor = textEditor;
    }

    public Button getButtonCopy() {
        return buttonCopy;
    }

    public void setButtonCopy(Button buttonCopy) {
        this.buttonCopy = buttonCopy;
    }

    public Button getButtonSelectedCopy() {
        return buttonSelectedCopy;
    }

    public void setButtonSelectedCopy(Button buttonSelectedCopy) {
        this.buttonSelectedCopy = buttonSelectedCopy;
    }

    public Button getButtonTextDelete() {
        return buttonTextDelete;
    }

    public void setButtonTextDelete(Button buttonTextDelete) {
        this.buttonTextDelete = buttonTextDelete;
    }

    public Button getButtonExit() {
        return buttonExit;
    }

    public void setButtonExit(Button buttonExit) {
        this.buttonExit = buttonExit;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonCopy.setOnAction(e -> copyText());
        buttonSelectedCopy.setOnAction(e -> copySelectedText());
        buttonTextDelete.setOnAction(e -> { textEditor.clear(); });
        buttonExit.setOnAction(e -> { Platform.exit(); });
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
