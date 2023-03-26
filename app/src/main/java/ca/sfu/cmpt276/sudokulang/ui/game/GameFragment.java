package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

import ca.sfu.cmpt276.sudokulang.GameViewModel;
import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.data.WordPair;
import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.board.CellUi;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {
    private FragmentGameBinding mBinding;
    private GameViewModel mGameViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Share ViewModel with parent activity.
        mGameViewModel = new ViewModelProvider(requireActivity()).get(GameViewModel.class);

        // Set OnClickListener for parent view of game board.
        final View.OnClickListener unselectCell = v -> mGameViewModel.setNoSelectedCell();
        ((View) mBinding.gameBoard.getParent()).setOnClickListener(unselectCell);
        mBinding.wordButtonKeypad.setOnClickListener(unselectCell);
        setupBoard();
        setupWordButtons();
        mBinding.eraseButton.setOnClickListener(v -> mGameViewModel.clearSelectedCell());
    }

    private void endGame() {
        mGameViewModel.endGame();
        mBinding.wordButtonKeypad.setEnabled(false);
        mBinding.eraseButton.setEnabled(false);
        mBinding.notesButton.setEnabled(false);
        Snackbar.make(mBinding.getRoot(), R.string.congratulations, Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(requireActivity().findViewById(R.id.bottom_app_bar))
                .show();
    }

    private void setupBoard() {
        mBinding.gameBoard.createBoard(mGameViewModel.getBoardUiState().getValue());
        mGameViewModel.getBoardUiState().observe(getViewLifecycleOwner(), boardUiState -> {
            mBinding.gameBoard.updateState(boardUiState);
            final var selectedCell = boardUiState.getSelectedCell();
            mBinding.quickCellView.setText(selectedCell == null ? "" : selectedCell.getText());
            if (boardUiState.isSolvedBoard() && mGameViewModel.isGameInProgress()) {
                endGame();
            }
        });
        mBinding.gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (CellUi) view;
            mGameViewModel.setSelectedCell(cell.getRowIndex(), cell.getColIndex());
        });
    }

    private void setupWordButtons() {
        mBinding.wordButtonKeypad.setValues(
                Arrays.stream(mGameViewModel.getWordPairs())
                        .map(WordPair::getOriginalWord)
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