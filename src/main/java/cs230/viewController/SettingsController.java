package cs230.viewController;

import cs230.external.LocalizationManager;
import cs230.external.Settings;
import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.stage.Stage;

/**
 * The SettingsController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 */
public class SettingsController implements Initializable {

    /**
     * Resource bundle used for this fxml
     */
    private ResourceBundle resourceBundle;
    /**
     * The Save button.
     */
    @FXML
    public Button saveButton;
    /**
     * The Music slider.
     */
    @FXML
    public Slider musicSlider;
    /**
     * The Effect slider.
     */
    @FXML
    public Slider effectSlider;
    /**
     * Checkbox to indicate fullscreen or not
     */
    @FXML
    private CheckBox playInFullscreenCheckBox;
    /**
     * Checkbox to indicate themed FX
     */
    @FXML
    private CheckBox themedFXCheckBox;
    /**
     * Checkbox to indicate fullscreen or not
     */
    @FXML
    private ComboBox<String> selectedLanguage;
    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * Allows user to manage the user settings.
     *
     * @param url            URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @FXML
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

        selectedLanguage.setItems(FXCollections.observableArrayList(
                "English (GB)", "Cantonese", "Indonesian", "Portuguese", "Tagalog", "Welsh"));

        FXMLController.fullscreen = Settings.isFullscreen();
        FXMLController.themedFX = Settings.isFXEnabled();

        selectedLanguage.setValue(deCode(Settings.getLanguage()));
        playInFullscreenCheckBox.selectedProperty().setValue(Settings.isFullscreen());
        themedFXCheckBox.selectedProperty().setValue(Settings.isFXEnabled());
        musicSlider.setValue(Settings.getMusic());
        effectSlider.setValue(Settings.getEffects());
    }

    /**
     * On save settings button click save settings to txt and update in-game UI to match settings.
     *
     * @throws IOException the io exception
     */
    @FXML
    protected void onSaveSettingsButtonClick() throws IOException {
        FXMLController.fullscreen = playInFullscreenCheckBox.isSelected();
        FXMLController.themedFX = themedFXCheckBox.isSelected();

        onSelectLanguage();
        Settings.setLanguage(getCode(selectedLanguage.getValue()));
        Settings.setFullscreen(playInFullscreenCheckBox.isSelected());
        Settings.setThemedFX(themedFXCheckBox.isSelected());
        Settings.setMusic(musicSlider.valueProperty().floatValue());
        Settings.setEffects(effectSlider.valueProperty().floatValue());

        Locale settingsLocale = Locale.forLanguageTag(getCode(selectedLanguage.getValue()));

        if (LocalizationManager.localeIsSupported(settingsLocale)) {
            LocalizationManager.setCurrentLocale(settingsLocale);
        } else {
            // Get system language
            Locale systemLocale = LocalizationManager.getSystemLocale();
            if (LocalizationManager.localeIsSupported(systemLocale)) {
                LocalizationManager.setCurrentLocale(systemLocale);
            } else {
                LocalizationManager.setCurrentLocale(LocalizationManager.FALLBACK_LOCALE);
            }
        }

        Stage stage = (Stage) selectedLanguage.getScene().getWindow();
        if (FXMLController.fromMain) {
            try {
                FXMLController.openMainMenu((Stage) stage.getOwner());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        stage.close();
    }

    /**
     * Converts readable language into its language code
     *
     * @param rx language regex
     * @return language code
     */
    private String getCode(String rx) {
        String code;

        switch (rx) {
            case "English (GB)" -> code = "en-GB";
            case "Cantonese" -> code = "yue";
            case "Indonesian" -> code = "in";
            case "Portuguese" -> code = "pt";
            case "Welsh" -> code = "cy";
            default -> code = "tl";
        }

        return code;
    }

    /**
     * De-codes the language code and converts it into its readable language
     *
     * @param code language code
     * @return readable string of language
     */
    private String deCode(String code) {
        String language;

        switch (code) {
            case "en-GB" -> language = "English (GB)";
            case "yue" -> language = "Cantonese";
            case "in" -> language = "Indonesian";
            case "pt" -> language = "Portuguese";
            case "cy" -> language = "Welsh";
            default -> language = "Tagalog";
        }

        return language;
    }

    /**
     * Updates language of program
     */
    public void onSelectLanguage() {

    }

}
