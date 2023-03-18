package ca.sfu.cmpt276.sudokulang.ui.translation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import ca.sfu.cmpt276.sudokulang.databinding.FragmentTranslationBinding;

public class TranslationFragment extends Fragment {

    private FragmentTranslationBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TranslationViewModel translationViewModel =
                new ViewModelProvider(this).get(TranslationViewModel.class);

        binding = FragmentTranslationBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textviewTranslation;
        translationViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonTranslation.setOnClickListener(v ->
                NavHostFragment
                        .findNavController(this)
                        .navigate(TranslationFragmentDirections.actionTranslationFragmentToMainActivity()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}