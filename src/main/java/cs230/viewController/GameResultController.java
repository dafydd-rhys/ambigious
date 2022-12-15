package cs230.viewController;

import cs230.Themes;
import cs230.external.Settings;
import cs230.model.board.BoardReaderAndGenerator;
import cs230.model.board.StaticBoard;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * The GameResultController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 */
public class GameResultController implements Initializable {

    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;
    /**
     * Display text used to show result of game.
     */
    @FXML
    private Text gameResult;
    /**
     * Continue button.
     */
    @FXML
    private Button cont;
    /**
     * Restart level button.
     */
    @FXML
    private Button restart;
    /**
     * Go back to main menu button.
     */
    @FXML
    private Button mainMenu;
    /**
     * Exit game button.
     */
    @FXML
    private Button exit;

    /**
     * Allows the player to decide what they want to do after finishing a level.
     *
     * @param url URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle)  {
        this.resources = resourceBundle;

        if (StaticBoard.won) {
            if (StaticBoard.level < Settings.NUMBER_OF_LEVELS) {
                gameResult.setText(resources.getString("youWon"));

                cont.setOnAction(e -> {
                    StaticBoard.level++;
                    StaticBoard.reader = new BoardReaderAndGenerator(StaticBoard.level);

                    try {
                        StaticBoard.board = StaticBoard.reader.readBoard(false);
                        StaticBoard.board.setColors(Themes.typeToTheme(StaticBoard.board.getCurrentLevel()).getColors());
                        FXMLController.openGame((Stage) getThisStage().getOwner());
                        getThisStage().close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
            } else {
                gameResult.setText(resources.getString("congratulations"));

                cont.setOnAction(e -> {
                    FXMLController.openCharacterDiscovery((Stage) getThisStage().getOwner());
                    getThisStage().close();
                });
            }
            cont.setDisable(false);
        } else {
            gameResult.setText(resources.getString("gameOver"));
        }

        restart.setOnAction(e -> {
            StaticBoard.reader = new BoardReaderAndGenerator(StaticBoard.level);

            try {
                StaticBoard.board = StaticBoard.reader.readBoard(false);
                StaticBoard.board.setColors(Themes.typeToTheme(StaticBoard.board.getCurrentLevel()).getColors());
                FXMLController.openGame((Stage) getThisStage().getOwner());

                getThisStage().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        mainMenu.setOnAction(e -> {
            try {
                FXMLController.openMainMenu((Stage) getThisStage().getOwner());
                getThisStage().close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        exit.setOnAction(e -> System.exit(1));
    }

    /**
     * Returns this stage
     *
     * @return stage
     */
    private Stage getThisStage() {
        return (Stage) gameResult.getScene().getWindow();
    }

}
