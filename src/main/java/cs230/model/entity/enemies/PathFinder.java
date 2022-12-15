package cs230.model.entity.enemies;

import cs230.model.Board;
import cs230.model.Tile;
import cs230.model.entity.items.Bomb;
import cs230.model.entity.items.Gate;
import java.util.ArrayList;

/**
 * Used to find the shortest path to a reachable entity on the board
 */
public class PathFinder {

    /**
     * current tile being stood on
     */
    private final Tile startingTile;
    /**
     * the wanted tile
     */
    private Tile goalTile;
    /**
     * Whether the goal tile has been reached
     */
    private boolean goalReached = false;
    /**
     * List used to identify tiles that have been opened
     */
    ArrayList<Tile> openList = new ArrayList<>();
    /**
     * List used to identify discovered tiles
     */
    ArrayList<Tile> checkedList = new ArrayList<>();
    /**
     * Moves required to reach goal tile
     */
    ArrayList<Tile> moves = new ArrayList<>();

    /**
     * Class initiation, will check if goal tile is reachable from starting tile in the current state of board
     *
     * @param board board being played on
     * @param starting starting tile
     * @param goalTile goal tile
     */
    public PathFinder(Board board, Tile starting, Tile goalTile) {
        this.startingTile = starting;
        this.goalTile = goalTile;

        setCostOnTile(board);
        search(board);
    }

    /**
     * number of moves from starting tile to goal tile
     *
     * @return returns distance
     */
    public Integer getDistance() {
        return moves.size();
    }

    /**
     * searches the board for the goal tile, for each search identifies tile that that
     * can and can't be moved to, adhering to game rules.
     *
     * @param board board being played on
     */
    private void search(Board board) {
        int step = 0;

        Tile tile = board.getTile(startingTile.getX(), startingTile.getY());

        boolean empty = false;
        while (!goalReached && step < 300 && !empty) {
            int x = tile.getX();
            int y = tile.getY();
            tile.setChecked(true);

            checkedList.add(tile);
            openList.remove(tile);

            int tempX = x - 1;
            while (tempX >= 0) {
                Tile tempTile = board.getTile(tempX, y);
                if (tempTile.getItem() instanceof Bomb) {
                    break;
                }

                if (!(tempTile.getItem() instanceof Gate)) {
                    if (tile.shareColor(tempTile)) {
                        openTile(board.getTile(tempX, y), tile);
                        break;
                    } else {
                        tempX--;
                    }
                }else {
                    tempX--;
                }
            }

            tempX = x + 1;
            while (tempX < board.getWidth()) {
                Tile tempTile = board.getTile(tempX, y);
                if (tempTile.getItem() instanceof Bomb) {
                    break;
                }

                if (!(tempTile.getItem() instanceof Gate)) {
                    if (tile.shareColor(tempTile)) {
                        openTile(board.getTile(tempX, y), tile);
                        break;
                    } else {
                        tempX++;
                    }
                } else {
                    tempX++;
                }
            }

            int tempY = y - 1;
            while (tempY >= 0) {
                Tile tempTile = board.getTile(x, tempY);
                if (tempTile.getItem() instanceof Bomb) {
                    break;
                }

                if (!(tempTile.getItem() instanceof Gate)) {
                    if (tile.shareColor(tempTile)) {
                        openTile(board.getTile(x, tempY), tile);
                        break;
                    } else {
                        tempY--;
                    }
                } else {
                    tempY--;
                }
            }

            tempY = y + 1;
            while (tempY < board.getHeight()) {
                Tile tempTile = board.getTile(x, tempY);
                if (tempTile.getItem() instanceof Bomb) {
                    break;
                }

                if (!(tempTile.getItem() instanceof Gate)) {
                    if (tile.shareColor(tempTile)) {
                        openTile(tempTile, tile);
                        break;
                    } else {
                        tempY++;
                    }
                } else {
                    tempY++;
                }
            }

            int bestIndex = 0;
            int bestFCost = 999;
            for (int i = 0; i < openList.size(); i++) {
                Tile openTile = openList.get(i);

                if (openTile.getfCost() < bestFCost) {
                    bestIndex = i;
                    bestFCost = openTile.getfCost();
                } else if (openTile.getfCost() == bestFCost) {
                    if (openTile.getgCost() < openList.get(bestIndex).getgCost()) {
                        bestIndex = i;
                    }
                }
            }
            step++;

            if (openList.size() > 0) {
                tile = board.getTile(openList.get(bestIndex).getX(), openList.get(bestIndex).getY());

                if (tile == goalTile) {
                    goalReached = true;

                    Tile current = goalTile;
                    moves.add(current);

                    while (current != startingTile) {
                        current = current.getParent();

                        if (current != startingTile) {
                            moves.add(current);
                        }
                    }
                }
            } else {
                empty = true;
            }
        }
    }

    /**
     * Identifies whether the tile should be investigated further
     *
     * @param tile possible tile
     * @param curr current tile
     */
    private void openTile(Tile tile, Tile curr) {
        if (!tile.isOpen() && !tile.isChecked() && canMove(tile)) {
            tile.setOpen(true);
            tile.setParent(curr);

            openList.add(tile);
        }
    }

    /**
     * Whether the tiles are being occupied by blocking items
     *
     * @param tile tile
     * @return whether tile is blocked
     */
    private boolean canMove(Tile tile) {
        return !(tile.getItem() instanceof Bomb) && !(tile.getItem() instanceof Gate);
    }

    /**
     * Gives a tile a cost, the lower the calculated value, the better the move is
     *
     * @param board game board
     */
    private void setCostOnTile(Board board) {
        int x = 0;
        int y = 0;

        while (x < board.getWidth() && y < board.getHeight()) {
            getCost(board.getTile(x, y));
            y++;

            if (y == board.getHeight()) {
                y = 0;
                x++;
            }
        }
    }

    /**
     * Calculates and returns a cost of moving to the passed tile
     *
     * @param tile tile being investigated
     */
    private void getCost(Tile tile) {
        int xDistance = Math.abs(tile.getX() - startingTile.getX());
        int yDistance = Math.abs(tile.getY() - startingTile.getY());
        tile.setgCost(xDistance + yDistance);

        xDistance = Math.abs(tile.getX() - goalTile.getX());
        yDistance = Math.abs(tile.getY() - goalTile.getY());
        tile.sethCost(xDistance + yDistance);

        tile.setfCost(tile.getgCost() + tile.gethCost());
    }

}
