package cs230.viewController;

import cs230.scoreboard.Scoreboard;
import cs230.scoreboard.ScoreboardPlayer;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * ScoreboardController - used to manage fxml elements in scoreboard scene.
 *
 * @author Dafydd -Rhys Maund (2003900)
 */
public class ScoreboardController implements Initializable {

    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;
    /**
     * levelSelector - combo box holding all scoreboard options.
     */
    @FXML
    private ComboBox<String> levelSelector;
    /**
     * scoreboard - table that holds the top 10 players.
     */
    @FXML
    private TableView<ScoreboardPlayer> scoreboard;
    /**
     * level - level row representing the top 10 players for this particular level.
     */
    @FXML
    private TableColumn<ScoreboardPlayer, String> level;
    /**
     * rank - rank of the player in question.
     */
    @FXML
    private TableColumn<ScoreboardPlayer, Integer> rank;
    /**
     * name - name of the player in question.
     */
    @FXML
    private TableColumn<ScoreboardPlayer, String> name;
    /**
     * score - score of the player in question.
     */
    @FXML
    private TableColumn<ScoreboardPlayer, Integer> score;

    /**
     * when the fxml is loaded do this straight away,
     * loads all players from all scoreboards into scoreboard table.
     *
     * @param url url of resources.
     * @param resourceBundle bundle of resources.
     */
    @Override
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        //add options to combo-box
        levelSelector.getItems().addAll("Level 1", "Level 2", "Level 3",
                "Level 4", "Level 5", "Level 6", "Level 7", "Level 8");

        //creates value factories so can be sorted and inserted easier
        level.setCellValueFactory(new PropertyValueFactory<>(resourceBundle.getString("level")));
        rank.setCellValueFactory(new PropertyValueFactory<>(resourceBundle.getString("rank")));
        name.setCellValueFactory(new PropertyValueFactory<>(resourceBundle.getString("name")));
        score.setCellValueFactory(new PropertyValueFactory<>(resourceBundle.getString("score")));

        //gets all players in scoreboards.
        ArrayList<ScoreboardPlayer> players = new ArrayList<>();
        try {
            players = new Scoreboard(0).getAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //adds players to scoreboard table.
        for (ScoreboardPlayer player : players) {
            scoreboard.getItems().add(player);
        }

        scoreboardListener();

        levelSelector.setValue("Level 1");
    }

    /**
     * scoreboardListener() - adds listener to comboBox (when combo box is edited -> do this).
     * <p>
     * levelSelector - combo box
     * when combo box is changed check in row 1 (level row) for all rows that contain new value and show them
     */
    private void scoreboardListener() {
        FilteredList<ScoreboardPlayer> filteredData = new FilteredList<>(scoreboard.getItems(), p -> true);
        levelSelector.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue)
                -> filteredData.setPredicate(myObject -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }

            String lowerCaseFilter = newValue.toLowerCase();
            return String.valueOf(myObject.getLevel()).toLowerCase().contains(lowerCaseFilter);
        }));

        SortedList<ScoreboardPlayer> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(scoreboard.comparatorProperty());
        scoreboard.setItems(sortedData);
    }

}
