package ca.sfu.cmpt276.sudokulang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.source.BoardRepository;
import ca.sfu.cmpt276.sudokulang.data.source.BoardRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepository;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.databinding.ActivityHomePage2Binding;

public class HomePage2 extends AppCompatActivity {
    private TranslationRepository mTranslationRepository;
    private BoardRepository mBoardRepository;
    private Spinner mSudokuSpinner, mlanglevelSpinner;
    private ArrayAdapter<CharSequence> mSudokuAdapter, mlanglevelAdapter;

    /**
     * Create a new intent with the required arguments for {@link HomePage2}.
     *
     * @param packageContext Context of the calling activity.
     * @param args           NavArgs built with: {@code new HomePage2Args.Builder(...).build()}
     */
    public static Intent newIntent(@NonNull Context packageContext, @NonNull HomePage2Args args) {
        final var intent = new Intent(packageContext, HomePage2.class);
        intent.putExtras(args.toBundle());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final var binding = ActivityHomePage2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        final var extras = HomePage2Args.fromBundle(getIntent().getExtras());


        //------------------------------------------SPINNER INITIALIZATION--------------------------------------------------------

        // Find the spinners using view binding
        mlanglevelSpinner = binding.spinnerLangLevel;
        mSudokuSpinner = binding.spinnerSudokuLevel;

        // Initialize the repositories
        mTranslationRepository = TranslationRepositoryImpl.getInstance(this);
        mBoardRepository = BoardRepositoryImpl.getInstance(this);

        // Initialize the adapter for the spinner
        mlanglevelAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout);
        mSudokuAdapter = new ArrayAdapter<>(this, R.layout.spinner_layout);
        populateSudokuAdapter(extras);

        // Specify the layout to use when the list of choices appear
        mlanglevelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSudokuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to populate the spinner
        mlanglevelSpinner.setAdapter(mlanglevelAdapter);
        mSudokuSpinner.setAdapter(mSudokuAdapter);


        // Observe the LiveData returned by the repository
        mTranslationRepository.getAvailableLanguageLevels().observe(this, new Observer<>() {
            @Override
            public void onChanged(List<String> languageLevels) {
                mlanglevelAdapter.clear();
                if (languageLevels.size() > 1) {
                    mlanglevelAdapter.add(getString(R.string.select_lang_level));
                }
                mlanglevelAdapter.addAll(languageLevels);
                mlanglevelAdapter.notifyDataSetChanged();
            }
        });


        //--------------------------------------BUTTON INITIALIZATION-----------------------------------------------------------------

        binding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String langLevel = mlanglevelSpinner.getSelectedItem().toString();
                String sudokuLevel = mSudokuSpinner.getSelectedItem().toString();

                if (langLevel.contentEquals(getString(R.string.select_lang_level))
                        || sudokuLevel.contentEquals(getString(R.string.select_sudoku_level))) {
                    Toast.makeText(
                            HomePage2.this,
                            getString(R.string.spinner_not_selected),
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Loading....", Toast.LENGTH_SHORT).show();
                    //gets the intent value from MainActivity and stores it
                    startActivity(GameActivity.newIntent(HomePage2.this,
                            new GameActivityArgs.Builder(
                                    extras.getNativeLang(),
                                    extras.getLearningLang(),
                                    langLevel,
                                    sudokuLevel,
                                    extras.getBoardSize(),
                                    extras.getSubgridHeight(),
                                    extras.getSubgridWidth(),
                                    true // TODO: Replace with a proper check and variable.
                            ).build()
                    ));
                }
            }
        });

        binding.textViewLangLevel.setText(getString(R.string.lang_level).replace(
                "(*input*)",
                HomePage2Args.fromBundle(getIntent().getExtras()).getLearningLang()
        ));

        binding.imageButtonFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "it works", Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageButtonTutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void populateSudokuAdapter(HomePage2Args extras) {
        final var boardLevels = mBoardRepository.getAvailableBoardLevelsByDimension(
                extras.getBoardSize(), extras.getSubgridHeight(), extras.getSubgridWidth());
        if (boardLevels.size() > 1) {
            mSudokuAdapter.add(getString(R.string.select_sudoku_level));
        }
        mSudokuAdapter.addAll(boardLevels);
    }

}