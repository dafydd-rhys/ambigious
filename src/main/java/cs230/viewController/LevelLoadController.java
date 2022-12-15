package cs230.viewController;

import cs230.Themes;
import cs230.external.Settings;
import cs230.model.board.BoardReaderAndGenerator;
import cs230.model.board.StaticBoard;
import cs230.profiles.Profile;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

/**
 * The LevelLoadController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 */
public class LevelLoadController implements Initializable {

    /**
     * Load level button.
     */
    @FXML
    private Button load;
    /**
     * Select level button.
     */
    @FXML
    private ComboBox<String> levelSelector;
    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Allows the player to play any unlocked level.
     *
     * @param url URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        levelSelector.getItems().add("Level 1");

        ArrayList<String> levels = new ArrayList<>();
        for (int i = 0; i < Profile.getProfile().getMaxLevel(); i++) {
            levels.add("Level " + (i + 1));
        }
        levelSelector.setItems(FXCollections.observableArrayList(levels));
        levelSelector.setValue("Level 1");

        load.setOnAction(e -> {
            String[] split = levelSelector.getSelectionModel().getSelectedItem().split(" ");
            StaticBoard.level = Integer.parseInt(split[1]);

            try {
                Stage stage = (Stage) load.getScene().getWindow();

                Profile.notesCollected = new boolean[Settings.NUMBER_OF_NOTES];
                Profile.getRandomCharacterDetails();

                StaticBoard.reader = new BoardReaderAndGenerator(StaticBoard.level);
                StaticBoard.board = StaticBoard.reader.readBoard(false);
                StaticBoard.board.setColors(Themes.typeToTheme(StaticBoard.board.getCurrentLevel()).getColors());

                FXMLController.openGame((Stage) stage.getOwner());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

}
