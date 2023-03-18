package ca.sfu.cmpt276.sudokulang;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ca.sfu.cmpt276.sudokulang.databinding.ActivityMainBinding;
import kotlin.Triple;

public class MainActivity extends AppCompatActivity {
    private Spinner learningLangSpinner, nativeLangSpinner, gridSizeSpinner;
    private ArrayAdapter<CharSequence> learningLangAdapter, nativeLangAdapter, gridSizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final var binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // State spinner Initialization
        learningLangSpinner = binding.spinnerLearningLang;
        nativeLangSpinner = binding.spinnerNativeLang;
        gridSizeSpinner = binding.spinnerGridSize;

        // Populate ArrayAdapter using string array and a spinner layout that we will define
        learningLangAdapter = ArrayAdapter.createFromResource(this, R.array.array_learning_lang, R.layout.spinner_layout);
        nativeLangAdapter = ArrayAdapter.createFromResource(this, R.array.array_native_lang, R.layout.spinner_layout);
        gridSizeAdapter = ArrayAdapter.createFromResource(this, R.array.array_grid_size, R.layout.spinner_layout);

        // Specify the layout to use when the list of choices appear
        learningLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        nativeLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner to populate State Spinner
        learningLangSpinner.setAdapter(learningLangAdapter);
        nativeLangSpinner.setAdapter(nativeLangAdapter);
        gridSizeSpinner.setAdapter(gridSizeAdapter);

        binding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = gridSizeSpinner.getSelectedItem().toString();
                String learningLang = learningLangSpinner.getSelectedItem().toString();
                String nativeLang = nativeLangSpinner.getSelectedItem().toString();
                if (size.contentEquals(gridSizeAdapter.getItem(0))
                        || learningLang.contentEquals(learningLangAdapter.getItem(0))
                        || nativeLang.contentEquals(nativeLangAdapter.getItem(0))) {
                    Toast.makeText(
                            MainActivity.this,
                            "*Please select a valid input for all fields*",
                            Toast.LENGTH_LONG
                    ).show();
                } else {
                    //used to send data
                    final var sizes = getGridSize();
                    startActivity(HomePage2.newIntent(MainActivity.this,
                            new HomePage2Args.Builder(
                                    nativeLang, learningLang,
                                    sizes.getFirst(), sizes.getSecond(), sizes.getThird()
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


    /**
     * @return A tuple of {@code (boardSize, subgridHeight, subgridWidth)}.
     */
    private Triple<Integer, Integer, Integer> getGridSize() {
        final var adapter = gridSizeAdapter;
        final var selectedItem = gridSizeSpinner.getSelectedItem().toString();
        if (selectedItem.contentEquals(adapter.getItem(1))) {
            return new Triple<>(4, 4, 4);
        }
        if (selectedItem.contentEquals(adapter.getItem(2))) {
            return new Triple<>(6, 2, 3);
        }
        if (selectedItem.contentEquals(adapter.getItem(3))) {
            return new Triple<>(9, 3, 3);
        }
        if (selectedItem.contentEquals(adapter.getItem(4))) {
            return new Triple<>(12, 3, 4);
        }
        throw new IllegalArgumentException("Unknown board dimension");
    }

}