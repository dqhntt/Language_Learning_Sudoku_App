package ca.sfu.cmpt276.sudokulang;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
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
import ca.sfu.cmpt276.sudokulang.ui.game.GameFragmentArgs;
import ca.sfu.cmpt276.sudokulang.ui.game.GameFragmentDirections;

public class GameActivity extends AppCompatActivity {
    private @Nullable AppBarConfiguration appBarConfiguration = null;
    private ActivityGameBinding binding;

    /**
     * Create a new intent with the required arguments for {@code GameActivity}.
     *
     * @param packageContext Context of the calling activity.
     * @param args           NavArgs built with: {@code new GameFragmentArgs.Builder(...).build()}
     */
    public static Intent newIntent(@NonNull Context packageContext, @NonNull GameFragmentArgs args) {
        final var intent = new Intent(packageContext, GameActivity.class);
        intent.putExtras(args.toBundle());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Cite: https://stackoverflow.com/a/60597670
        NavController navController = ((NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_content_game))
                .getNavController();
        // Forward the Intent extras as arguments to the host fragment = GameFragment.
        // Cite: https://developer.android.com/guide/navigation/navigation-migrate#pass_intent_extras_to_the_fragment
        navController.setGraph(R.navigation.nav_graph, getIntent().getExtras());

        if (binding.topAppToolbar != null) {
            setSupportActionBar(binding.topAppToolbar);
            // Passing each menu ID as a set of Ids because each
            // menu should be considered as top level destinations.
            appBarConfiguration = new AppBarConfiguration.Builder(
                    R.id.game_fragment, R.id.main_activity, R.id.help_fragment, R.id.translation_fragment)
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
            if (id == R.id.main_activity) {
                navController.navigate(GameFragmentDirections.actionGameFragmentToMainActivity());
                return true;
            } else if (id == R.id.help_fragment) {
                navController.navigate(GameFragmentDirections.actionGameFragmentToHelpFragment());
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
        // Handle back button in action bar.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_game);
        assert (appBarConfiguration != null);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}