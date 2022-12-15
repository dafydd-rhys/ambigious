package cs230.viewController;

import cs230.profiles.Profile;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

/**
 * The CharacterDiscoveryController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 */
public class CharacterDiscoveryController implements Initializable {

    /**
     * Complete game button.
     */
    @FXML
    private Button complete;
    /**
     * Injury of character label.
     */
    @FXML
    private Label injury;
    /**
     * Name of character label.
     */
    @FXML
    private Label name;
    /**
     * Description of character label.
     */
    @FXML
    private Label desc;
    /**
     * Gender of character label.
     */
    @FXML
    private Label gender;
    /**
     * Date Of Birth of character label.
     */
    @FXML
    private Label dob;
    /**
     * Occupation of character label.
     */
    @FXML
    private Label occupation;
    /**
     * Nationality of character label.
     */
    @FXML
    private Label nationality;
    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Displays what the player discover about the character.
     *
     * @param url URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        Label[] labels = {gender, occupation, injury, name, desc, nationality, dob, name};

        for (int i = 0; i < Profile.notesCollected.length; i++) {
            if (Profile.notesCollected[i]) {
                labels[i].setText(Profile.notesData[i] + " " + labels[i].getText());
            } else {
                labels[i].setText("_______" + " " + labels[i].getText());
            }
        }

        complete.setOnMouseClicked(e -> openMainMenu());
    }

    /**
     * Opens main menu
     */
    private void openMainMenu() {
        try {
            FXMLController.openMainMenu((Stage) getThisStage().getOwner());
            getThisStage().close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Returns this stage
     *
     * @return stage
     */
    private Stage getThisStage() {
        return (Stage) complete.getScene().getWindow();
    }

}
