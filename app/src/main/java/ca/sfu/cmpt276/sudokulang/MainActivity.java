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

public class MainActivity extends AppCompatActivity {
    ImageButton nextImageButton, favouritesImageButton, settingsImageButton,
            helpImageButton, tutorialImageButton, historyImageButton;
    private String selectedState, selectedDistrict, selectedGridSize; //vars to hold the values of state and district
    private TextView tvLearningLangSpinner, tvNativeLangSpinner, tvGridSizeSpinner; //declaring text view to show errors
    private Spinner learningLangSpinner, nativeLangSpinner, gridSizeSpinner;
    private ArrayAdapter<CharSequence> learningLangAdapter, nativeLangAdapter, gridSizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set background color.
        this.getWindow().getDecorView().setBackgroundResource(R.color.blue);

        //State spinner Initialization
        learningLangSpinner = findViewById(R.id.spinner_learning_lang);
        nativeLangSpinner = findViewById(R.id.textView_native_lang);
        gridSizeSpinner = findViewById(R.id.spinner_grid_size);

        //Populate ArrayAdapter using string array and a spinner layout that we will define
        learningLangAdapter = ArrayAdapter.createFromResource(this, R.array.array_learning_lang, R.layout.spinner_layout);
        nativeLangAdapter = ArrayAdapter.createFromResource(this, R.array.array_native_lang, R.layout.spinner_layout);
        gridSizeAdapter = ArrayAdapter.createFromResource(this, R.array.array_grid_size, R.layout.spinner_layout);

        //Specify the layout to use when the list of choices appear
        learningLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nativeLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set the adapter to the spinner to populate State Spinner
        learningLangSpinner.setAdapter(learningLangAdapter);
        nativeLangSpinner.setAdapter(nativeLangAdapter);
        gridSizeSpinner.setAdapter(gridSizeAdapter);

        nextImageButton = (ImageButton) findViewById(R.id.image_button_next);

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = gridSizeSpinner.getSelectedItem().toString();
                if (size.contentEquals(gridSizeAdapter.getItem(0))) {
                    Toast.makeText(MainActivity.this, "*Please select a valid grid size*", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, HomePage2.class);
                    //used to send data
                    // TODO: Error checking for these two.
                    intent.putExtra("native_lang", nativeLangSpinner.getSelectedItem().toString());
                    intent.putExtra("learning_lang", learningLangSpinner.getSelectedItem().toString());
                    intent.putExtra("grid_size", size);

                    startActivity(intent);
                }
            }
        });


        favouritesImageButton = (ImageButton) findViewById(R.id.image_button_favourites);
        favouritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
        settingsImageButton = (ImageButton) findViewById(R.id.image_button_settings);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        historyImageButton = (ImageButton) findViewById(R.id.image_button_history);
        historyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        helpImageButton = (ImageButton) findViewById(R.id.image_button_help);
        helpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "it works", Toast.LENGTH_SHORT).show();
            }
        });

        tutorialImageButton = (ImageButton) findViewById(R.id.image_button_tutorial);
        tutorialImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
    }

}