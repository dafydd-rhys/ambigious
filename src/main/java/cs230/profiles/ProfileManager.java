package cs230.profiles;

import cs230.external.Settings;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Profile manager is used to handle profiles - add, delete etc.
 *
 * @author Dafydd-Rhys Maund
 */
public class ProfileManager {

    /**
     * Holds saved profiles.
     */
    private final List<Profile> loadedProfiles;
    /**
     * directory of where profiles are stored
     */
    private static final String profiles = System.getProperty("user.dir")
            + "\\src\\main\\resources\\cs230\\config\\profiles.txt";
    /**
     * directory of where scoreboard data is stored
     */
    private static final String scoreboard = System.getProperty("user.dir")
            + "\\src\\main\\resources\\cs230\\scoreboard\\scoreboard-level";

    /**
     * Instantiates a new Profile manager.
     */
    public ProfileManager() {
        loadedProfiles = loadProfilesFromFile(profiles);
    }

    /**
     * deletes current player.
     *
     * @param profile the profile
     * @throws IOException if there's error writing/reading to file
     */
    public static void deletePlayer(Profile profile) throws IOException {
        ArrayList<File> files = new ArrayList<>();
        files.add(new File(profiles));

        for (int i = 0; i < Settings.NUMBER_OF_LEVELS; i++) {
            files.add(new File(scoreboard + (i + 1) + ".txt"));
        }

        for (File file : files) {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            ArrayList<String> existingPlayers = new ArrayList<>();

            //reads file and adds all players apart from this one
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains(profile.getName() + ";")) {
                    existingPlayers.add(line);
                }
            }

            //writes players without this one
            PrintWriter writer = new PrintWriter(file, StandardCharsets.UTF_8);
            for (String player : existingPlayers) {
                writer.println(player);
            }
            writer.close();
        }
    }

    /**
     * Deletes save file associated with profile.
     *
     * @param profile deleted profile.
     */
    public static void deleteSave(Profile profile) {
        File file = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\cs230\\config\\saves\\save-" + profile.getName() + ".txt");

        if (file.exists()) {
            if (!file.delete()) {
                System.out.println("not deleted");
            }
        }
    }

    /**
     * Save current profile to file.
     */
    public void saveCurrentProfileToFile() {
        try {
            FileWriter myWriter = new FileWriter(profiles, true);
            myWriter.write(Profile.getProfile().getName() + ";" + Profile.getProfile().getMaxLevel() +"\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("Could not save user.");
        }
    }

    /**
     * Load profiles from file array list.
     *
     * @param filepath the filepath
     * @return the array list
     */
    public ArrayList<Profile> loadProfilesFromFile(String filepath) {
        File file = new File(filepath);
        Scanner readFile;

        try {
            readFile = new Scanner(file);
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        }

        ArrayList<Profile> profiles = new ArrayList<>();
        while (readFile.hasNext()) {
            String[] split = readFile.next().split(";");
            String username = split[0];
            int level = Integer.parseInt(split[1]);
            Profile profile = new Profile(username, level);
            profiles.add(profile);
        }
        readFile.close();

        return profiles;
    }

    /**
     * Gets loaded profiles.
     *
     * @return the loaded profiles
     */
    public List<Profile> getLoadedProfiles() {
        return loadedProfiles;
    }

}
