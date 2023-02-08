package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private String selectedState,selectedDistrict ,selectedGridSize; //vars to hold the values of state and district
    private TextView tvStateSpinner, tvDistrictSpinner ,tvGridSizeSpinner; //declaring text view to show errors
    private Spinner stateSpinner, districtSpinner, GridSizeSpinner;
    private ArrayAdapter<CharSequence> stateAdapter, districtAdapter, GridSizeAdapter;


    View view;
    ImageButton nextImageButton, favouritesImageButton, settingsImageButton,
            helpImageButton, tutorialImageButton,historyImageButton;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        view=this.getWindow().getDecorView();
        view.setBackgroundResource(R.color.blue);
        //State spinner Initialization
        stateSpinner = findViewById(R.id.spinner_indian_states);
        districtSpinner= findViewById(R.id.spinner_indian_districts);
        GridSizeSpinner = findViewById(R.id.spinner_grid_size);

        //Populate ArrayAdapter using string array and a spinner layout that we will define
        stateAdapter = ArrayAdapter.createFromResource(this,R.array.array_indian_states,R.layout.spinner_layout);
        districtAdapter= ArrayAdapter.createFromResource(this,R.array.array_indian_districts,R.layout.spinner_layout);
        GridSizeAdapter = ArrayAdapter.createFromResource(this,R.array.array_grid_size,R.layout.spinner_layout);

        //Specify the layout to use when the list of choices appear
        stateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        GridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //set the adapter to the spinner to populate State Spinner
        stateSpinner.setAdapter(stateAdapter);
        districtSpinner.setAdapter(districtAdapter);
        GridSizeSpinner.setAdapter(GridSizeAdapter);

       nextImageButton= (ImageButton) findViewById(R.id.image_button_next);

        nextImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
                openHomePage2();
            }
        });



        favouritesImageButton= (ImageButton) findViewById(R.id.image_button_favourites);
        favouritesImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });
        settingsImageButton=(ImageButton) findViewById(R.id.image_button_settings);
        settingsImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        historyImageButton=(ImageButton) findViewById(R.id.image_button_history);
        historyImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
            }
        });

        helpImageButton=(ImageButton) findViewById(R.id.image_button_history);
        helpImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "it works", Toast.LENGTH_SHORT).show();
            }
        });

       tutorialImageButton=(ImageButton) findViewById(R.id.image_button_tutorial);
       tutorialImageButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Toast.makeText(MainActivity.this, "this works", Toast.LENGTH_SHORT).show();
           }
       });




    }

    public void openHomePage2(){
        Intent intent = new Intent(this, HomePage2.class);
        startActivity(intent);
    }
}