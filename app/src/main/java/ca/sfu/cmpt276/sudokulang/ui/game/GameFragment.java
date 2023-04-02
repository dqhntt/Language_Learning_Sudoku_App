package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

import ca.sfu.cmpt276.sudokulang.GameActivity;
import ca.sfu.cmpt276.sudokulang.GameViewModel;
import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.Util;
import ca.sfu.cmpt276.sudokulang.data.WordPair;
import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.board.CellUi;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {
    private FragmentGameBinding mBinding;
    private GameViewModel mGameViewModel;
    private Snackbar mGameEndsSnackbar;
    private TextToSpeech mTts;

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

        // Prepare snackbar but doesn't show it yet.
        mGameEndsSnackbar = Snackbar
                .make(mBinding.getRoot(), "", Snackbar.LENGTH_INDEFINITE)
                .setAnchorView(requireActivity().findViewById(R.id.bottom_app_bar))
                .setAction(R.string.new_game, v -> {
                    ((GameActivity) requireActivity()).startNewGame();
                    mGameEndsSnackbar.dismiss();
                });

        // Share text to speech instance with parent activity.
        mTts = ((GameActivity) requireActivity()).getTts(Locale.US); // TODO: Get locale from VM.
    }

    private void endGame() {
        mGameViewModel.endGame();
        mGameEndsSnackbar
                .setText(Util.formatWithTime(getString(R.string.congratulations),
                        mGameViewModel.getElapsedTime()))
                .show();
    }

    private void setupBoard() {
        mBinding.gameBoard.createBoard(mGameViewModel.getBoardUiState().getValue());
        mGameViewModel.getBoardUiState().observe(getViewLifecycleOwner(), boardUiState -> {
            mBinding.gameBoard.updateState(boardUiState);
            final var selectedCell = boardUiState.getSelectedCell();
            final var selectedCellText = selectedCell == null ? "" : selectedCell.getText();
            mBinding.quickCellView.setText(selectedCellText);

            // TODO
            mTts.speak(selectedCellText, TextToSpeech.QUEUE_FLUSH, null, "");

            if (boardUiState.isSolvedBoard() && mGameViewModel.isGameInProgress().getValue()) {
                endGame();
            }
        });
        mBinding.gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (CellUi) view;
            mGameViewModel.setSelectedCell(cell.getRowIndex(), cell.getColIndex());
        });
    }

    private void setupWordButtons() {
        mGameViewModel.getWordPairs().observe(getViewLifecycleOwner(), wordPairs -> {
                    mBinding.wordButtonKeypad.setValues(Arrays.stream(wordPairs)
                            .map(WordPair::getOriginalWord)
                            .toArray(String[]::new));
                    mBinding.wordButtonKeypad.setOnclickListenersForAllButtons(view -> {
                        final var button = (MaterialButton) view;
                        final String choice = (String) button.getText();
                        mBinding.quickCellView.setText(choice);
                        mGameViewModel.setSelectedCellText(choice);

                        // TODO
                        mTts.speak(choice, TextToSpeech.QUEUE_FLUSH, null, "");
                    });
                }
        );
        mGameViewModel.isGameInProgress().observe(getViewLifecycleOwner(), gameInProgress -> {
            // Disable buttons but doesn't end game.
            mBinding.wordButtonKeypad.setEnabled(gameInProgress);
            mBinding.eraseButton.setEnabled(gameInProgress);
            mBinding.notesButton.setEnabled(gameInProgress);
            if (gameInProgress) {
                mGameEndsSnackbar.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}