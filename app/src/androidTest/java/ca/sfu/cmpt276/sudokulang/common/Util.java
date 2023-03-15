package ca.sfu.cmpt276.sudokulang.common;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.Configurator;
import androidx.test.uiautomator.Direction;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class Util {
    public static final String APP_PACKAGE_NAME = "ca.sfu.cmpt276.sudokulang";
    public static final int CLICK_TIMEOUT = 100;
    public static final int SELECTOR_TIMEOUT = 500;
    private static final int LAUNCH_TIMEOUT = 5000;

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.
     *
     * @cite <a href="https://github.com/android/testing-samples/blob/main/ui/uiautomator/BasicSample/app/src/androidTest/java/com/example/android/testing/uiautomator/BasicSample/ChangeTextBehaviorTest.java">Sample</a>
     */
    private static String getLauncherPackageName() {
        // Create launcher Intent
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);

        // Use PackageManager to get the launcher package name
        PackageManager pm = getApplicationContext().getPackageManager();
        ResolveInfo resolveInfo = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return resolveInfo.activityInfo.packageName;
    }

    /**
     * @param shortIdName ID of the view as defined in its XML layout file. <p>
     *                    e.g. {@code "@+id/welcome_text"} -> {@code "welcome_text"}
     * @return Fully qualified name of {@code shortIdName}
     * for use with {@link By#res(String)} or {@link UiSelector#resourceId(String)}.
     */
    public static String getResourceId(@NonNull String shortIdName) {
        return shortIdName.startsWith(APP_PACKAGE_NAME)
                ? shortIdName
                : APP_PACKAGE_NAME + ":id/" + shortIdName;
    }

    /**
     * @see <a href="https://stackoverflow.com/questions/40881680/whats-is-the-difference-between-uiobject-and-uiobject2-other-than-uiautomator-2">Differences between UiObject and UiObject2</a>
     */
    public static class TestHelper {
        public static final UiDevice DEVICE = UiDevice.getInstance(getInstrumentation());

        /**
         * @see #wait(long)
         */
        public static void pause(long timeout) {
            synchronized (DEVICE) {
                try {
                    DEVICE.wait(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        private static void longWaitForWindowUpdate() {
            DEVICE.waitForWindowUpdate(APP_PACKAGE_NAME, LAUNCH_TIMEOUT);
        }

        public static void rotateDevice() throws RemoteException {
            if (DEVICE.isNaturalOrientation()) {
                putDeviceInLandscapeMode();
            } else {
                DEVICE.setOrientationNatural();
                longWaitForWindowUpdate();
                pause(SELECTOR_TIMEOUT);
            }
        }

        public static void putDeviceInLandscapeMode() throws RemoteException {
            if (new Random().nextBoolean()) {
                DEVICE.setOrientationRight();
            } else {
                DEVICE.setOrientationLeft();
            }
            longWaitForWindowUpdate();
            pause(SELECTOR_TIMEOUT);
        }

        public static void open(@NonNull UiObject spinner) throws UiObjectNotFoundException {
            if (spinner.exists()) {
                spinner.clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
                pause(SELECTOR_TIMEOUT);
            } else {
                throw new UiObjectNotFoundException("Cannot open nonexistent spinner");
            }
        }

        @NonNull
        public static String getSpinnerText(@NonNull UiObject spinner) throws UiObjectNotFoundException {
            assertTrue(spinner.exists());
            return spinner.getChild(new UiSelector().className(android.widget.TextView.class)).getText();
        }

        @NonNull
        public static List<UiObject2> getUpdatedMenus() {
            return DEVICE.findObjects(By.clazz(android.widget.CheckedTextView.class));
        }

        public static void selectMenuItem(@NonNull UiObject2 item) {
            item.clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
        }

        /**
         * @return Text of the selected menu item.
         */
        private static String selectRandomMenuItem() {
            final var menus = getUpdatedMenus();
            // Assuming there is a header.
            final int index = new Random().nextInt(menus.size() - 1) + 1;
            final var selectedMenu = menus.get(index);
            final var selectedText = selectedMenu.getText();
            selectMenuItem(selectedMenu);
            return selectedText;
        }

        /**
         * @return The word button keypad.
         */
        public static UiObject2 bringWordButtonsIntoView() throws UiObjectNotFoundException {
            if (DEVICE.isNaturalOrientation()) {
                searchForId("erase_button");
            }
            return getId2NoScroll("word_button_keypad");
        }

        /**
         * @return The game board.
         */
        public static UiObject2 bringGameBoardIntoView() throws UiObjectNotFoundException {
            if (DEVICE.isNaturalOrientation()) {
                searchForId("game_board");
                getId2NoScroll("game_board").fling(Direction.DOWN);
            }
            return getId2NoScroll("game_board");
        }

        public static long getNonemptyVisibleCellCount(@NonNull UiObject2 gameBoard) {
            // Note: Calling `getText()` on a blank cell returns `null`, not "".
            return gameBoard
                    .getChildren()
                    .stream()
                    .filter(view -> view.isClickable()
                            && view.getText() != null
                            && !view.getText().isBlank())
                    .count();
        }

        public static long getVisibleCellCount(@NonNull UiObject2 gameBoard) {
            return gameBoard.getChildren().stream().filter(UiObject2::isClickable).count();
        }

        public static List<UiObject2> getVisibleCells(@NonNull UiObject2 gameBoard) {
            return gameBoard
                    .getChildren()
                    .stream()
                    .filter(UiObject2::isClickable)
                    .collect(Collectors.toList());
        }

        @NonNull
        private static UiObject scrollAndGetView(UiSelector selector) throws UiObjectNotFoundException {
            new UiScrollable(new UiSelector().classNameMatches("^(android\\.widget\\.)?"
                    + "((Horizontal|Nested|)ScrollView|(List|Grid|Recycler)View)$")
            ).scrollIntoView(selector);
            return DEVICE.findObject(selector);
        }

        @NonNull
        private static UiObject scrollAndGetId(String id) throws UiObjectNotFoundException {
            return scrollAndGetView(new UiSelector().resourceId(getResourceId(id)));
        }

        @NonNull
        private static UiObject getIdNoScroll(String id) {
            return DEVICE.findObject(new UiSelector().resourceId(getResourceId(id)));
        }

        /**
         * @see UiDevice#findObject(BySelector)
         */
        @Nullable
        public static UiObject2 getId2NoScroll(String id) {
            return DEVICE.findObject(By.res(getResourceId(id)));
        }

        /**
         * Find object with {@code id}, scroll everywhere if needed.
         */
        public static UiObject searchForId(String id) throws UiObjectNotFoundException {
            final var obj = getIdNoScroll(id);
            return obj.exists() ? obj : scrollAndGetId(id);
        }

        private static int parseBoardSize(@NonNull String size) {
            switch (size.charAt(0)) {
                case '4':
                    return 4;
                case '6':
                    return 6;
                case '9':
                    return 9;
                case '1':
                    return 12;
                default:
                    throw new IllegalArgumentException("Cannot parse board size");
            }
        }

        // Cite: https://github.com/android/testing-samples/blob/main/ui/uiautomator/BasicSample/app/src/androidTest/java/com/example/android/testing/uiautomator/BasicSample/ChangeTextBehaviorTest.java
        public static void startAppInPortraitMode() throws RemoteException {
            assertNotNull(DEVICE);

            // Set timeout.
            Configurator.getInstance().setWaitForSelectorTimeout(SELECTOR_TIMEOUT);

            // Start from the home screen
            DEVICE.pressHome();

            // Wait for launcher
            final String launcherPackage = Util.getLauncherPackageName();
            assertNotNull(launcherPackage);
            DEVICE.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), Util.LAUNCH_TIMEOUT);

            // Launch the app
            Context context = getApplicationContext();
            final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE_NAME);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
            context.startActivity(intent);

            // Wait for the app to appear
            DEVICE.wait(Until.hasObject(By.pkg(APP_PACKAGE_NAME).depth(0)), Util.LAUNCH_TIMEOUT);
            assertEquals(APP_PACKAGE_NAME, DEVICE.getCurrentPackageName());

            // Put device in portrait mode.
            DEVICE.setOrientationNatural();
        }

        /**
         * @return The selected text of learning lang spinner and the board size.
         */
        public static Pair<String, Integer> navigateToHomePage2FromMainActivity() throws UiObjectNotFoundException {
            // Select one choice from each spinner.
            searchForId("spinner_learning_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            final var learningLangSelectedText = selectRandomMenuItem();
            searchForId("spinner_native_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            selectRandomMenuItem();
            searchForId("spinner_grid_size").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            final var boardSize = parseBoardSize(selectRandomMenuItem());

            // Press next.
            searchForId("image_button_next").clickAndWaitForNewWindow();
            return new Pair<>(learningLangSelectedText, boardSize);
        }

        public static void navigateToGameActivityFromHomePage2() throws UiObjectNotFoundException {
            // Select one choice from each spinner.
            searchForId("spinner_lang_level").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            selectRandomMenuItem();
            searchForId("spinner_sudoku_level").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            selectRandomMenuItem();

            // Press next.
            searchForId("image_button_next").clickAndWaitForNewWindow();
        }
    }
}