package cs230.external;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Handles user settings
 *
 * @author Dafydd-Rhys Maund
 */
public class Settings {

    /**
     * Number of current levels in the game
     */
    public static final int NUMBER_OF_LEVELS = 8;
    /**
     * Number of notes in the game
     */
    public static final int NUMBER_OF_NOTES = 8;
    /**
     * represent default value for volume.
     */
    private static final float DEFAULT_VALUE = 50f;
    /**
     * The music value produced by the slider in settings.
     */
    private static float music = DEFAULT_VALUE;
    /**
     * The effects value produced by the slider in settings.
     */
    private static float effects = DEFAULT_VALUE;
    /**
     * represent value for 1% volume.
     */
    private static final float POINT_OF_VOLUME = 0.5f;
    /**
     * Value which represents muted.
     */
    private static final float MUTED = -50f;
    /**
     * The calculated volume of music.
     */
    private static float musicVolume = MUTED + (music * POINT_OF_VOLUME);
    /**
     * The calculated volume of effects.
     */
    private static float effectsVolume = MUTED + (music * POINT_OF_VOLUME);
    /**
     * Whether music is currently muted.
     */
    private static boolean musicMuted = false;
    /**
     * Whether effects is currently muted.
     */
    private static final boolean effectsMuted = false;
    /**
     * The clip used to play music.
     */
    private static Clip musicClip = null;
    /**
     * host directory of resources used.
     */
    private static final String dir = System.getProperty("user.dir") + "\\src\\main\\resources\\cs230\\";
    /**
     * background music file.
     */
    private static final File musicAudio = new File(dir + "audio\\music.wav");
    /**
     * click sound effect.
     */
    private static final File click = new File(dir + "audio\\click.wav");
    /**
     * settings file, holds current user settings.
     */
    private static final File settings = new File(dir + "config\\settings.txt");
    /**
     * settings file, holds current user settings.
     */
    private static String language;
    /**
     * settings file, holds current user settings.
     */
    private static boolean fullscreen;
    /**
     * settings file, holds current user settings.
     */
    private static boolean themedFX;

    /**
     * This method gathers the users settings.
     *
     * @throws IOException the io exception
     */
    public static void initialize() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(settings));
        boolean validLanguageSpecified = false;
        String line;

        while ((line = reader.readLine()) != null) {
            String[] split = line.split(":");
            if (Objects.equals(split[0], "language")) {

                // Locale
                // 1. Get locale from settings.
                // 2. If supported, use that locale.
                // 3. Otherwise, get default system locale.
                // 4. If not supported, fall back to English.

                String langString = split[1];
                language = langString;
                Locale settingsLocale = Locale.forLanguageTag(langString);

                if (LocalizationManager.localeIsSupported(settingsLocale)) {
                    LocalizationManager.setCurrentLocale(settingsLocale);
                    validLanguageSpecified = true;
                } else {
                    validLanguageSpecified = false;
                }
            } else if (Objects.equals(split[0], "fullscreen")) {
                fullscreen = Boolean.parseBoolean(split[1]);
            } else if (Objects.equals(split[0], "themedFX")) {
                themedFX = Boolean.parseBoolean(split[1]);
            } else if (Objects.equals(split[0], "musicVolume")) {
                music = Float.parseFloat(split[1]);
                musicVolume = MUTED + (music * POINT_OF_VOLUME);
            } else {
                effects = Float.parseFloat(split[1]);
                effectsVolume = MUTED + (effects * POINT_OF_VOLUME);
            }
        }

        if (!validLanguageSpecified) {
            // Get system language
            Locale systemLocale = LocalizationManager.getSystemLocale();
            if (LocalizationManager.localeIsSupported(systemLocale)) {
                LocalizationManager.setCurrentLocale(systemLocale);
            } else {
                LocalizationManager.setCurrentLocale(LocalizationManager.FALLBACK_LOCALE);
            }
        }
    }

    /**
     * Plays background music.
     *
     * @throws IOException                   the io exception
     * @throws UnsupportedAudioFileException the unsupported audio file exception
     * @throws LineUnavailableException      the line unavailable exception
     */
    public static void playMusic() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audio = AudioSystem.getAudioInputStream(musicAudio);
        musicClip = AudioSystem.getClip();
        musicClip.open(audio);

        //makes sure music is not muted
        if (musicVolume > MUTED && !musicMuted) {
            ((FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(musicVolume);
        } else {
            ((FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(Float.NEGATIVE_INFINITY);
        }

        musicClip.start();
        musicClip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /**
     * Plays the passed effect (.wav file).
     *
     * @param file the file to be played.
     * @throws UnsupportedAudioFileException the unsupported audio file exception
     * @throws IOException                   the io exception
     * @throws LineUnavailableException      the line unavailable exception
     */
    public static void playGameEffect(final File file)
            throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        if (effectsVolume > MUTED && !effectsMuted) {
            AudioInputStream audio = AudioSystem.getAudioInputStream(file);
            Clip click = AudioSystem.getClip();
            click.open(audio);
            ((FloatControl) click.getControl(FloatControl.Type.MASTER_GAIN)).setValue(effectsVolume);
            click.start();
        }
    }

    /**
     * Plays the click effect - used in menu and in-game,
     * represents user click or dragging something.
     *
     * @throws LineUnavailableException      the line unavailable exception
     * @throws IOException                   the io exception
     * @throws UnsupportedAudioFileException the unsupported audio file exception
     */
    public static void clickEffect() throws LineUnavailableException, IOException, UnsupportedAudioFileException {
        //makes sure effects isn't muted
        if (effectsVolume > MUTED && !effectsMuted) {
            AudioInputStream audio = AudioSystem.getAudioInputStream(click);
            Clip clickEffect = AudioSystem.getClip();
            clickEffect.open(audio);
            ((FloatControl) clickEffect.getControl(FloatControl.Type.MASTER_GAIN)).setValue(effectsVolume);
            clickEffect.start();
        }
    }

    /**
     * Updates the current music volume.
     *
     * @param volume the volume
     * @throws IOException error writing updated values
     */
    public static void setMusic(final float volume) throws IOException {
        musicMuted = volume == 0;

        Settings.music = volume;
        musicVolume = MUTED + (music * POINT_OF_VOLUME);

        if (!musicMuted) {
            ((FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(musicVolume);
        } else {
            ((FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN)).setValue(Float.NEGATIVE_INFINITY);
        }

        //writes new values to .txt
        writeValues();
    }

    /**
     * Gets current music volume.
     *
     * @return the music
     */
    public static float getMusic() {
        return music;
    }

    /**
     * Sets effects volume.
     *
     * @param volume wanted volume
     * @throws IOException error writing updated values
     */
    public static void setEffects(final float volume) throws IOException {
        Settings.effects = volume;
        effectsVolume = MUTED + (effects * POINT_OF_VOLUME);
        writeValues();
    }

    /**
     * Gets effects volume.
     *
     * @return the effects volume
     */
    public static float getEffects() {
        return effects;
    }

    /**
     * Sets the preferred language.
     *
     * @param language preferred language
     */
    public static void setLanguage(String language) {
        Settings.language = language;
    }

    /**
     * Sets fullscreen to window.
     *
     * @param fullscreen the fullscreen boolean
     */
    public static void setFullscreen(boolean fullscreen) {
        Settings.fullscreen = fullscreen;
    }

    /**
     * Gets current language of program.
     *
     * @return the language
     */
    public static String getLanguage() {
        return language;
    }

    /**
     * Whether window is in fullscreen.
     *
     * @return fullscreen boolean
     */
    public static boolean isFullscreen() {
        return fullscreen;
    }

    /**
     * Writes updated values to config file.
     *
     * @throws IOException caused by file not existing
     */
    private static void writeValues() throws IOException {
        PrintWriter writer = new PrintWriter(settings, StandardCharsets.UTF_8);

        writer.println("language:" + language);
        writer.println("fullscreen:" + fullscreen);
        writer.println("themedFX:" + themedFX);
        writer.println("musicVolume:" + music);
        writer.println("effectsVolume:" + effects);
        writer.close();
    }

    /**
     * Sets whether the user wants themes FX or not
     *
     * @param themedFX users decision
     */
    public static void setThemedFX(boolean themedFX) {
        Settings.themedFX = themedFX;
    }

    /**
     * Returns whether themeFX are on.
     *
     * @return themeFX boolean
     */
    public static boolean isFXEnabled() {
        return themedFX;
    }

}
