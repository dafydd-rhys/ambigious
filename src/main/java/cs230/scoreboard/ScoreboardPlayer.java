package cs230.scoreboard;

/**
 * ScoreboardPlayer - used to store and display player info
 *
 * @author Dafydd -Rhys Maund (2003900)
 */
public class ScoreboardPlayer {

    /**
     * level of the scoreboard
     */
    private String level;
    /**
     * rank of player on scoreboard
     */
    private int rank;
    /**
     * name of player
     */
    private String name;
    /**
     * score of player
     */
    private int score;

    /**
     * Instantiates a new Scoreboard player.
     *
     * @param newLevel the level
     * @param newRank  the rank
     * @param newName  the name
     * @param newScore the score
     */
    public ScoreboardPlayer(final String newLevel, final int newRank, final String newName, final int newScore) {
        setLevel(newLevel);
        setRank(newRank);
        setName(newName);
        setScore(newScore);
    }

    /**
     * Sets level of scoreboard.
     *
     * @param newLevel the level
     */
    public void setLevel(final String newLevel) {
        this.level = newLevel;
    }

    /**
     * Gets level of scoreboard.
     *
     * @return the level
     */
    public String getLevel() {
        return level;
    }

    /**
     * Sets rank of scoreboard.
     *
     * @param newRank the rank
     */
    public void setRank(final int newRank) {
        this.rank = newRank;
    }

    /**
     * Gets rank of scoreboard.
     *
     * @return the rank
     */
    public int getRank() {
        return rank;
    }

    /**
     * Sets name of scoreboard.
     *
     * @param newName the name
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Gets name of scoreboard.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets score of scoreboard.
     *
     * @param newScore the score
     */
    public void setScore(final int newScore) {
        this.score = newScore;
    }

    /**
     * Gets score of scoreboard.
     *
     * @return the score
     */
    public int getScore() {
        return score;
    }

}

