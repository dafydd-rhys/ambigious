package cs230.viewController;

import cs230.Themes;
import cs230.external.Settings;
import cs230.model.Board;
import cs230.model.TileColor;
import cs230.model.board.StaticBoard;
import cs230.model.entity.Enemy;
import cs230.model.entity.Entity;
import cs230.model.entity.enums.Direction;
import cs230.model.entity.items.Bomb;
import cs230.profiles.Profile;
import cs230.scoreboard.Score;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

/**
 * The GameController used to manage UI interactions of this FXML.
 *
 * @author Dafydd-Rhys Maund
 * @author Oliver
 */
public class GameController implements Initializable {

    /**
     * Canvas game is drawn on.
     */
    @FXML
    private Canvas gameCanvas;
    /**
     * Displays current board level.
     */
    @FXML
    private Label level;
    /**
     * Displays current board score.
     */
    @FXML
    private Label score;
    /**
     * Displays current board time remaining.
     */
    @FXML
    private Label timeRemaining;
    /**
     * Restart image
     */
    @FXML
    private ImageView restart;
    /**
     * Settings image
     */
    @FXML
    private ImageView settings;
    /**
     * Pause/Play image
     */
    @FXML
    private ImageView pausePlay;
    /**
     * Big play image
     */
    @FXML
    private ImageView bigPlay;
    /**
     * Main menu button
     */
    @FXML
    private Button main;
    /**
     * Exit game button
     */
    @FXML
    private Button exit;
    /**
     * Theme gif
     */
    @FXML
    private ImageView themePane;
    /**
     * Resource bundle for this fxml.
     */
    @FXML
    private ResourceBundle resources;

    /**
     * GraphicsContext used to draw on canvas
     */
    private GraphicsContext context;
    /**
     * Frame rate of game
     */
    private final int FRAME_RATE = 30;
    /**
     * Identifies whether game is paused
     */
    private boolean paused = false;
    /**
     * Identifies whether UI is focused
     */
    private boolean uiFocused = false;
    /**
     * Color of board
     */
    private final Paint boardColor = Paint.valueOf("#a3815a");
    /**
     * Color of board border
     */
    private final Paint boardBorderColor = Paint.valueOf("#edd6bb");
    /**
     * Board that is being played on
     */
    private Board board;
    /**
     * Colors used on board
     */
    private Paint[] tileColors;
    /**
     * Current tick of the game.
     */
    private int currentTick = 0;
    /**
     * Cell size of board tiles.
     */
    private double cellSize;
    /**
     * Padding on sprites
     */
    private final double spritePadding = 0.2;
    /**
     * distance from window to board X axis
     */
    private double boardLeft;
    /**
     * distance from window to board Y axis
     */
    private double boardTop;
    /**
     * width between board and initial tiles x and y
     */
    private final int tileBorderWidth = 2; // width of each tile's border in px

    /**
     * clears canvas
     */
    private void clearCanvas() {
        context.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
    }

    /**
     * updates canvas size based on window size
     */
    private void updateCanvasSize() {
        gameCanvas.setWidth(getThisStage().getWidth() - 2 * 10);
        gameCanvas.setHeight(getThisStage().getHeight() - 10 - 110);
    }

    /**
     * Updates game UI based on inputs and entity interactions
     */
    private void updateCanvas() {
        if (getThisStage() == null || gameCanvas == null) {
            return;
        }

        updateCanvasSize();
        clearCanvas();

        // Size of the level's board (e.g. 5 x 10)
        int boardHeight = board.getHeight();
        int boardWidth = board.getWidth();

        // Size of the board that is drawn to the screen.
        // This is dependent on the size of the canvas.
        // "Board border" is drawn outside the border.
        double boardBorderDrawHeight;
        double boardBorderDrawWidth;

        double boardDrawHeight;
        double boardDrawWidth;

        // Size (height/width) of each individual tile.
        //double cellSize;

        int boardBorderSize = 10; // width of the board's border in px
        double minDistanceFromEdge = 0.15; // minimum distance of the board's border from the canvas edge (%)

        if (boardHeight > boardWidth) {
            boardBorderDrawHeight = (int) gameCanvas.getHeight() - (2 * minDistanceFromEdge * getThisStage().getHeight());
            boardDrawHeight = boardBorderDrawHeight - (2 * boardBorderSize);
            cellSize = (boardDrawHeight - ((boardHeight + 1) * tileBorderWidth)) / boardHeight;
            boardDrawWidth = cellSize * boardWidth + ((boardWidth + 1) * tileBorderWidth);
            boardBorderDrawWidth = boardDrawWidth + (2 * boardBorderSize);
        } else {
            boardBorderDrawWidth = (int) gameCanvas.getWidth() - (2 * minDistanceFromEdge * getThisStage().getWidth());
            boardDrawWidth = boardBorderDrawWidth - (2 * boardBorderSize);
            cellSize = (boardDrawWidth - ((boardWidth + 1) * tileBorderWidth)) / boardWidth;
            boardDrawHeight = cellSize * boardHeight + ((boardHeight + 1) * tileBorderWidth);
            boardBorderDrawHeight = boardDrawHeight + (2 * boardBorderSize);
        }

        // Draw border around board.
        context.setFill(boardBorderColor);
        double borderLeft = (gameCanvas.getWidth() - boardBorderDrawWidth) / 2;
        double borderTop = (gameCanvas.getHeight() - boardBorderDrawHeight) / 2;
        context.fillRect(borderLeft, borderTop, boardBorderDrawWidth, boardBorderDrawHeight);

        // Draw board.
        boardLeft = borderLeft + boardBorderSize;
        boardTop = borderTop + boardBorderSize;

        context.setFill(boardColor);
        context.fillRect(boardLeft,
                boardTop,
                boardDrawWidth,
                boardDrawHeight);

        resizeImage(themePane, borderLeft, borderTop, boardBorderSize, boardBorderDrawWidth);
        resizeImage(bigPlay, borderLeft, borderTop, boardBorderSize, boardBorderDrawWidth);
        timeRemaining.setLayoutX(getThisStage().getWidth() / 2 - 135);
        themePane.setVisible(Settings.isFXEnabled());
        getThisStage().setFullScreen(FXMLController.fullscreen);

        double tileTop;
        double tileLeft;
        double subTileTop;
        double subTileLeft;
        // Draw tiles
        for (int row = 0; row < boardHeight; row++) {
            for (int col = 0; col < boardWidth; col++) {
                tileTop = boardTop + (row * cellSize) + ((row + 1) * tileBorderWidth);
                tileLeft = boardLeft + (col * cellSize) + ((col + 1) * tileBorderWidth);

                // Draw the four sub-tiles within each tile.
                TileColor[] colors = board.getTile(col, row).getSubTiles();
                for (int subTile = 0; subTile < colors.length; subTile++) {
                    context.setFill(getColor(colors[subTile]));

                    subTileLeft = tileLeft + (subTile % 2 == 0 ? 0 : cellSize / 2.0);
                    subTileTop = tileTop + (subTile < 2 ? 0 : cellSize / 2.0);

                    context.fillRect(subTileLeft, subTileTop, cellSize / 2.0, cellSize / 2.0);
                }
            }
        }

        //draw player last (needs to be on top)
        List<Entity> entities = board.getEntities();
        Collections.reverse(entities);

        // Draw board entities
        for (Entity entity : entities) {
            int entityX = entity.getCurrentPosX();
            int entityY = entity.getCurrentPosY();

            // 0 to 1, where 0 means no padding
            context.drawImage(entity.getImage(),
                    boardLeft + (entityX * cellSize) + ((entityX + 1) * tileBorderWidth) + (spritePadding * 0.5 * cellSize),
                    boardTop + (entityY * cellSize) + ((entityY + 1) * tileBorderWidth) + (spritePadding * 0.5 * cellSize),
                    // Sprite size is scaled depending on the spritePadding (0-1).
                    cellSize * (1 - spritePadding),
                    cellSize * (1 - spritePadding)
            );
        }
    }

    /**
     * Resizes image new board size'
     *
     * @param image image
     * @param bX    board layout X size
     * @param bY    board layout Y size
     * @param bS    border size
     * @param bW    board width size
     */
    private void resizeImage(ImageView image, double bX, double bY, int bS, double bW) {
        image.setLayoutX(bX + gameCanvas.getLayoutX() + bS);
        image.setLayoutY(bY + gameCanvas.getLayoutY() + bS);
        image.setFitWidth(bW - (2 * bS));
    }

    /**
     * gets color relative to type
     *
     * @param color color from file
     * @return returns color
     */
    private Paint getColor(TileColor color) {
        if (color == TileColor.COL1) {
            return tileColors[0];
        } else if (color == TileColor.COL2) {
            return tileColors[1];
        } else if (color == TileColor.COL3) {
            return tileColors[2];
        } else if (color == TileColor.COL4) {
            return tileColors[3];
        } else if (color == TileColor.COL5) {
            return tileColors[4];
        } else {
            return tileColors[5];
        }
    }

    /**
     * Gets this stage
     *
     * @return stage
     */
    private Stage getThisStage() {
        return (Stage) gameCanvas.getScene().getWindow();
    }

    /**
     * Initializes the board, places entities in correct positions and lets UI know tile colors and
     * sets up timer task which runs and updates the UI relative to frame rate.
     *
     * @param url            URL of fxml
     * @param resourceBundle Resource Bundle of fxml
     */
    @FXML
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.resources = resourceBundle;
        themePane.setPreserveRatio(true);
        restart.setCursor(Cursor.HAND);
        pausePlay.setCursor(Cursor.HAND);
        settings.setCursor(Cursor.HAND);

        pausePlay.setOnMouseClicked(e -> changePause(!paused));
        bigPlay.setOnMouseClicked(e -> changePause(false));

        settings.setOnMouseClicked(e -> {
            FXMLController.fromMain = false;
            changePause(true);
            FXMLController.openSettings(getThisStage());
        });

        context = gameCanvas.getGraphicsContext2D();
        board = StaticBoard.board;
        tileColors = board.getColors();

        if (Themes.currentTheme == Themes.THEME_TYPE.MixedThemes) {
            int x = 0;
            if (board.getCurrentLevel() > 4) {
                x = 4;
            }

            themePane.setImage(new Image(System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\themes\\" +
                    (board.getCurrentLevel() - x) + ".gif"));
        } else {
            themePane.setImage(Themes.typeToTheme(board.getCurrentLevel()).getGIF());
        }
        themePane.setVisible(Settings.isFXEnabled());

        restart.setOnMouseClicked(e -> {
            currentTick = 0;
            changePause(false);

            try {
                board = StaticBoard.reader.readBoard(false);
            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
        });

        timeRemaining.setText("Time Remaining: " + board.getTimeRemaining());
        score.setText("Score: " + board.getPlayer().getScore());
        level.setText("Level: " + board.getCurrentLevel());

        Timer ticker = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                Platform.runLater(() -> {
                    updateCanvas();

                    if (!paused && !uiFocused) {
                        if (board.getTimeRemaining() == 0) {
                            try {
                                gameLost();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            uiFocused = true;
                        }

                        if (board.isGameWon()) {
                            try {
                                gameWon();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            uiFocused = true;
                        } else if (board.isPlayerDead()) {
                            try {
                                gameLost();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            uiFocused = true;
                        }

                        currentTick++;
                        board.setTick(currentTick);
                        for (int i = 0; i < board.getEntities().size(); i++) {
                            if (board.getEntities().get(i) instanceof Bomb bomb) {
                                bomb.checkForPlayersAndEnemies(board);

                                if (bomb.hasExploded) {
                                    drawBombExplosion(bomb);
                                }
                            }
                        }

                        //every second
                        if ((currentTick * (1000 / FRAME_RATE) % 990) == 0) {
                            board.addSecondCurrentTime();
                            if (board.getTimeRemaining() >= 0) {
                                timeRemaining.setText("Time Remaining: " + board.getTimeRemaining());
                            }

                            List<Entity> entities = board.getEntities();

                            for (int i = 0; i < entities.size(); i++) {
                                Entity entity = entities.get(i);

                                if (entity instanceof Enemy enemy) {
                                    enemy.move(board);
                                } else if (entity instanceof Bomb bomb) {
                                    if (bomb.isTicking()) {
                                        bomb.tick();
                                    }

                                    if (bomb.hasExploded) {
                                        drawBombExplosion(bomb);
                                    }
                                }
                            }
                        }

                        gameCanvas.getScene().setOnKeyPressed(e -> {
                            if (!paused) {
                                switch (e.getCode()) {
                                    case W -> board.getPlayer().move(board, Direction.UP);
                                    case A -> board.getPlayer().move(board, Direction.LEFT);
                                    case S -> board.getPlayer().move(board, Direction.DOWN);
                                    case D -> board.getPlayer().move(board, Direction.RIGHT);
                                }
                            }

                            if (e.getCode() == KeyCode.ESCAPE) {
                                FXMLController.fullscreen = !FXMLController.fullscreen;
                            } else if (e.getCode() == KeyCode.F11) {
                                FXMLController.fullscreen = true;
                            } else if (e.getCode() == KeyCode.R) {
                                currentTick = 0;
                                changePause(false);

                                try {
                                    board = StaticBoard.reader.readBoard(false);
                                } catch (FileNotFoundException ex) {
                                    ex.printStackTrace();
                                }
                            }
                        });
                        score.setText("Score: " + board.getPlayer().getScore());
                    }
                });
            }
        };
        //run tickers task every 500ms until stopped
        ticker.schedule(task, 1000 / FRAME_RATE, 1000 / FRAME_RATE);

        main.setOnMouseClicked(e -> {
            try {
                uiFocused = true;
                FXMLController.saveAndExit(board);
                FXMLController.openMainMenu(getThisStage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        exit.setOnMouseClicked(e -> {
            try {
                FXMLController.saveAndExit(board);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            System.exit(1);
        });
    }

    /**
     * Draws bomb explosion on canvas
     *
     * @param bomb the bomb that is exploding
     */
    private void drawBombExplosion(Bomb bomb) {
        int bombX = bomb.getCurrentPosX();
        int bombY = bomb.getCurrentPosY();

        // Draw an explosion sprite on every tile on the same row and column as the bomb.
        for (int i = 0; i < board.getWidth(); i++) {
            context.drawImage(
                    bomb.getResources().bomb_exp,
                    boardLeft + (i * cellSize) + ((i + 1) * tileBorderWidth),
                    boardTop + cellSize * bombY + (bombY + 1) * tileBorderWidth,
                    cellSize * (1 - spritePadding),
                    cellSize * (1 - spritePadding)
            );
            bomb.remove(board.getTile(i, bombY).getEntities(), board);

            if (board.getTile(i, bombY).getItem() instanceof Bomb b) {
               b.explode();
            }
        }

        for (int i = 0; i < board.getHeight(); i++) {
            context.drawImage(
                    bomb.getResources().bomb_exp,
                    boardLeft + cellSize * bombX + (bombX + 1) * tileBorderWidth,
                    boardTop + (i * cellSize) + ((i + 1) * tileBorderWidth),
                    cellSize * (1 - spritePadding),
                    cellSize * (1 - spritePadding)
            );
            bomb.remove(board.getTile(bombX, i).getEntities(), board);

            if (board.getTile(bombX, i).getItem() instanceof Bomb b) {
                b.explode();
            }
        }
        bomb.destroy(board);
    }

    /**
     * Updates pause state of game.
     *
     * @param bool whether game is paused
     */
    private void changePause(boolean bool) {
        paused = bool;

        String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\images\\";
        if (paused) {
            bigPlay.setVisible(true);
            bigPlay.setDisable(false);
            pausePlay.setImage(new Image(dir + "play.png"));
        } else {
            bigPlay.setDisable(true);
            bigPlay.setVisible(false);
            pausePlay.setImage(new Image(dir + "pause.png"));
        }
    }

    /**
     * Updates profile variables and opens game result fxml.
     *
     * @throws IOException the io exception
     */
    public void gameWon() throws IOException {
        board.getEntities().clear();
        StaticBoard.won = true;
        StaticBoard.level = board.getCurrentLevel();
        board.getPlayer().setScore(board.getPlayer().getScore() + board.getTimeRemaining() * 3);

        new Score(StaticBoard.level, Profile.getProfile().getName(), board.getPlayer().getScore()).addToScoreBoard();

        if (StaticBoard.level + 1 > Profile.getProfile().getMaxLevel() && StaticBoard.level + 1 < 9) {
            Profile.unlockedNew(StaticBoard.level + 1);
        }

        FXMLController.openGameResult(getThisStage());
    }

    /**
     * Opens game result fxml.
     */
    public void gameLost() throws IOException {
        board.getEntities().clear();
        StaticBoard.won = false;
        StaticBoard.level = board.getCurrentLevel();

        new Score(StaticBoard.level, Profile.getProfile().getName(), board.getPlayer().getScore()).addToScoreBoard();

        FXMLController.openGameResult(getThisStage());
    }

}
