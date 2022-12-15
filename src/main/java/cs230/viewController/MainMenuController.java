package cs230.viewController;

import cs230.Application;
import cs230.Themes;
import cs230.external.MOTD;
import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.TileColor;
import cs230.model.board.BoardReaderAndGenerator;
import cs230.model.board.StaticBoard;
import cs230.model.entity.Entity;
import cs230.model.entity.Player;
import cs230.model.entity.enemies.FloorFollowingThief;
import cs230.model.entity.enemies.FlyingAssassin;
import cs230.model.entity.enemies.SmartThief;
import cs230.model.entity.enums.Direction;
import cs230.model.entity.enums.GateLeverColour;
import cs230.model.entity.items.Bomb;
import cs230.model.entity.items.Clock;
import cs230.model.entity.items.Door;
import cs230.model.entity.items.Gate;
import cs230.model.entity.items.Lever;
import cs230.model.entity.items.Loot;
import cs230.model.entity.items.Note;
import cs230.profiles.Profile;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.Scanner;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The MainMenuController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 * @author Oliver
 */
public class MainMenuController implements Initializable {

    /**
     * Global resources bundle.
     */
    private final ResourceBundle globalResources = ResourceBundle.getBundle("cs230.resource.Global");
    /**
     * Select theme for game
     */
    @FXML
    private ComboBox<String> themeSelector;
    /**
     * Continue game button.
     */
    @FXML
    public Button continueGameButton;
    /**
     * The New game button.
     */
    @FXML
    public Button newGameButton;
    /**
     * The Settings button.
     */
    @FXML
    public Button settingsButton;
    /**
     * The Load button.
     */
    @FXML
    public Button loadButton;
    /**
     * The Exit button.
     */
    @FXML
    public Button exitButton;
    /**
     * Display current profile name
     */
    @FXML
    private Label currentProfileLabel;
    /**
     * Display Title of game.
     */
    @FXML
    private Label titleText;
    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;
    /**
     * Display Message of the day
     */
    @FXML
    private Label motdText;
    /**
     * Vertical box used to hold UI components
     */
    @FXML
    private VBox motdPane;
    /**
     * Save file
     */
    private File file;

    /**
     * Allows the player to navigate any aspect of the program.
     *
     * @param url URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @FXML
    public void initialize(final URL url, final ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        continueGameButton.setDisable(true);

        themeSelector.getItems().addAll("MixedThemes", "Winter", "Party", "Autumn", "Jungle");
        if (Themes.currentTheme == null) {
            themeSelector.setValue("MixedThemes");
        } else {
            themeSelector.setValue(Themes.currentTheme.toString());
        }

        themeSelector.getSelectionModel().selectedItemProperty().addListener((options, oldValue, newValue) -> {
            if (newValue != null) {
                Themes.currentTheme = txtToType(newValue);
            }
        });

        if (FXMLController.firstLoad) {
            try {
                Settings.playMusic();
            } catch (IOException | UnsupportedAudioFileException | LineUnavailableException e) {
                e.printStackTrace();
            }
        }
        titleText.setText(globalResources.getString("gameName"));

        if (Profile.getProfile() == null) {
            newGameButton.setDisable(true);
            loadButton.setDisable(true);
        } else {
            currentProfileLabel.setText(resources.getString("profile") + " " + Profile.getProfile().getName());

            file = new File(System.getProperty("user.dir")
                    + "\\src\\main\\resources\\cs230\\config\\saves\\save-" + Profile.getProfile().getName() + ".txt");
            continueGameButton.setDisable(!file.exists());
        }

        // Update motd
        try {
            MOTD motd = new MOTD();
            String motdMessage = motd.getMessage();
            motdText.setText(motdMessage);
        } catch (Exception e) {
            motdPane.setVisible(false);
        }
    }

    /**
     * Closes the application.
     */
    @FXML
    protected void onExitButtonClick() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, resources.getString("exitConfirmation"),
                ButtonType.YES, ButtonType.NO);
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(Objects.requireNonNull(Application.class.getResource(
                "css/application.css")).toExternalForm());
        dialogPane.getStyleClass().add("customDialog");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            Platform.exit();
        }
    }

    /**
     * Loads credit FXML.
     */
    public void onCreditsButtonClick() {
        FXMLController.openCredits(getThisStage());
    }

    /**
     * gets this fxml stage
     *
     * @return stage
     */
    private Stage getThisStage() {
        return (Stage) settingsButton.getScene().getWindow();
    }

    /**
     * Loads new game on click.
     *
     * @throws IOException the io exception
     */
    @FXML
    protected void onNewGameButtonClick() throws IOException {
        Themes.currentTheme = txtToType(themeSelector.getSelectionModel().getSelectedItem());
        StaticBoard.reader = new BoardReaderAndGenerator(1);
        StaticBoard.board = StaticBoard.reader.readBoard(false);
        Profile.getRandomCharacterDetails();

        FXMLController.openGame(getThisStage());
    }

    /**
     * converts text to actual theme
     *
     * @param s string of theme
     * @return Theme
     */
    private Themes.THEME_TYPE txtToType(String s) {
        Themes.THEME_TYPE type;

        switch (s) {
            case "MixedThemes" -> type = Themes.THEME_TYPE.MixedThemes;
            case "Winter" -> type = Themes.THEME_TYPE.Winter;
            case "Party" -> type = Themes.THEME_TYPE.Party;
            case "Autumn" -> type = Themes.THEME_TYPE.Autumn;
            default -> type = Themes.THEME_TYPE.Jungle;
        }

        return type;
    }


    /**
     * Loads save file on click.
     *
     * @throws FileNotFoundException file isn't found
     */
    @FXML
    protected void onContinueClicked() throws FileNotFoundException {
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }

        assert scanner != null;
        Themes.currentTheme = txtToType(scanner.next());
        Profile.identity = scanner.next();

        boolean[] notes = new boolean[Settings.NUMBER_OF_NOTES];
        for (int i = 0; i < Settings.NUMBER_OF_NOTES; i++) {
            String note = scanner.next();

            if (i < (Settings.NUMBER_OF_NOTES - 1)) {
                notes[i] = Boolean.parseBoolean(note.substring(0, note.length() - 1));
            } else {
                notes[i] = Boolean.parseBoolean(note);
            }
        }
        Profile.notesCollected = notes;
        Profile.getCharacterDetails(Profile.identity);

        String[] playerSplit = scanner.next().split(":");
        String[] playerCoords = playerSplit[1].split(",");
        String[] playerDir = playerCoords[1].split(";");

        Player player = new Player(Direction.getDirectionRegex(playerDir[1]), Integer.parseInt(playerCoords[0]),
                Integer.parseInt(playerDir[0]));
        int level = scanner.nextInt();
        int remaining = scanner.nextInt();
        int score = scanner.nextInt();

        StaticBoard.reader = new BoardReaderAndGenerator(level);
        BoardReaderAndGenerator r = StaticBoard.reader;
        try {
            r.readBoard(true);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        StaticBoard.board = new Board(r.getTiles(), r.getSizeX(), r.getSizeY(), level,
                player, r.getExpectedTime(), Themes.typeToTheme(level).getColors(), remaining, score);

        while (scanner.hasNext()) {
            String[] split = scanner.next().split(":");
            String[] coords = split[1].split(",");

            switch (split[0]) {
                case "E1" -> {
                    String[] dir = coords[1].split(";");
                    String[] col = dir[1].split("-");

                    createEntity(new FloorFollowingThief(Direction.getDirectionRegex(col[0]),
                            TileColor.getColorRegex(col[1])), Integer.parseInt(coords[0]), Integer.parseInt(dir[0]));
                }
                case "E2" -> {
                    String[] dir = coords[1].split(";");

                    createEntity(new FlyingAssassin(Direction.getDirectionRegex(dir[1])),
                            Integer.parseInt(coords[0]), Integer.parseInt(dir[0]));
                }
                case "E3" -> {
                    String[] dir = coords[1].split(";");

                    SmartThief smart = new SmartThief(Direction.getDirectionRegex(dir[1]));
                    Tile tile = StaticBoard.board.getTile(Integer.parseInt(coords[0]), Integer.parseInt(dir[0]));

                    smart.setStarting(tile);
                    StaticBoard.board.placeEntity(smart, tile);
                }
                case "I3" -> {
                    String[] ticks = coords[1].split(";");
                    createEntity(new Bomb(Integer.parseInt(ticks[1])), Integer.parseInt(coords[0]),
                            Integer.parseInt(ticks[0]));
                }
                default -> createEntity(getEntity(split[0]), Integer.parseInt(coords[0]), Integer.parseInt(coords[1]));
            }
        }
        r.initSmartThieves(StaticBoard.board);
        r.setGatesAndLevers(StaticBoard.board);

        try {
            FXMLController.openGame(getThisStage());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Creates entity on board
     *
     * @param e entity
     * @param x x position
     * @param y y position
     */
    private void createEntity(Entity e, int x, int y) {
        StaticBoard.board.placeEntity(e, StaticBoard.board.getTile(x, y));
    }

    /**
     * Loads load level fxml on click.
     */
    @FXML
    protected void onLoadLevelButtonClick() {
        Themes.currentTheme = txtToType(themeSelector.getSelectionModel().getSelectedItem());
        FXMLController.openLoadLevel(getThisStage());
    }

    /**
     * Loads scoreboard fxml on click.
     */
    @FXML
    protected void onScoreboardButtonClick() {
        FXMLController.openScoreboard(getThisStage());
    }

    /**
     * Loads settings fxml on click.
     */
    @FXML
    protected void onSettingsButtonClick() {
        FXMLController.fromMain = true;
        FXMLController.openSettings(getThisStage());
    }

    /**
     * Loads manage profiles fxml on click.
     */
    @FXML
    protected void onManageProfilesButtonClick() {
        FXMLController.openProfileManager(getThisStage());
    }

    /**
     * Converts string to entity using regex
     *
     * @param regex regex
     * @return entity
     */
    private Entity getEntity(String regex) {
        return switch (regex) {
            case "L1" -> new Loot(Loot.LOOT_TYPE.CENT);
            case "L2" -> new Loot(Loot.LOOT_TYPE.DOLLAR);
            case "L3" -> new Loot(Loot.LOOT_TYPE.RUBY);
            case "L4" -> new Loot(Loot.LOOT_TYPE.DIAMOND);
            case "I1" -> new Door();
            case "I2" -> new Clock();
            case "I3" -> new Bomb();
            case "RL" -> new Lever(GateLeverColour.RED);
            case "GL" -> new Lever(GateLeverColour.GREEN);
            case "BL" -> new Lever(GateLeverColour.BLUE);
            case "RG" -> new Gate(GateLeverColour.RED);
            case "GG" -> new Gate(GateLeverColour.GREEN);
            case "NO" -> new Note();
            default -> new Gate(GateLeverColour.BLUE);
        };
    }

}
