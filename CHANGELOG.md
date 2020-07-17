# Changelog
All notable changes to this project will be document in this file.

This project adheres to semantic versioning.
Dates are given as YYYY-MM-DD.

## [0.1.0] - 2020-07-16
### Added
- Grid of cells
- Random cells contain mines
- The 'Show Mines' button allows the user to see where the mines are
    - primarily intended for testing
- Clicked cells reveal the count of mines surrounding that cell
    - Cells with no nearby mines will reveal the mine count of their neighbors
    - Neighboring cells with no nearby mines will repeat the reveal process
- Mines are revealed when the game ends
    - The game ends on a loss if a mine is revealed
    - The game ends on a victory if all non-mine cells are revealed