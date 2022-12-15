package cs230.viewController;

import java.net.URL;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

/**
 * The CreditsController used to manage UI interactions of this FXML.
 *
 * @author Oliver
 */
public class CreditsController implements Initializable {

    /**
     * Resource bundle of this fxml
     */
    @FXML
    private ResourceBundle resources;

    /**
     * ...
     *
     * @param url URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;

    }

}
