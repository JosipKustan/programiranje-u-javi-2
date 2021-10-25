module hr.algebra.senet {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens hr.algebra.senet to javafx.fxml;
    exports hr.algebra.senet;
}