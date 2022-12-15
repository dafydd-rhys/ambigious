package cs230.model.entity.enums;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Represents a direction that a moving entity can be facing.
 *
 * @author Dafydd-Rhys Maund
 * @author Oliver
 */
public enum Direction {
    /**
     * Up direction.
     */
    UP(),
    /**
     * Down direction.
     */
    DOWN(),
    /**
     * Left direction.
     */
    LEFT(),
    /**
     * Right direction.
     */
    RIGHT();

    /**
     * Gets the opposite direction of passed direction.
     *
     * @param dir the direction
     * @return the opposite direction
     */
    public static Direction oppositeDirection(Direction dir) {
        Direction direction;
        switch (dir) {
            case UP -> direction = DOWN;
            case RIGHT -> direction = LEFT;
            case DOWN -> direction = UP;
            default -> direction = RIGHT;
        }

        return direction;
    }

    /**
     * Gets hugging direction to the left of passed direction.
     *
     * @param dir the direction
     * @return the left-hugging direction
     */
    public static Direction getHugging(Direction dir) {
        Direction direction;
        switch (dir) {
            case UP -> direction = LEFT;
            case RIGHT -> direction = UP;
            case DOWN -> direction = RIGHT;
            default -> direction = DOWN;
        }

        return direction;
    }

    /**
     * Gets random direction.
     *
     * @return random direction
     */
    public static Direction getRandomDirection() {
        return List.of(Direction.values()).get(new Random().nextInt(List.of(Direction.values()).size()));
    }

    /**
     * Gets direction based on passed regex.
     *
     * @param s the regex
     * @return the direction
     */
    public static Direction getDirectionRegex(String s) {
        Direction direction;
        switch (s) {
            case "UP" -> direction = UP;
            case "RIGHT" -> direction = RIGHT;
            case "DOWN" -> direction = DOWN;
            default -> direction = LEFT;
        }

        return direction;
    }

    /**
     * Get directions in a random order
     *
     * @return shuffled direction list
     */
    public static List<Direction> scrambledDirectionList() {
        List<Direction> directions = Arrays.asList(Direction.values());
        Collections.shuffle(directions);

        return directions;
    }

}
