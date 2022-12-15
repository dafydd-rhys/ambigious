package cs230.viewController;

import cs230.Application;
import cs230.external.LocalizationManager;
import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.board.SaveBoardState;
import cs230.model.board.StaticBoard;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * The FXMLController used to manage UI interactions of every FXML.
 *
 * @author Dafydd-Rhys Maund
 */
public class FXMLController {

    /**
     * Global resources to be used on any FXML.
     */
    private static ResourceBundle globalResources = ResourceBundle.getBundle("cs230.resource.Global",
            LocalizationManager.getCurrentLocale());
    /**
     * The constant firstLoad.
     */
    public static boolean firstLoad = true;
    /**
     * Whether stage is wanted in fullscreen.
     */
    public static boolean fullscreen;
    /**
     * Whether player wants theme FX.
     */
    public static boolean themedFX;
    /**
     * Settings loaded from main menu or game controller
     */
    public static boolean fromMain = false;
    /**
     * Game window dimensions
     */
    private static final int[] GAME_WINDOW_DIM = {1920, 1080};
    /**
     * Credits window dimensions
     */
    private static final int[] CREDITS_WINDOW_DIM = {600, 350};
    /**
     * Main menu window dimensions
     */
    private static final int[] MAIN_MENU_WINDOW_DIM = {640, 510};
    /**
     * Settings window dimensions
     */
    private static final int[] SETTINGS_WINDOW_DIM = {470, 300};
    /**
     * Game results window dimensions
     */
    private static final int[] GAME_RESULTS_WINDOW_DIM = {400, 300};
    /**
     * Profile manager window dimensions
     */
    private static final int[] PROFILE_MANAGER_WINDOW_DIM = {350, 270};
    /**
     * Load level window dimensions
     */
    private static final int[] LOAD_LEVEL_WINDOW_DIM = {350, 140};
    /**
     * Scoreboard window dimensions
     */
    private static final int[] SCOREBOARD_WINDOW_DIM = {500, 370};
    /**
     * Character discovery window dimensions
     */
    private static final int[] CHARACTER_DISCOVERY_WINDOW_DIM = {400, 420};

    /**
     * Open game fxml.
     *
     * @param owner the parent FXML
     * @throws IOException can't find fxml
     */
    public static void openGame(Stage owner) throws IOException {
        ResourceBundle gameBundle = ResourceBundle.getBundle("cs230.resource.Game",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/game.fxml"), gameBundle);

        Stage stage = new Stage();
        stage.setFullScreenExitHint("");
        clickEffect(stage);
        stage.setTitle(globalResources.getString("gameName"));
        stage.setMaximized(true);
        stage.setFullScreen(Settings.isFullscreen());

        fullscreen = Settings.isFullscreen();

        stage.setScene(new Scene(loader.load(), GAME_WINDOW_DIM[0], GAME_WINDOW_DIM[1]));
        stage.centerOnScreen();
        stage.setOnCloseRequest(e -> {
            try {
                saveAndExit(StaticBoard.board);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(1);
        });
        stage.show();
        owner.close();
    }

    /**
     * Open credits fxml.
     *
     * @param owner the parent FXML
     */
    public static void openCredits(Stage owner) {
        ResourceBundle bundle = ResourceBundle.getBundle("cs230.resource.Credits");
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/credits.fxml"), bundle);

        openSubWindow(new Stage(), bundle, owner, CREDITS_WINDOW_DIM[0], CREDITS_WINDOW_DIM[1], loader);
    }

    /**
     * Open main menu fxml.
     *
     * @param stage the FXML
     * @throws IOException can't find fxml
     */
    public static void openMainMenu(Stage stage) throws IOException {
        ResourceBundle bundle = ResourceBundle.getBundle("cs230.resource.MainMenu",
                LocalizationManager.getCurrentLocale());
        FXMLLoader fxmlLoader = new FXMLLoader(Application.class.getResource("fxml/main-menu.fxml"), bundle);
        Scene scene = new Scene(fxmlLoader.load(), MAIN_MENU_WINDOW_DIM[0], MAIN_MENU_WINDOW_DIM[1]);

        stage.setTitle(globalResources.getString("gameName"));
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.setScene(scene);

        stage.getIcons()
                .add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/jewels-256.png"))));
        stage.getIcons()
                .add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/jewels-48.png"))));
        stage.getIcons()
                .add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/jewels-32.png"))));
        stage.getIcons()
                .add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/jewels-24.png"))));
        stage.getIcons()
                .add(new Image(Objects.requireNonNull(Application.class.getResourceAsStream("icons/jewels-16.png"))));

        clickEffect(stage);
        stage.setOnCloseRequest(e -> System.exit(1));
        stage.show();
        stage.centerOnScreen();
    }

    /**
     * Open settings fxml.
     *
     * @param owner the parent FXML
     */
    public static void openSettings(Stage owner) {
        ResourceBundle settingsBundle = ResourceBundle.getBundle("cs230.resource.Settings",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/settings.fxml"), settingsBundle);

        openSubWindow(new Stage(), settingsBundle, owner, SETTINGS_WINDOW_DIM[0], SETTINGS_WINDOW_DIM[1], loader);
    }

    /**
     * Open game result fxml.
     *
     * @param owner the parent FXML
     */
    public static void openGameResult(Stage owner) {
        ResourceBundle resultBundle = ResourceBundle.getBundle("cs230.resource.GameResult",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/game-result.fxml"), resultBundle);

        Stage stage = new Stage();
        stage.setOnCloseRequest(e -> System.exit(1));
        Platform.runLater(() -> openSubWindow(stage, resultBundle, owner,
                GAME_RESULTS_WINDOW_DIM[0], GAME_RESULTS_WINDOW_DIM[1], loader));
    }

    /**
     * Open profile manager fxml.
     *
     * @param owner the parent FXML
     */
    public static void openProfileManager(Stage owner) {
        ResourceBundle profileBundle = ResourceBundle.getBundle("cs230.resource.ProfileManager",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/profile-manager.fxml"), profileBundle);

        Platform.runLater(() -> openSubWindow(new Stage(), profileBundle, owner,
                PROFILE_MANAGER_WINDOW_DIM[0], PROFILE_MANAGER_WINDOW_DIM[1], loader));
    }

    /**
     * Opens FXML window.
     *
     * @param stage  stage wanted to be opened
     * @param bundle bundle of stage
     * @param owner  parent of stage
     * @param width  width of stage
     * @param height height of stage
     * @param loader fxml loader for stage
     */
    private static void openSubWindow(Stage stage, ResourceBundle bundle, Stage owner,
            int width, int height, FXMLLoader loader) {
        stage.setTitle(bundle.getString("title"));
        stage.setResizable(false);
        stage.initOwner(owner);
        stage.initModality(Modality.WINDOW_MODAL);
        clickEffect(stage);

        try {
            stage.setScene(new Scene(loader.load(), width, height));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        stage.showAndWait();
    }

    /**
     * Open load level fxml.
     *
     * @param owner the parent FXML
     */
    public static void openLoadLevel(Stage owner) {
        ResourceBundle loadLevelBundle = ResourceBundle.getBundle("cs230.resource.LevelLoad",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/level-load.fxml"), loadLevelBundle);

        openSubWindow(new Stage(), loadLevelBundle, owner, LOAD_LEVEL_WINDOW_DIM[0], LOAD_LEVEL_WINDOW_DIM[1], loader);
    }

    /**
     * Open scoreboard fxml.
     *
     * @param owner the parent FXML
     */
    public static void openScoreboard(Stage owner) {
        ResourceBundle scoreboardBundle = ResourceBundle.getBundle("cs230.resource.Scoreboard",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/scoreboard.fxml"), scoreboardBundle);

        openSubWindow(new Stage(), scoreboardBundle, owner, SCOREBOARD_WINDOW_DIM[0], SCOREBOARD_WINDOW_DIM[1], loader);
    }

    /**
     * Open character discover fxml.
     *
     * @param owner the parent FXML
     */
    public static void openCharacterDiscovery(Stage owner) {
        ResourceBundle characterBundle = ResourceBundle.getBundle("cs230.resource.CharacterDiscovery",
                LocalizationManager.getCurrentLocale());
        FXMLLoader loader = new FXMLLoader(Application.class.getResource("fxml/character-discovery.fxml"),
                characterBundle);

        Stage stage = new Stage();
        stage.setOnCloseRequest(e -> System.exit(1));
        Platform.runLater(() -> openSubWindow(stage, characterBundle, owner,
                CHARACTER_DISCOVERY_WINDOW_DIM[0], CHARACTER_DISCOVERY_WINDOW_DIM[1], loader));
    }

    /**
     * Plays click effect when a node on stage is clicked
     *
     * @param stage stage
     */
    private static void clickEffect(Stage stage) {
        if (stage.getScene() != null) {
            for (Node node : getAllNodes(stage.getScene().getRoot())) {
                node.setOnMousePressed(e -> {
                    try {
                        Settings.clickEffect();
                    } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                        ex.printStackTrace();
                    }
                });
            }
        }
    }

    /**
     * Gets all nodes in FXML
     *
     * @param root parent of FXML
     * @return nodes in FXML
     */
    private static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<>();
        addAllDescendants(root, nodes);

        return nodes;
    }

    /**
     * adds all descendants of parent
     *
     * @param parent parent node
     * @param nodes  current nodes of fxml
     */
    private static void addAllDescendants(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllDescendants((Parent) node, nodes);
            }
        }
    }

    /**
     * Save the current board state.
     *
     * @param board the board
     * @throws IOException can't write to .txt
     */
    public static void saveAndExit(Board board) throws IOException {
        new SaveBoardState(board);
    }

}
