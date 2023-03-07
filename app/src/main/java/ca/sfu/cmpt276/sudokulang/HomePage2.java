package ca.sfu.cmpt276.sudokulang;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.sudokulang.ui.game.GameFragmentArgs;
import kotlin.Triple;

public class HomePage2 extends AppCompatActivity {
    ImageButton nextImageButton, favouritesImageButton, settingsImageButton,
            helpImageButton, tutorialImageButton, historyImageButton;
    private String selectedLang, selectedSudoku; //vars to hold the values of the selected language and sudoku level
    private TextView tvLangSpinner, tvSudokuSpinner; //declaring TextView to show errors
    private Spinner langSpinner, sudokuSpinner;
    private ArrayAdapter<CharSequence> langAdapter, sudokuAdapter; //only declaration

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);

        // Set background color.
        this.getWindow().getDecorView().setBackgroundResource(R.color.blue);


        //------------------------------------------SPINNER INITIALIZATION--------------------------------------------------------

        langSpinner = findViewById(R.id.spinner_lang_level);
        sudokuSpinner = findViewById(R.id.spinner_sudoku_level);

        //populate ArrayAdapter using string array and a spinner layout that we will define
        langAdapter = ArrayAdapter.createFromResource(this, R.array.array_lang_level, R.layout.spinner_layout);
        sudokuAdapter = ArrayAdapter.createFromResource(this, R.array.array_sudoku_level, R.layout.spinner_layout);

        //specify the layout to use when the list of choices appear
        langAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sudokuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set adapter to spinner to populate the Lang Spinner
        langSpinner.setAdapter(langAdapter);
        sudokuSpinner.setAdapter(sudokuAdapter);


        //--------------------------------------BUTTON INITIALIZATION-----------------------------------------------------------------

        nextImageButton = (ImageButton) findViewById(R.id.image_button_next);
        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Loading....", Toast.LENGTH_SHORT).show();

                Intent intent = getIntent();
                //gets the intent value from MainActivity and stores it
                var sizes = parseGridSize(intent.getStringExtra("grid_size"));

                startActivity(GameActivity.newIntent(HomePage2.this,
                        new GameFragmentArgs.Builder(
                                intent.getStringExtra("native_lang"),
                                intent.getStringExtra("learning_lang"),
                                // TODO: Error checking for these two.
                                langSpinner.getSelectedItem().toString(),
                                sudokuSpinner.getSelectedItem().toString(),
                                sizes.getFirst(),
                                sizes.getSecond(),
                                sizes.getThird()
                        ).build())
                );
            }
        });


        favouritesImageButton = (ImageButton) findViewById(R.id.image_button_favourites);
        favouritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
        settingsImageButton = (ImageButton) findViewById(R.id.image_button_settings);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        historyImageButton = (ImageButton) findViewById(R.id.image_button_history);
        historyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        helpImageButton = (ImageButton) findViewById(R.id.image_button_help);
        helpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "it works", Toast.LENGTH_SHORT).show();
            }
        });

        tutorialImageButton = (ImageButton) findViewById(R.id.image_button_tutorial);
        tutorialImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * @return A tuple of {@code (boardSize, subgridHeight, subgridWidth)}.
     * @implNote TODO: Replace with a better way. Perhaps parse in MainActivity beforehand.
     */
    private Triple<Integer, Integer, Integer> parseGridSize(String size) {
        for (char c : size.toCharArray()) {
            if (Character.isDigit(c)) {
                switch (c) {
                    case '4':
                        return new Triple<>(4, 4, 4);
                    case '6':
                        return new Triple<>(6, 2, 3);
                    case '9':
                        return new Triple<>(9, 3, 3);
                    case '1':
                        return new Triple<>(12, 3, 4);
                    default:
                        throw new IllegalArgumentException("Unknown board dimension");
                }
            }
        }
        throw new IllegalArgumentException("Cannot parse grid size");
    }

}