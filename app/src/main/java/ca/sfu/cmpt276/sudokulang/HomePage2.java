package ca.sfu.cmpt276.sudokulang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HomePage2 extends AppCompatActivity {
    ImageButton nextImageButton, favouritesImageButton, settingsImageButton,
            helpImageButton, tutorialImageButton, historyImageButton;
    private String selectedLang, selectedSudoku; //vars to hold the values of the selected language and sudoku level
    private TextView tvLangSpinner, tvSudokuSpinner; //declaring TextView to show errors
    private Spinner langSpinner, sudokuSpinner;
    private ArrayAdapter<CharSequence> langAdapter, sudokuAdapter; //only declaration

    /**
     * Create a new intent with the required arguments for {@code HomePage2Args}.
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

        nextImageButton = findViewById(R.id.image_button_next);
        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Loading....", Toast.LENGTH_SHORT).show();

                //gets the intent value from MainActivity and stores it
                final var extras = HomePage2Args.fromBundle(getIntent().getExtras());

                startActivity(GameActivity.newIntent(HomePage2.this,
                        new GameActivityArgs.Builder(
                                extras.getNativeLang(),
                                extras.getLearningLang(),
                                // TODO: Error checking for these two.
                                langSpinner.getSelectedItem().toString(),
                                sudokuSpinner.getSelectedItem().toString(),
                                extras.getBoardSize(),
                                extras.getSubgridHeight(),
                                extras.getSubgridWidth()
                        ).build())
                );
            }
        });


        favouritesImageButton = findViewById(R.id.image_button_favourites);
        favouritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
        settingsImageButton = findViewById(R.id.image_button_settings);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        historyImageButton = findViewById(R.id.image_button_history);
        historyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        helpImageButton = findViewById(R.id.image_button_help);
        helpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "it works", Toast.LENGTH_SHORT).show();
            }
        });

        tutorialImageButton = findViewById(R.id.image_button_tutorial);
        tutorialImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
    }

}