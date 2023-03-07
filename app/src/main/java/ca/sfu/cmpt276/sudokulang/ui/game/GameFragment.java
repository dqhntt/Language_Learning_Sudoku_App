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

import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.board.SudokuCell;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {
    private static final String SHOULD_CREATE_NEW_GAME = "should_create_new_game";
    private FragmentGameBinding mBinding;
    private GameViewModel mGameViewModel;
    private boolean mIsCompletedGame = false;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mBinding = FragmentGameBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mGameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Check if recreating a previously destroyed instance.
        if (shouldCreateNewGame(savedInstanceState)) {
            if (getArguments() == null) {
                mGameViewModel.generateNewBoard(9, 3, 3);
            } else {
                final var args = GameFragmentArgs.fromBundle(requireArguments());
                mGameViewModel.generateNewBoard(
                        args.getBoardSize(),
                        args.getSubgridHeight(),
                        args.getSubgridWidth()
                );
            }
        }

        // Set OnClickListener for parent view of game board.
        final View.OnClickListener unselectCell = v -> mGameViewModel.setNoSelectedCell();
        ((View) mBinding.gameBoard.getParent()).setOnClickListener(unselectCell);
        mBinding.wordButtonKeypad.setOnClickListener(unselectCell);
        setupBoard();
        setupWordButtons();
        mBinding.eraseButton.setOnClickListener(v -> mGameViewModel.clearSelectedCell());
    }

    private boolean shouldCreateNewGame(@Nullable Bundle savedInstanceState) {
        return savedInstanceState == null || savedInstanceState.getBoolean(SHOULD_CREATE_NEW_GAME);
    }

    private void endGame() {
        mIsCompletedGame = true;
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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save game state to the instance state bundle.
        outState.putBoolean(SHOULD_CREATE_NEW_GAME, false);
    }
}
