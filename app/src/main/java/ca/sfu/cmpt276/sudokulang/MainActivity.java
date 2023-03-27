package ca.sfu.cmpt276.sudokulang;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import java.util.List;

import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepository;
import ca.sfu.cmpt276.sudokulang.data.source.TranslationRepositoryImpl;
import ca.sfu.cmpt276.sudokulang.databinding.ActivityMainBinding;
import kotlin.Triple;

public class MainActivity extends AppCompatActivity {
    private Spinner learningLangSpinner, nativeLangSpinner, gridSizeSpinner;
    private ArrayAdapter<CharSequence> learningLangAdapter, nativeLangAdapter, gridSizeAdapter;

    private Spinner mLearningLangSpinner, mNativeLangSpinner;
    private TranslationRepository mTranslationRepository;
    private ArrayAdapter<String> mLearningLangAdapter, mNativeLangAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final var binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mLearningLangSpinner = findViewById(R.id.spinner_learning_lang);
        mNativeLangSpinner = findViewById(R.id.spinner_native_lang);
        mTranslationRepository = TranslationRepositoryImpl.getInstance(getApplicationContext());

        // Initialize the adapter for the spinner
        mLearningLangAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        mLearningLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLearningLangSpinner.setAdapter(mLearningLangAdapter);

        mNativeLangAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        mNativeLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mNativeLangSpinner.setAdapter(mNativeLangAdapter);

        // Observe the LiveData returned by the repository
        mTranslationRepository.getAvailableLearningLanguages().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> learningLanguages) {
                mLearningLangAdapter.clear();
                mLearningLangAdapter.add(getString(R.string.select_learning_lang));
                mLearningLangAdapter.addAll(learningLanguages);
                mLearningLangAdapter.notifyDataSetChanged();

                mNativeLangAdapter.clear();
                mNativeLangAdapter.add(getString(R.string.select_native_lang));
                mNativeLangAdapter.addAll(learningLanguages);
                mLearningLangAdapter.notifyDataSetChanged();
            }
        });


//-------------------------------------------------------------------------------------------
        // State spinner Initialization
//        learningLangSpinner = binding.spinnerLearningLang;
//        nativeLangSpinner = binding.spinnerNativeLang;
        gridSizeSpinner = binding.spinnerGridSize;

        // Populate ArrayAdapter using string array and a spinner layout that we will define
//        learningLangAdapter = ArrayAdapter.createFromResource(this, R.array.array_learning_lang, R.layout.spinner_layout);
//        nativeLangAdapter = ArrayAdapter.createFromResource(this, R.array.array_native_lang, R.layout.spinner_layout);
        gridSizeAdapter = ArrayAdapter.createFromResource(this, R.array.array_grid_size, R.layout.spinner_layout);

        // Specify the layout to use when the list of choices appear
//        learningLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        nativeLangAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gridSizeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Set the adapter to the spinner to populate State Spinner
//        learningLangSpinner.setAdapter(learningLangAdapter);
//        nativeLangSpinner.setAdapter(nativeLangAdapter);
        gridSizeSpinner.setAdapter(gridSizeAdapter);

//------------------------------------------------------------------------------------------------------------------------
        binding.imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String size = gridSizeSpinner.getSelectedItem().toString();
                String learningLang = mLearningLangSpinner.getSelectedItem().toString();
                String nativeLang = mNativeLangSpinner.getSelectedItem().toString();
                if (size.contentEquals(gridSizeAdapter.getItem(0))
                        || learningLang.contentEquals(mLearningLangAdapter.getContext().getString(R.string.select_learning_lang))
                        || nativeLang.contentEquals(mNativeLangAdapter.getContext().getString(R.string.select_native_lang))) {
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

    private void getlang() {
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

    private Context getContext() {
        return getApplicationContext();
    }


}