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

public class MainActivity extends AppCompatActivity {

    ImageButton nextImageButton, favouritesImageButton, settingsImageButton,
            helpImageButton, tutorialImageButton, historyImageButton;
    private String selectedState, selectedDistrict, selectedGridSize; //vars to hold the values of state and district
    private TextView tvLearningLangSpinner, tvNativeLangSpinner, tvGridSizeSpinner; //declaring text view to show errors
    private Spinner learninglangSpinner, nativelangSpinner, GridSizeSpinner;


    View view;
    private ArrayAdapter<CharSequence> learninglangAdapter, nativelangAdapter, GridSizeAdapter;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.blue);
        //State spinner Initialization
        learninglangSpinner = findViewById(R.id.spinner_learning_lang);
        nativelangSpinner = findViewById(R.id.textView_native_lang);
        GridSizeSpinner = findViewById(R.id.spinner_grid_size);

        //Populate ArrayAdapter using string array and a spinner layout that we will define
        learninglangAdapter = ArrayAdapter.createFromResource(this, R.array.array_learning_lang, R.layout.spinner_layout);
        nativelangAdapter = ArrayAdapter.createFromResource(this, R.array.array_native_lang, R.layout.spinner_layout);
        GridSizeAdapter = ArrayAdapter.createFromResource(this, R.array.array_grid_size, R.layout.spinner_layout);

        //Specify the layout to use when the list of choices appear
        learninglangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nativelangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set the adapter to the spinner to populate State Spinner
        learninglangSpinner.setAdapter(learninglangAdapter);
        nativelangSpinner.setAdapter(nativelangAdapter);
        GridSizeSpinner.setAdapter(GridSizeAdapter);

        nextImageButton = (ImageButton) findViewById(R.id.image_button_next);

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = GridSizeSpinner.getSelectedItem().toString();
                if (size.equals("Select Grid Size")) {
                    Toast.makeText(MainActivity.this, "*Please select a valid grid size*", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, HomePage2.class);
                    //used to send data
                    // TODO: Error checking for these two.
                    intent.putExtra("native_lang", nativelangSpinner.getSelectedItem().toString());
                    intent.putExtra("learning_lang", learninglangSpinner.getSelectedItem().toString());
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