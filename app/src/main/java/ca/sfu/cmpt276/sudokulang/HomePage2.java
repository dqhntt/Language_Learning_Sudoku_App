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

import ca.sfu.cmpt276.sudokulang.databinding.ActivityHomePage2Binding;

public class HomePage2 extends AppCompatActivity {
    private Spinner langSpinner, sudokuSpinner;
    private ArrayAdapter<CharSequence> langAdapter, sudokuAdapter; //only declaration

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


        //------------------------------------------SPINNER INITIALIZATION--------------------------------------------------------

        langSpinner = binding.spinnerLangLevel;
        sudokuSpinner = binding.spinnerSudokuLevel;

        // Populate ArrayAdapter using string array and a spinner layout that we will define
        langAdapter = ArrayAdapter.createFromResource(this, R.array.array_lang_level, R.layout.spinner_layout);
        sudokuAdapter = ArrayAdapter.createFromResource(this, R.array.array_sudoku_level, R.layout.spinner_layout);

        // Specify the layout to use when the list of choices appear
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sudokuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set adapter to spinner to populate the Lang Spinner
        langSpinner.setAdapter(langAdapter);
        sudokuSpinner.setAdapter(sudokuAdapter);


        //--------------------------------------BUTTON INITIALIZATION-----------------------------------------------------------------

        binding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String langLevel = langSpinner.getSelectedItem().toString();
                String sudokuLevel = sudokuSpinner.getSelectedItem().toString();

                if (langLevel.contentEquals(langAdapter.getItem(0))
                        || sudokuLevel.contentEquals(sudokuAdapter.getItem(0))) {
                    Toast.makeText(
                            HomePage2.this,
                            "*Please select a valid input for all fields*",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Loading....", Toast.LENGTH_SHORT).show();
                    //gets the intent value from MainActivity and stores it
                    final var extras = HomePage2Args.fromBundle(getIntent().getExtras());
                    startActivity(GameActivity.newIntent(HomePage2.this,
                            new GameActivityArgs.Builder(
                                    extras.getNativeLang(),
                                    extras.getLearningLang(),
                                    langLevel,
                                    sudokuLevel,
                                    extras.getBoardSize(),
                                    extras.getSubgridHeight(),
                                    extras.getSubgridWidth()
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

}