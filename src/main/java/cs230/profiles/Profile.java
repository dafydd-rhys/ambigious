package cs230.profiles;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Handles the current profile of the user.
 *
 * @author Dafydd-Rhys Maund
 */
public class Profile {

    /**
     * Total number of identity-notes, x1 per level
     */
    private static final int TOTAL_NUMBER_OF_NOTES = 8;
    /**
     * The current profile.
     */
    private static Profile profile;
    /**
     * Name of profile.
     */
    private String name;
    /**
     * Max level this profile has unlocked.
     */
    private int maxLevel;
    /**
     * Profile.txt used to gather profile info.
     */
    private static final String filepath =
            System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\config\\profiles.txt";
    /**
     * Identity this profile is trying to discover.
     */
    public static String identity;
    /**
     * The constant notesCollected.
     */
    public static boolean[] notesCollected = new boolean[TOTAL_NUMBER_OF_NOTES];
    /**
     * The Notes data.
     */
    public static String[] notesData = {"", "", "", "", "", "", "", ""};

    /**
     * Instantiates a new Profile.
     *
     * @param name  the name
     * @param level the level
     */
    public Profile(String name, int level) {
        this.name = name;
        this.maxLevel = level;
    }

    /**
     * Unlocks new level for player.
     *
     * @param level the level to be unlocked
     * @throws IOException if there's error writing to file
     */
    public static void unlockedNew(final int level) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(filepath));
        ArrayList<String> existingPlayers = new ArrayList<>();

        String line;
        while ((line = reader.readLine()) != null) {
            if (!line.equals(Profile.getProfile().getName() + ";" + (level - 1))) {
                existingPlayers.add(line);
            } else {
                existingPlayers.add(Profile.getProfile().getName() + ";" + level);
                Profile.getProfile().setMaxLevel(level);
            }
        }

        //writes file with new unlocked level
        PrintWriter writer = new PrintWriter(filepath, StandardCharsets.UTF_8);
        for (String player : existingPlayers) {
            writer.println(player);
        }
        writer.close();
    }

    /**
     * Gets character details of a random identity.
     *
     * @throws FileNotFoundException the file not found exception
     */
    public static void getRandomCharacterDetails() throws FileNotFoundException {
        File[] charList = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\cs230\\config\\identities").listFiles();

        if (charList != null) {
            File file = charList[new Random().nextInt(charList.length)];
            identity = file.getName();

            Scanner scanner = new Scanner(file).useDelimiter("\\n");
            for (int i = 0; i < notesData.length; i++) {
                notesData[i] = scanner.nextLine();
            }
        }
    }

    /**
     * Gets character details of a set identity.
     *
     * @param identity identity-? .txt file
     * @throws FileNotFoundException the file not found exception
     */
    public static void getCharacterDetails(String identity) throws FileNotFoundException {
        File file = new File(System.getProperty("user.dir")
                + "\\src\\main\\resources\\cs230\\config\\identities\\" + identity);

        Scanner scanner = new Scanner(file).useDelimiter("\\n");
        for (int i = 0; i < notesData.length; i++) {
            notesData[i] = scanner.nextLine();
        }
    }

    /**
     * Gets name of profile.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name of profile.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets max level of profile.
     *
     * @return the max level
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Sets max level of profile.
     *
     * @param currentLevel the current level
     */
    public void setMaxLevel(int currentLevel) {
        this.maxLevel = currentLevel;
    }

    /**
     * Sets profile.
     *
     * @param profile the profile
     */
    public static void setProfile(Profile profile) {
        Profile.profile = profile;
    }

    /**
     * Gets profile.
     *
     * @return the profile
     */
    public static Profile getProfile() {
        return profile;
    }

    /**
     * Prints string that encapsulates data needed to save this profile on case of user exiting.
     *
     * @return the data about this enemy
     */
    @Override
    public String toString() {
        return name + ": Level " + maxLevel;
    }

}
