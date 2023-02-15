package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.Random;

import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.board.SudokuBoard;
import ca.sfu.cmpt276.sudokulang.ui.game.board.SudokuCell;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class GameFragment extends Fragment {

    private FragmentGameBinding binding;

//    public View onCreateView(@NonNull LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        GameViewModel gameViewModel =
//                new ViewModelProvider(this).get(GameViewModel.class);
//
//        binding = FragmentGameBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();
//
//        final TextView textView = binding.gameQuickCellView;
//        gameViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
//        return root;
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);

        // TODO: Replace this.
        setupTestGame();

        // Set OnClickListener for parent view of game board.
        ((View) binding.gameBoard.getParent()).setOnClickListener(view -> {
            binding.gameBoard.setNoSelectedCell();
            binding.gameQuickCellView.setText("");
        });

        binding.gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (SudokuCell) view;
            binding.gameBoard.setSelectedCell(cell.getRowIndex(), cell.getColIndex());
            binding.gameQuickCellView.setText(cell.getText());
        });

        final var wordButtonOnClickListener = new WordButtonOnClickListener();
        for (var button : getAllWordButtons()) {
            button.setOnClickListener(wordButtonOnClickListener);
        }

        return binding.getRoot();
    }

    private Button[] getAllWordButtons() {
        var buttons = new ArrayList<Button>();
        buttons.add(binding.wordButton1);
        buttons.add(binding.wordButton2);
        buttons.add(binding.wordButton3);
        buttons.add(binding.wordButton4);
        buttons.add(binding.wordButton5);
        buttons.add(binding.wordButton6);
        buttons.add(binding.wordButton7);
        buttons.add(binding.wordButton8);
        buttons.add(binding.wordButton9);
        return buttons.toArray(new Button[0]);
    }

    private boolean validate(SudokuBoard gameBoard) {
        // TODO: Validate entire board.
        return true;
    }

    private boolean validate(String value, SudokuCell cell, @NonNull SudokuBoard gameBoard) {
        assert (binding.gameBoard.existsCell(cell));
        // TODO
        return new Random().nextBoolean();
    }

    // TODO: Implement error checking.
    private class WordButtonOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final var button = (Button) v;
            final String choice = (String) button.getText();
            binding.gameQuickCellView.setText(choice);
            final var selectedCell = binding.gameBoard.getSelectedCell();
            if (selectedCell != null && !selectedCell.isPrefilled()) {
                binding.gameBoard.setValue(selectedCell, choice);
                selectedCell.setAsErrorCell(!validate(choice, selectedCell, binding.gameBoard));
                binding.gameBoard.highlightRelatedCells(selectedCell);
                if (binding.gameBoard.getNumEmptyCells() == 0 && validate(binding.gameBoard)) {
                    // TODO: Disable buttons, end game.
                    for (var b : getAllWordButtons()) {
                        b.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * For TESTING only.
     * <p>
     * TODO: Replace this manual test with a database.
     */
    private void setupTestGame() {
        binding.wordButton1.setText("Ag");
        binding.wordButton2.setText("Br");
        binding.wordButton3.setText("Ca");
        binding.wordButton4.setText("Fe");
        binding.wordButton5.setText("He");
        binding.wordButton6.setText("Kr");
        binding.wordButton7.setText("Li");
        binding.wordButton8.setText("Pb");
        binding.wordButton9.setText("Se");
        binding.gameBoard.setProperties(9, 3, 3);
        binding.gameBoard.setCellProperties(0, 3, "Silver", true, false);
        binding.gameBoard.setCellProperties(2, 6, "Silver", true, false);
        binding.gameBoard.setCellProperties(4, 7, "Silver", true, false);
        binding.gameBoard.setCellProperties(6, 5, "Silver", true, false);
        binding.gameBoard.setCellProperties(7, 1, "Silver", true, false);
        binding.gameBoard.setCellProperties(0, 2, "Bromine", true, false);
        binding.gameBoard.setCellProperties(1, 4, "Bromine", true, false);
        binding.gameBoard.setCellProperties(3, 1, "Bromine", true, false);
        binding.gameBoard.setCellProperties(6, 7, "Bromine", true, false);
        binding.gameBoard.setCellProperties(7, 0, "Bromine", true, false);
        binding.gameBoard.setCellProperties(1, 1, "Calcium", true, false);
        binding.gameBoard.setCellProperties(4, 2, "Calcium", true, false);
        binding.gameBoard.setCellProperties(5, 8, "Calcium", true, false);
        binding.gameBoard.setCellProperties(7, 3, "Calcium", true, false);
        binding.gameBoard.setCellProperties(8, 6, "Calcium", true, false);
        binding.gameBoard.setCellProperties(1, 6, "Iron", true, false);
        binding.gameBoard.setCellProperties(2, 4, "Iron", true, false);
        binding.gameBoard.setCellProperties(4, 1, "Iron", true, false);
        binding.gameBoard.setCellProperties(5, 7, "Iron", true, false);
        binding.gameBoard.setCellProperties(6, 2, "Iron", true, false);
        binding.gameBoard.setCellProperties(0, 6, "Helium", true, false);
        binding.gameBoard.setCellProperties(2, 0, "Helium", true, false);
        binding.gameBoard.setCellProperties(3, 7, "Helium", true, false);
        binding.gameBoard.setCellProperties(4, 5, "Helium", true, false);
        binding.gameBoard.setCellProperties(5, 1, "Helium", true, false);
        binding.gameBoard.setCellProperties(3, 2, "Krypton", true, false);
        binding.gameBoard.setCellProperties(4, 4, "Krypton", true, false);
        binding.gameBoard.setCellProperties(5, 6, "Krypton", true, false);
        binding.gameBoard.setCellProperties(7, 5, "Krypton", true, false);
        binding.gameBoard.setCellProperties(8, 7, "Krypton", true, false);
        binding.gameBoard.setCellProperties(0, 1, "Lithium", true, false);
        binding.gameBoard.setCellProperties(2, 5, "Lithium", true, false);
        binding.gameBoard.setCellProperties(3, 3, "Lithium", true, false);
        binding.gameBoard.setCellProperties(5, 2, "Lithium", true, false);
        binding.gameBoard.setCellProperties(6, 4, "Lithium", true, false);
        binding.gameBoard.setCellProperties(2, 1, "Lead", true, false);
        binding.gameBoard.setCellProperties(7, 4, "Lead", true, false);
        binding.gameBoard.setCellProperties(1, 8, "Selenium", true, false);
        binding.gameBoard.setCellProperties(3, 6, "Selenium", true, false);
        binding.gameBoard.setCellProperties(6, 3, "Selenium", true, false);
        binding.gameBoard.setCellProperties(7, 7, "Selenium", true, false);
    }
}