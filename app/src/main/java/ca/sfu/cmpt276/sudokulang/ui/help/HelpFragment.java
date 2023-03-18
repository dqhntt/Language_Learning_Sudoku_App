package ca.sfu.cmpt276.sudokulang.ui.help;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import ca.sfu.cmpt276.sudokulang.R;
import ca.sfu.cmpt276.sudokulang.databinding.FragmentHelpBinding;

// See: https://developer.android.com/topic/libraries/architecture/viewmodel
public class HelpFragment extends Fragment {

    private FragmentHelpBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HelpViewModel helpViewModel =
                new ViewModelProvider(this).get(HelpViewModel.class);

        binding = FragmentHelpBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textviewHelp;
        helpViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonHelp.setOnClickListener(v ->
                NavHostFragment
                        .findNavController(this)
                        .navigate(R.id.main_activity));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}