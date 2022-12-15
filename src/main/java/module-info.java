/**
 * module used for project - implements javafx
 */
module cs230 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires java.desktop;

    opens cs230 to javafx.fxml, javafx.base;
    opens cs230.scoreboard to javafx.base;
    opens cs230.viewController to javafx.fxml;

    exports cs230;
    exports cs230.viewController;
}