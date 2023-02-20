package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.board.SudokuBoardViewModel;
import ca.sfu.cmpt276.sudokulang.ui.game.board.SudokuCell;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {
    private FragmentGameBinding mBinding;
    private SudokuBoardViewModel mSudokuBoardVM;
    private LifecycleOwner mLifecycleOwner;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        mLifecycleOwner = getViewLifecycleOwner();
        mSudokuBoardVM = new ViewModelProvider(this).get(SudokuBoardViewModel.class);

        // Set OnClickListener for parent view of game board.
        ((View) mBinding.gameBoard.getParent()).setOnClickListener(view -> mSudokuBoardVM.setNoSelectedCell());

        setupBoard();
        setupCells();
        setupWordButtons();
        setupEraseButton();

        return mBinding.getRoot();
    }

    private void endGame() {
        for (var button : getAllWordButtons()) {
            button.setEnabled(false);
        }
        mBinding.eraseButton.setEnabled(false);
        mBinding.notesButton.setEnabled(false);
        Snackbar.make(mBinding.getRoot(), "Game completed. Well done!", Snackbar.LENGTH_INDEFINITE).show();
    }

    private void setupBoard() {
        mBinding.gameBoard.createEmptyBoard(
                mSudokuBoardVM.getBoardSize(),
                mSudokuBoardVM.getSubgridHeight(),
                mSudokuBoardVM.getSubgridWidth());
        // Set up observers.
        mSudokuBoardVM.getBoardDimension().observe(mLifecycleOwner, boardDimension -> {
            mBinding.gameBoard.createEmptyBoard(
                    boardDimension.boardSize,
                    boardDimension.subgridHeight,
                    boardDimension.subgridWidth);
            setupCells();
        });
        mSudokuBoardVM.getSelectedCell().observe(mLifecycleOwner, selectedCellVM -> {
            if (selectedCellVM == null) {
                mBinding.quickCellView.setText("");
            } else {
                mBinding.quickCellView.setText(selectedCellVM.getText().getValue());
            }
            mBinding.gameBoard.highlightRelatedCells(selectedCellVM);
        });
        mSudokuBoardVM.getNumEmptyCells().observe(mLifecycleOwner, numEmptyCells -> {
            if (numEmptyCells == 0 && mSudokuBoardVM.isValidBoard()) {
                endGame();
            }
        });
    }

    private void setupCells() {
        mBinding.gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (SudokuCell) view;
            mSudokuBoardVM.setSelectedCell(cell.getRowIndex(), cell.getColIndex());
        });
        clearSudokuCellViewModelsObservers();
        // Set SudokuCell to automatically observe SudokuCellViewModel.
        for (var row : mSudokuBoardVM.getCells()) {
            for (var cellVM : row) {
                final int rowIndex = cellVM.getRowIndex().getValue();
                final int colIndex = cellVM.getColIndex().getValue();
                final var cellsUIs = mBinding.gameBoard.getCells();
                cellVM.isPrefilled().observe(mLifecycleOwner, cellsUIs[rowIndex][colIndex]::setPrefilled);
                cellVM.getText().observe(mLifecycleOwner, text -> {
                    cellsUIs[rowIndex][colIndex].setText(text);
                    if (cellVM == mSudokuBoardVM.getSelectedCell().getValue()) {
                        mBinding.quickCellView.setText(text);
                    }
                });
                cellVM.isErrorCell().observe(mLifecycleOwner, isError -> {
                    cellsUIs[rowIndex][colIndex].setAsErrorCell(isError);
                    if (cellVM == mSudokuBoardVM.getSelectedCell().getValue()) {
                        mBinding.gameBoard.highlightRelatedCells(cellVM);
                    }
                });
            }
        }
    }

    private void clearSudokuCellViewModelsObservers() {
        for (var row : mSudokuBoardVM.getCells()) {
            for (var cellVM : row) {
                cellVM.isPrefilled().removeObservers(mLifecycleOwner);
                cellVM.getText().removeObservers(mLifecycleOwner);
                cellVM.isErrorCell().removeObservers(mLifecycleOwner);
            }
        }
    }

    private void setupEraseButton() {
        mBinding.eraseButton.setOnClickListener(v -> {
            var selectedCellVM = mSudokuBoardVM.getSelectedCell().getValue();
            if (selectedCellVM != null && !selectedCellVM.isPrefilled().getValue()) {
                selectedCellVM.setText("");
                selectedCellVM.setAsErrorCell(false);
            }
        });
    }

    private void setupWordButtons() {
        final var wordButtonOnClickListener = new WordButtonOnClickListener();
        final var wordButtons = getAllWordButtons();
        final var dataValuePairs = mSudokuBoardVM.getDataValuePairs();
        assert (wordButtons.length == dataValuePairs.length);
        for (int i = 0; i < wordButtons.length; i++) {
            wordButtons[i].setOnClickListener(wordButtonOnClickListener);
            wordButtons[i].setText(dataValuePairs[i].first);
        }
    }

    private Button[] getAllWordButtons() {
        var buttons = new ArrayList<Button>();
        buttons.add(mBinding.wordButton1);
        buttons.add(mBinding.wordButton2);
        buttons.add(mBinding.wordButton3);
        buttons.add(mBinding.wordButton4);
        buttons.add(mBinding.wordButton5);
        buttons.add(mBinding.wordButton6);
        buttons.add(mBinding.wordButton7);
        buttons.add(mBinding.wordButton8);
        buttons.add(mBinding.wordButton9);
        return buttons.toArray(new Button[0]);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private class WordButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final var button = (Button) v;
            final String choice = (String) button.getText();
            mBinding.quickCellView.setText(choice);
            final var selectedCell = mSudokuBoardVM.getSelectedCell().getValue();
            if (selectedCell != null && !selectedCell.isPrefilled().getValue()) {
                selectedCell.setText(choice);
                selectedCell.setAsErrorCell(!mSudokuBoardVM.isValidValueForCell(choice, selectedCell));
            }
            mSudokuBoardVM.updateNumEmptyCells();
        }
    }
}