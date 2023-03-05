package ca.sfu.cmpt276.sudokulang;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class HomePage2 extends AppCompatActivity {

    private String selectedLang, selectedSudoku; //cars to hold the values of teh selected language and sudoku level
    private TextView tvLangSpinner, tvSudokuSpinner; //declaring TextView to show errors
    private Spinner LangSpinner, SudokuSpinner;
    private ArrayAdapter<CharSequence> LangAdapter, SudokuAdapter; //only declaration











    ImageButton nextImageButton, favouritesImageButton, settingsImageButton,
            helpImageButton, tutorialImageButton,historyImageButton;

    View view;




    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page2);



        //------------------------------------------SPINNER INITIALIZATION--------------------------------------------------------

        LangSpinner = findViewById(R.id.spinner_lang_level);
        SudokuSpinner=findViewById(R.id.spinner_sudoku_level);
        //populate ArrayAdapter using string array and a spinner layout that we will define
        LangAdapter = ArrayAdapter.createFromResource(this,R.array.array_lang_level,R.layout.spinner_layout);
        SudokuAdapter=ArrayAdapter.createFromResource(this,R.array.array_sudoku_level,R.layout.spinner_layout);
        //specify the layout to use when the list of choices appear
        LangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SudokuAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set adapter to spinner to populate the Lang Spinner
        LangSpinner.setAdapter(LangAdapter);
        SudokuSpinner.setAdapter(SudokuAdapter);



        view=this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.blue);








        //--------------------------------------BUTTON INITIALIZATION-----------------------------------------------------------------
        nextImageButton= (ImageButton) findViewById(R.id.image_button_next);
        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "Loading....", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(HomePage2.this, GameActivity.class));
                Intent intent = getIntent();

                //gets the intent value from MainActivity and stores it
                String size = intent.getStringExtra("grid_size");

                //makes a toast, printing out the selected grid size value-----for testing only
                Toast.makeText(HomePage2.this, size, Toast.LENGTH_SHORT).show();
            }
        });


        favouritesImageButton = (ImageButton) findViewById(R.id.image_button_favourites);
        favouritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
        settingsImageButton=(ImageButton) findViewById(R.id.image_button_settings);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        historyImageButton=(ImageButton) findViewById(R.id.image_button_history);
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

        tutorialImageButton=(ImageButton) findViewById(R.id.image_button_tutorial);
        tutorialImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(HomePage2.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });




    }


}