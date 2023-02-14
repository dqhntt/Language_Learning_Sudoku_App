package ca.sfu.cmpt276.sudokulang.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentGameBinding.inflate(inflater, container, false);

        final var gameBoard = binding.gameBoard;
        final var quickCellView = binding.gameQuickCellView;
        gameBoard.setOnclickListenersForAllCells(view -> {
            final var cell = (SudokuCell) view;
            gameBoard.setSelectedCell(cell.getRowIndex(), cell.getColIndex(), true);
            quickCellView.setText(cell.getText());
        });

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}