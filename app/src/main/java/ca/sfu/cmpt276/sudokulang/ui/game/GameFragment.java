package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.board.SudokuCell;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {
    private FragmentGameBinding mBinding;
    private GameViewModel mGameViewModel;
    private boolean mIsCompletedGame = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        mGameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        mGameViewModel.generateNewBoard(9, 3, 3);

        // Set OnClickListener for parent view of game board.
        ((View) mBinding.gameBoard.getParent()).setOnClickListener(v -> mGameViewModel.setNoSelectedCell());
        setupBoard();
        setupWordButtons();
        mBinding.eraseButton.setOnClickListener(v -> mGameViewModel.clearSelectedCell());

        return mBinding.getRoot();
    }

    private void endGame() {
        mIsCompletedGame = true;
        mBinding.wordButtonKeypad.setEnabled(false);
        mBinding.eraseButton.setEnabled(false);
        mBinding.notesButton.setEnabled(false);
        Snackbar.make(mBinding.getRoot(), "Game completed. Well done!", Snackbar.LENGTH_INDEFINITE).show();
    }

    private void setupBoard() {
        mBinding.gameBoard.createBoard(mGameViewModel.getBoardUiState().getValue());
        mGameViewModel.getBoardUiState().observe(getViewLifecycleOwner(), boardUiState -> {
            mBinding.gameBoard.updateState(boardUiState);
            final var selectedCell = boardUiState.getSelectedCell();
            mBinding.quickCellView.setText(selectedCell == null ? "" : selectedCell.getText());
            if (boardUiState.isSolvedBoard() && !mIsCompletedGame) {
                endGame();
            }
        });
        mBinding.gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (SudokuCell) view;
            mGameViewModel.setSelectedCell(cell.getRowIndex(), cell.getColIndex());
        });
    }

    private void setupWordButtons() {
        mBinding.wordButtonKeypad.setValues(
                Arrays.stream(mGameViewModel.getDataValuePairs())
                        .map(pair -> pair.first)
                        .toArray(String[]::new)
        );
        mBinding.wordButtonKeypad.setOnclickListenersForAllButtons(view -> {
            final var button = (MaterialButton) view;
            final String choice = (String) button.getText();
            mBinding.quickCellView.setText(choice);
            mGameViewModel.setSelectedCellText(choice);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
