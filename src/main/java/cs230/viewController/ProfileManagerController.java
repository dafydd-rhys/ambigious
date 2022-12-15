package cs230.viewController;

import cs230.profiles.Profile;
import cs230.profiles.ProfileManager;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import static cs230.Application.profileManager;

/**
 * The ProfileManagerController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 */
public class ProfileManagerController implements Initializable {

    /**
     * Load profile
     */
    @FXML
    private Button load;
    /**
     * Remove selected profile
     */
    @FXML
    private ImageView remove;
    /**
     * List of possible profiles
     */
    @FXML
    private ComboBox<String> profileSelector;
    /**
     * Name of new Profile
     */
    @FXML
    private TextField enteredName;
    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Allows user to create or load a profile.
     *
     * @param url URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        remove.setCursor(Cursor.HAND);
        load.setDisable(true);

        List<Profile> profiles = profileManager.getLoadedProfiles();
        updateList(profiles);

        enteredName.textProperty().addListener(e -> {
            if (!enteredName.getText().equals("")) {
                profileSelector.getSelectionModel().select(null);
                profileSelector.setDisable(true);
                load.setDisable(false);
                remove.setVisible(false);
            } else {
                profileSelector.setDisable(false);
                remove.setVisible(false);
                load.setDisable(true);
            }
        });
        profileSelector.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                enteredName.setDisable(true);
                enteredName.setText("");
                load.setDisable(false);
                remove.setVisible(true);
            } else if (enteredName.getText().equals("")) {
                remove.setVisible(false);
                load.setDisable(true);
            }
        });

        remove.setOnMouseClicked(e -> {
            profileSelector.getSelectionModel().select(null);
            enteredName.setDisable(false);
            remove.setVisible(false);
        });

        profileSelector.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (e.getButton() == MouseButton.SECONDARY) {
                if (profileSelector.getSelectionModel().getSelectedItem() != null) {
                    ContextMenu menu = new ContextMenu();
                    MenuItem delete = new MenuItem("Delete Profile");
                    menu.getItems().add(delete);

                    delete.setOnAction(ev -> {
                        try {
                            Profile profile =
                                    getProfile(profileSelector.getSelectionModel().getSelectedItem(), profiles);
                            profiles.remove(profile);

                            assert profile != null;
                            ProfileManager.deletePlayer(profile);
                            ProfileManager.deleteSave(profile);

                            updateList(profiles);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    });
                    profileSelector.setContextMenu(menu);
                }
            }
            e.consume();
        });

        load.setOnAction(e -> {
            if (!enteredName.getText().equals("")) {
                for (Profile profile : profiles) {
                    if (profile.getName().equals(enteredName.getText())) {
                        try {
                            ProfileManager.deletePlayer(profile);
                            ProfileManager.deleteSave(profile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                }

                Profile.setProfile(new Profile(enteredName.getText(), 1));
                profileManager.saveCurrentProfileToFile();
            } else {
                Profile.setProfile(getProfile(profileSelector.getSelectionModel().getSelectedItem(), profiles));
            }

            Stage stage = (Stage) load.getScene().getWindow();
            try {
                FXMLController.openMainMenu((Stage) stage.getOwner());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            stage.close();
        });
    }

    /**
     * Updates list of profiles
     *
     * @param profiles profiles to be updated
     */
    private void updateList(List<Profile> profiles) {
        ArrayList<String> profileNames = new ArrayList<>();
        for (Profile profile : profiles) {
            profileNames.add(profile.getName());
        }

        profileSelector.setItems(FXCollections.observableArrayList(profileNames));
        profileSelector.setPromptText("Profile");
    }

    /**
     * Returns profile of selected item in list
     *
     * @param selectedItem selected item
     * @param profiles list of profiles
     * @return profile from list
     */
    private Profile getProfile(String selectedItem, List<Profile> profiles) {
        for (Profile profile : profiles) {
            if (Objects.equals(profile.getName(), selectedItem)) {
                return profile;
            }
        }
        return null;
    }

}
