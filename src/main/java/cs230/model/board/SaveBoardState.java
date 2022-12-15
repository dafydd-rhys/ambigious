package cs230.model.board;

import cs230.Themes;
import cs230.model.Board;
import cs230.model.entity.Entity;
import cs230.model.entity.Player;
import cs230.profiles.Profile;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * This class saves the current state of board, used when exiting application.
 *
 * @author Dafydd-Rhys Maund (2003900)
 */
public class SaveBoardState {

    /**
     * Instantiates a new Save board state.
     *
     * @param board the board wanted to be saved
     * @throws IOException unable to write to file
     */
    public SaveBoardState(Board board) throws IOException {
        String save = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\config\\saves\\save-";
        File file = new File(save + Profile.getProfile().getName() + ".txt");

        String notes = Arrays.toString(Profile.notesCollected);

        PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);

        writer.println(Themes.currentTheme);
        writer.println(Profile.identity);
        writer.println(notes.substring(1, notes.length() - 1));
        writer.println(board.getPlayer().toString());
        writer.println(board.getCurrentLevel());
        writer.println(board.getTimeRemaining());
        writer.println(board.getPlayer().getScore());

        for (Entity entity : board.getEntities()) {
            if (!(entity instanceof Player)) {
                writer.print(entity);
            }
        }

        writer.close();
    }

}
