package ca.sfu.cmpt276.sudokulang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import ca.sfu.cmpt276.sudokulang.data.BoardDimension;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepository;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepository;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final Map<String, BoardDimension> mStringToGridSizeMap = new HashMap<>();
    private TranslationRepository mTranslationRepository;
    private BoardRepository mBoardRepository;
    private Spinner mLearningLangSpinner, mNativeLangSpinner, mGridSizeSpinner;
    private ArrayAdapter<CharSequence> mLearningLangAdapter, mNativeLangAdapter, mGridSizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final var binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Find the spinners using view binding
        mLearningLangSpinner = binding.spinnerLearningLang;
        mNativeLangSpinner = binding.spinnerNativeLang;
        mGridSizeSpinner = binding.spinnerGridSize;

        // Initialize the repositories
        mTranslationRepository = TranslationRepositoryImpl.getInstance(this);
        mBoardRepository = BoardRepositoryImpl.getInstance(this);

        // Initialize the adapter for the spinner
        mLearningLangAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout);
        mLearningLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLearningLangSpinner.setAdapter(mLearningLangAdapter);

        mNativeLangAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout);
        mNativeLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNativeLangSpinner.setAdapter(mNativeLangAdapter);

        mGridSizeAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout);
        mGridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mGridSizeSpinner.setAdapter(mGridSizeAdapter);

        // Observe the LiveData returned by the repository
        mTranslationRepository.getAvailableLearningLanguages().observe(this, new Observer<>() {
            @Override
            public void onChanged(List<String> learningLanguages) {
                mLearningLangAdapter.clear();
                if (learningLanguages.size() > 1) {
                    mLearningLangAdapter.add(getString(R.string.select_learning_lang));
                }
                mLearningLangAdapter.addAll(learningLanguages);
                mLearningLangAdapter.notifyDataSetChanged();
            }
        });

        mTranslationRepository.getAvailableNativeLanguages().observe(this, new Observer<>() {
            @Override
            public void onChanged(List<String> nativeLanguages) {
                mNativeLangAdapter.clear();
                if (nativeLanguages.size() > 1) {
                    mNativeLangAdapter.add(getString(R.string.select_native_lang));
                }
                mNativeLangAdapter.addAll(nativeLanguages);
                mNativeLangAdapter.notifyDataSetChanged();
            }
        });

        mBoardRepository.getAvailableBoardDimensions().observe(this, new Observer<>() {
            @Override
            public void onChanged(List<BoardDimension> boardDimensions) {
                mGridSizeAdapter.clear();
                if (boardDimensions.size() > 1) {
                    mGridSizeAdapter.add(getString(R.string.select_sudoku_level));
                }
                mGridSizeAdapter.addAll(convertToStrings(boardDimensions));
                mGridSizeAdapter.notifyDataSetChanged();

                mStringToGridSizeMap.clear();
                for (var dimen : boardDimensions) {
                    mStringToGridSizeMap.put(convertToString(dimen), dimen);
                }
            }
        });


//------------------------------------------------------------------------------------------------------------------------
        binding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = mGridSizeSpinner.getSelectedItem().toString();
                String learningLang = mLearningLangSpinner.getSelectedItem().toString();
                String nativeLang = mNativeLangSpinner.getSelectedItem().toString();
                if (size.contentEquals(getString(R.string.select_grid_size))
                        || learningLang.contentEquals(getString(R.string.select_learning_lang))
                        || nativeLang.contentEquals(getString(R.string.select_native_lang))
                        || learningLang.equals(nativeLang)) {
                    Toast.makeText(
                            MainActivity.this,
                            getString(R.string.spinner_not_selected),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    //used to send data
                    final var sizes = getGridSize();
                    startActivity(HomePage2.newIntent(MainActivity.this,
                            new HomePage2Args.Builder(
                                    nativeLang, learningLang,
                                    sizes.getBoardSize(),
                                    sizes.getSubgridHeight(), sizes.getSubgridWidth()
                            ).build()
                    ));
                }
            }
        });

        binding.imageButtonFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "it works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private BoardDimension getGridSize() {
        return mStringToGridSizeMap.get(mGridSizeSpinner.getSelectedItem().toString());
    }

    private String convertToString(BoardDimension boardDimension) {
        final var boardSize = boardDimension.getBoardSize();
        return "" + boardSize + " x " + boardSize; // e.g. "9 x 9"
    }

    private List<String> convertToStrings(List<BoardDimension> boardDimensions) {
        return boardDimensions
                .stream()
                .map(this::convertToString)
                .collect(Collectors.toList());
    }
}