package cs230;

import cs230.external.Settings;
import cs230.model.board.StaticBoard;
import cs230.profiles.ProfileManager;
import cs230.viewController.FXMLController;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Application class instantiates the UI and backend aspect of program.
 */
public class Application extends javafx.application.Application {

    /**
     * Profile manager used to manage profile.
     */
    public static ProfileManager profileManager = new ProfileManager();

    /**
     * Sets stage and loads it.
     *
     * @param stage the stage hosting fxml
     * @throws IOException can't find fxml
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLController.openMainMenu(stage);
        FXMLController.firstLoad = false;
        StaticBoard.level = 1;
    }

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws IOException file can't be found
     */
    public static void main(String[] args) throws IOException {
        // OB: I have moved settings initialization here, since it needs to check the language before any
        // fxml windows are created.
        Settings.initialize();
        launch();
    }

}
