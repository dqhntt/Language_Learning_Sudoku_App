package ca.sfu.cmpt276.sudokulang;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import ca.sfu.cmpt276.sudokulang.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {
    private @Nullable AppBarConfiguration appBarConfiguration = null;
    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cite: https://stackoverflow.com/a/60597670
        NavController navController = ((NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_game))
                .getNavController();

        if (binding.topAppToolbar != null) {
            setSupportActionBar(binding.topAppToolbar);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.navigation_game, R.id.navigation_home, R.id.navigation_help, R.id.navigation_translation)
                    .build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.topAppToolbar, navController);
        }

        if (binding.bottomAppBar != null) {
            assert (binding.fab != null);
            binding.fab.setOnClickListener(view ->
                    Snackbar.make(view, "Pause FAB", Snackbar.LENGTH_SHORT).show());
            binding.bottomAppBar.setOnMenuItemClickListener(getOnMenuItemClickListener(navController));
        }
    }

    // See: https://github.com/material-components/material-components-android/blob/master/docs/components/BottomAppBar.md
    private Toolbar.OnMenuItemClickListener getOnMenuItemClickListener(NavController navController) {
        return menuItem -> {
            final var id = menuItem.getItemId();
            if (id == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
                return true;
            } else if (id == R.id.navigation_help) {
                navController.navigate(R.id.navigation_help);
                return true;
            }
            return false;
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // TODO: Navigate to settings activity.
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle back button.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_game);
        assert (appBarConfiguration != null);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}