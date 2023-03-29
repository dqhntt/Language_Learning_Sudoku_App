package ca.sfu.cmpt276.sudokulang;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;

import ca.sfu.cmpt276.sudokulang.databinding.ActivityGameBinding;
import ca.sfu.cmpt276.sudokulang.ui.game.GameFragmentDirections;

public class GameActivity extends AppCompatActivity {
    private static final String SHOULD_CREATE_NEW_GAME = "should_create_new_game";
    private GameViewModel gameViewModel;
    private @Nullable AppBarConfiguration appBarConfiguration = null;
    private Snackbar snackbar;

    /**
     * Create a new intent with the required arguments for {@link GameActivity}.
     *
     * @param packageContext Context of the calling activity.
     * @param args           NavArgs built with: {@code new GameActivityArgs.Builder(...).build()}
     */
    public static Intent newIntent(@NonNull Context packageContext, @NonNull GameActivityArgs args) {
        final var intent = new Intent(packageContext, GameActivity.class);
        intent.putExtras(args.toBundle());
        return intent;
    }

    private static boolean shouldCreateNewGame(@Nullable Bundle savedInstanceState) {
        return savedInstanceState == null || savedInstanceState.getBoolean(SHOULD_CREATE_NEW_GAME);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final var binding = ActivityGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        // Check if recreating a previously destroyed instance.
        if (shouldCreateNewGame(savedInstanceState)) {
            startNewGame();
        }

        // Cite: https://stackoverflow.com/a/60597670
        NavController navController = ((NavHostFragment)
                getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment_game))
                .getNavController();

        if (binding.topAppBar != null) {
            setSupportActionBar(binding.topAppBar);
            appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
            NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
            NavigationUI.setupWithNavController(binding.topAppBar, navController);
        }

        if (binding.bottomAppBar != null) {
            assert (binding.fab != null);
            snackbar = Snackbar
                    .make(binding.fab, "", Snackbar.LENGTH_INDEFINITE)
                    .setAnchorView(binding.bottomAppBar);
            binding.fab.setOnClickListener(view -> {
                if (gameViewModel.isGameInProgress().getValue()) {
                    gameViewModel.pauseGame();
                    snackbar.setText(Util.formatWithTime(
                            getString(R.string.paused_message),
                            gameViewModel.getElapsedTime()
                    )).show();
                } else {
                    gameViewModel.resumeGame();
                    snackbar.dismiss();
                }
            });
            gameViewModel.isGameInProgress().observe(this, gameInProgress -> {
                binding.fab.setImageResource(gameInProgress
                        ? R.drawable.ic_pause_24dp
                        : R.drawable.ic_play_arrow_24dp);
                if (gameInProgress) {
                    snackbar.dismiss();
                }
            });
            binding.bottomAppBar.setOnMenuItemClickListener(getOnMenuItemClickListener(navController));
        }
    }

    public void startNewGame() {
        final var args = GameActivityArgs.fromBundle(getIntent().getExtras());
        try {
            gameViewModel.startNewGame(
                    args.getNativeLang(),
                    args.getLearningLang(),
                    args.getLangLevel(),
                    args.getSudokuLevel(),
                    args.getBoardSize(),
                    args.getSubgridHeight(),
                    args.getSubgridWidth()
            );
        } catch (SQLiteException e) {
            Log.e(getClass().getTypeName(), "Game database exception occurred", e);
            recreate(); // Restart activity.
        }
    }

    // See: https://github.com/material-components/material-components-android/blob/master/docs/components/BottomAppBar.md
    private Toolbar.OnMenuItemClickListener getOnMenuItemClickListener(NavController navController) {
        return menuItem -> {
            final var id = menuItem.getItemId();
            if (id == R.id.main_activity) {
                finishAfterTransition();
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
        if (id == R.id.action_reset) {
            gameViewModel.resetGame();
            return true;
        } else if (id == R.id.action_new_game) {
            startNewGame();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Handle back button in action bar.
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_game);
        assert (appBarConfiguration != null);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        // Save game state to the instance state bundle.
        outState.putBoolean(SHOULD_CREATE_NEW_GAME, false);
    }
}