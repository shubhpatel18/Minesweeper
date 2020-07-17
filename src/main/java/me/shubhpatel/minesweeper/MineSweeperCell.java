package me.shubhpatel.minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MineSweeperCell extends Button {

    private final Logger log = LogManager.getLogger();

    private static final int PIXEL_HEIGHT = 30;
    private static final int PIXEL_WIDTH = 30;

    private final Controller controller;

    private final int row;
    private final int col;

    private boolean mine;

    private boolean revealed;
    private int nearbyMineCount;

    /**
     * Creates a MineSweeperCell with a row, column, and reference to its controller.
     * @param row Target row.
     * @param col Target column.
     * @param controller JavaFX application controller.
     */
    public MineSweeperCell(int row, int col, Controller controller) {
        super();
        this.controller = controller;
        this.row = row;
        this.col = col;
        this.mine = false;

        this.setMinWidth(PIXEL_WIDTH);
        this.setMinHeight(PIXEL_HEIGHT);
        this.setStyle("-fx-border-color: #444444"); // TODO move style to CSS stylessheet

        this.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                controller.revealCell(row, col);
            }
        });
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    /**
     * Reveals the cell.
     */
    public void reveal() {
        this.setText(String.valueOf(this.getNearbyMineCount())); // TODO move style to CSS stylessheet
        this.setStyle("-fx-background-color: #aaaaaa; -fx-text-inner-color: #4444ff; -fx-border-color: #444444");
        revealed = true;
    }

    /**
     * Determines whether the cell has zero nearby mines.
     * @return True if zero nearby mines, false if not.
     */
    public boolean hasZeroNearbyMines() {
        return this.getNearbyMineCount() == 0;
    }

    /**
     * Determines if the cell has been revealed yet.
     * @return True if the cell has been revealed, false if not.
     */
    public boolean isRevealed() {
        return revealed;
    }

    /**
     * Sets the cell as having a mine.
     */
    public void setMine() {
        mine = true;
        log.trace("A mine has been set at {}, {}", getRow(), getCol());
    }

    /**
     * Determines if the cell has a mine.
     * @return True if the cell has a mine, false if not.
     */
    public boolean isMine() {
        return mine;
    }

    /**
     * Returns the count of how many nearby
     * @return integer count of nearby mines, 0-8.
     */
    public int getNearbyMineCount() {
        return nearbyMineCount;
    }

    /**
     * Calculates how many nearby mines the cell has.
     */
    public void countNearbyMines() {
        nearbyMineCount = 0;
        for (int i = getRow() - 1; i <= getRow() + 1; i++) {
            for (int j = getCol() - 1; j <= getCol() + 1; j++) {

                boolean isValidRow = i >= 0 && i < controller.getRows();
                boolean isValidColumn = j >= 0 && j < controller.getCols();
                boolean isNotSelf = i != getRow() || j != getCol();

                if (isValidRow && isValidColumn && isNotSelf &&
                        controller.getCells()[i][j].isMine())
                    nearbyMineCount++;

            }
        }

    }
}
