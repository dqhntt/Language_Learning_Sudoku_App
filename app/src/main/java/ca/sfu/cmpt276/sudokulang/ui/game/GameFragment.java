package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ca.sfu.cmpt276.sudokulang.databinding.FragmentGameBinding;
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

        binding.gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (SudokuCell) view;
            binding.gameBoard.setSelectedCell(cell.getRowIndex(), cell.getColIndex(), true);
            binding.gameQuickCellView.setText(cell.getText());
        });

        // TODO: Implement error checking.
        final var wordButtonOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final var button = (Button) v;
                binding.gameQuickCellView.setText(button.getText());
                final var selectedCell = binding.gameBoard.getSelectedCell();
                if (selectedCell != null) {
                    selectedCell.setText(button.getText());
                }
            }
        };
        binding.wordButton1.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton2.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton3.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton4.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton5.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton6.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton7.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton8.setOnClickListener(wordButtonOnClickListener);
        binding.wordButton9.setOnClickListener(wordButtonOnClickListener);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}