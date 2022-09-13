module compilation.terror.symbolic {
    requires javafx.controls;
    requires javafx.fxml;

    requires javafx.swing;
    requires java.desktop;

    requires org.kordamp.ikonli.core;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires org.kordamp.ikonli.remixicon;


    opens compilation.terror.symbolic to javafx.fxml;
    exports compilation.terror.symbolic;
}