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

import ca.sfu.cmpt276.sudokulang.GameActivity;
import ca.sfu.cmpt276.sudokulang.GameViewModel;
import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.Util;
import ca.sfu.cmpt276.sudokulang.data.WordPair;
import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.UiUtil;
import ca.sfu.cmpt276.sudokulang.ui.game.board.CellUi;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {
    private FragmentGameBinding mBinding;
    private GameViewModel mGameViewModel;
    private Snackbar mGameEndsSnackbar;
    private TextToSpeech mTtsLearningLang, mTtsNativeLang;

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

        // Set up text to speech instances.
        mTtsLearningLang = UiUtil.makeTts(getContext(), mGameViewModel.getLearningLang().getCode());
        mTtsNativeLang = UiUtil.makeTts(getContext(), mGameViewModel.getNativeLang().getCode());
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
        final var valueWordPairMap = mGameViewModel.getValueWordPairMap();
        mGameViewModel.getBoardUiState().observe(getViewLifecycleOwner(), boardUiState -> {
            mBinding.gameBoard.updateState(boardUiState);
            final var selectedCell = boardUiState.getSelectedCell();
            final var selectedCellText = selectedCell == null ? "" : selectedCell.getText();
            mBinding.quickCellView.setText(selectedCellText);
            if (selectedCell != null) {
                if (selectedCell.isPrefilled()) {
                    mTtsLearningLang.speak(mGameViewModel.isComprehensionMode()
                                    ? valueWordPairMap.get(selectedCell.getValue()).getTranslatedWord()
                                    : selectedCellText
                            , TextToSpeech.QUEUE_FLUSH, null, "");
                } else if (!mTtsNativeLang.isSpeaking()) {
                    mTtsNativeLang.speak(selectedCellText, TextToSpeech.QUEUE_FLUSH, null, "");
                }
            }
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
                        mTtsNativeLang.speak(choice, TextToSpeech.QUEUE_FLUSH, null, "");
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

    @Override
    public void onDestroy() {
        if (mTtsLearningLang != null) {
            mTtsLearningLang.stop();
            mTtsLearningLang.shutdown();
        }
        if (mTtsNativeLang != null) {
            mTtsNativeLang.stop();
            mTtsNativeLang.shutdown();
        }
        super.onDestroy();
    }
}