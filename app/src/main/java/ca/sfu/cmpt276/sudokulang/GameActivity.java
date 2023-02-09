package ca.sfu.cmpt276.sudokulang;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

import ca.sfu.cmpt276.sudokulang.databinding.ActivityGameBinding;

public class GameActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;

    private ActivityGameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.topAppToolbar);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_game, R.id.navigation_home, R.id.navigation_help,
                R.id.navigation_settings, R.id.navigation_translation)
                .build();
        // Cite: https://stackoverflow.com/a/60597670
        NavController navController = ((NavHostFragment) Objects.requireNonNull(
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_game)))
                .getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.topAppToolbar, navController);

        binding.fab.setOnClickListener(view -> Snackbar.make(view, "Pause FAB snackbar", Snackbar.LENGTH_SHORT)
                .setAction("Action", null).show());

        binding.bottomAppBar.setNavigationOnClickListener(view -> Toast.makeText(this,
                        "Picnic with toast and snacks",
                        Toast.LENGTH_SHORT)
                .show());

        // See: https://github.com/material-components/material-components-android/blob/master/docs/components/BottomAppBar.md
        binding.bottomAppBar.setOnMenuItemClickListener(menuItem -> {
            final var id = menuItem.getItemId();
            if (id == R.id.navigation_home) {
                navController.navigate(R.id.navigation_home);
                return true;
            } else if (id == R.id.navigation_help) {
                navController.navigate(R.id.navigation_help);
                return true;
            } // else if (id == R.id.navigation_settings) {
            //  navController.navigate(R.id.navigation_settings);
            //  return true;
            // } else {
            return false;
            // }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    // NOTE: No idea what's going on down here.
    //       Probably has to do with the back button.

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_game);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}