# Sudoku Vocabulary Practice App

## Basic Concept

This is an educational game app for users who are learning a new language and enjoy Sudoku puzzles.

A standard Sudoku puzzle is a 9x9 grid of cells organized into a 3x3 arrangement of 3x3 subgrids. Some of the cells initially contain digits 1 through 9. A Sudoku puzzle is solved by filling in the entire grid of cells such that every row and every column and every subgrid contains one each of the digits 1 through 9.

The idea of the Sudoku Vocabulary Practice App is to replace the digits 1 through 9 with nine pairs of words. Each pair of words consists of one word in the user's native language (say English), and one word in the language being studied.

Rather than an initial layout with some of the digits 1 through 9 in some cells, the cells contain words from the word pairs. In one mode, the initial layout may use words in the user's native language and solving the puzzle is to fill in the grid cells using words from the language being studied, such that each row, column and 3x3 subgrid contains words from nine distinct word pairs. In this mode, the words in the grid are in the user's native language and the buttons for entering data into the cells show the words in the language being studied.

In another mode, the initial layout may use the words from the language being studied and the buttons are labelled with the words in the user's native language.

## Project Development

### Requirements Elicitation

[Requirements Document](./docs/requirements_doc.md)

[Implemented User Stories](./docs/requirements_doc_final.md)

### Design

[Messy sketches 😬](./docs/pdf/Messy_Sketches.pdf)

[Sketches](./docs/pdf/Sketches.pdf)

[Mockups](./docs/pdf/Prototype.pdf) ([close-up 🔍](./docs/pdf/Prototype_Closeup.pdf))

### Implementation

[Screenshots](./docs/requirements_doc_final.md#visual-states-of-the-gameapplication)

Tools used:
- Design: [Material Design 3](https://material.io/)
- Database: [Room](https://developer.android.com/topic/libraries/architecture/room)
- Lifecycle: [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel), [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- Navigation: [SafeArgs](https://developer.android.com/guide/navigation/use-graph/pass-data), [Fragment](https://developer.android.com/guide/fragments)

### Testing

Tools used:
- [JUnit 5](https://junit.org/junit5/docs/current/user-guide/)
- [AndroidX Test Junit](https://developer.android.com/training/testing/instrumented-tests)
- [UiAutomator](https://developer.android.com/training/testing/other-components/ui-automator)

### Reflection

[Project Reflection](./docs/pdf/Final_Report.pdf)
