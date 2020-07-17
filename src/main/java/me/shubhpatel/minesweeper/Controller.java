package me.shubhpatel.minesweeper;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class Controller {

    @FXML
    private GridPane grid;
    @FXML
    private Button minesVisibilityToggle;

    private int rows = 12;
    private int cols = 12;
    private double mineRatio = .2;

    private final Random rand = new Random();
    private final Logger log = LogManager.getLogger();

    private final MineSweeperCell[][] cells = new MineSweeperCell[rows][cols];
    private boolean minesAreVisible = false;

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public MineSweeperCell[][] getCells() {
        return cells;
    }

    @FXML
    private void initialize() {
        log.info("Minesweeper is starting...");
        minesVisibilityToggle.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                toggleMinesVisibility();
            }
        });

        createMineSweeperGrid(getRows(), getCols());
        addMines(mineRatio);
        updateAllMineCounts();
        log.info("Minesweeper has started.");
    }

    /**
     * Creates the Grid of cells that follow the minesweeper behaviors.
     * @param rows Rows in the cells grid.
     * @param cols Columns in the cells grid.
     */
    private void createMineSweeperGrid(int rows, int cols) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                cells[r][c] = new MineSweeperCell(r, c, this);
                grid.add(cells[r][c], c, 4 + r, 1, 1);
            }
        }
        log.debug("The Minesweeper Grid was created.");
    }

    /**
     * Adds mines randomly to the Grid.
     * @param mineRatio The changes of creating a mine at any location.
     */
    private void addMines(double mineRatio) {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                if (rand.nextDouble() < mineRatio)
                    cells[r][c].setMine();
            }
        }
        log.debug("Mines were added the Grid randomly.");
    }

    /**
     * Calculates how many mines surround each cell.
     */
    private void updateAllMineCounts() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {

                cells[r][c].countNearbyMines();
            }
        }
        log.debug("All cells mine counts have been updated.");
    }

    /**
     * Reveals how many mines surround the target cell.
     * If the target cell has zero nearby mines, the nearby mines of its neighbors are also revealed.
     * Then, if a nearby cell has zero nearby mines, the method is called again on that cell.
     *
     * @param row The row of the target cell.
     * @param col The column of the target cell.
     */
    protected void revealCell(int row, int col) {
        checkLoss(row, col);

        if (!cells[row][col].isRevealed()) {

            cells[row][col].reveal();
            log.trace("The cell at {}, {} was revealed", row, col);

            if (cells[row][col].hasZeroNearbyMines()) {
                log.trace("The cell at {}, {} has no neighboring mines", row, col);

                if (isValidCoordinates(row - 1, col)) revealCell(row - 1, col);
                if (isValidCoordinates(row + 1, col)) revealCell(row + 1, col);
                if (isValidCoordinates(row, col - 1)) revealCell(row, col - 1);
                if (isValidCoordinates(row, col + 1)) revealCell(row, col + 1);
            }
        }

        checkWin();
    }

    /**
     * Determines if the target coordinates are a valid location in the cells array.
     *
     * @param row Target row.
     * @param col Target cell.
     * @return True if the coordinates are in the cells array, false otherwise.
     */
    public boolean isValidCoordinates(int row, int col) {
        boolean isValidRow = row >= 0 && row < getRows();
        boolean isValidColumn = col >= 0 && col < getCols();
        return isValidRow && isValidColumn;
    }

    /**
     * Checks if clicking a certain cell results in a game-ending loss.
     *
     * @param row Row of the target cell.
     * @param col Column of the target cell.
     */
    private void checkLoss(int row, int col) {
        if (cells[row][col].isMine())
            endGame(false);
    }

    /**
     * Checks if the game has been won.
     */
    private void checkWin() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (!cells[r][c].isRevealed() && !cells[r][c].isMine())
                    return;
            }
        }
        endGame(true);
    }

    /**
     * Displays game end mechanics
     *
     * @param victory True means victory, false means loss.
     */
    private void endGame(boolean victory) {
        if (victory) {
            log.info("You won!");
        } else {
            log.info("You lost!");
        }

        enableMineVisibility();
    }

    /**
     * Toggles mine visibility.
     */
    private void toggleMinesVisibility() {
        if (minesAreVisible) disableMineVisibility();
        else enableMineVisibility();
        minesAreVisible = !minesAreVisible;
    }

    /**
     * Enables mine visibility.
     */
    private void enableMineVisibility() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].isMine())
                    cells[r][c].setStyle("-fx-background-color: #eeaaaa; -fx-border-color: #444444");
            }
        }
        log.info("Mine visibility was enabled.");
    }

    /**
     * Disables mine visibility.
     */
    private void disableMineVisibility() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c].isMine()) {
                    if (!cells[r][c].isRevealed())
                        cells[r][c].setStyle("-fx-border-color: #444444");
                    else
                        cells[r][c].setStyle("-fx-background-color: #aaaaaa; -fx-border-color: #444444");
                }
            }
        }
        log.info("Mine visibility was disabled.");
    }

}
