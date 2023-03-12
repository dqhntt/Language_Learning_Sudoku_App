package ca.sfu.cmpt276.sudokulang;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.Util.APP_PACKAGE_NAME;
import static ca.sfu.cmpt276.sudokulang.Util.SELECTOR_TIMEOUT;
import static ca.sfu.cmpt276.sudokulang.Util.getResourceId;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;

import androidx.annotation.NonNull;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Configurator;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.Random;

// See: https://stackoverflow.com/questions/40881680/whats-is-the-difference-between-uiobject-and-uiobject2-other-than-uiautomator-2
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final UiDevice device = UiDevice.getInstance(getInstrumentation());

    //----------------------------- Helper methods -------------------------------------------------

    private static void open(@NonNull UiObject spinner) throws UiObjectNotFoundException {
        if (spinner.exists()) {
            spinner.clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        } else {
            throw new UiObjectNotFoundException("Cannot open nonexistent spinner");
        }
    }

    @NonNull
    private static String getSpinnerText(@NonNull UiObject spinner) throws UiObjectNotFoundException {
        assertTrue(spinner.exists());
        return spinner.getChild(new UiSelector().className(android.widget.TextView.class)).getText();
    }

    @NonNull
    private static List<UiObject2> getUpdatedMenus() {
        return device.findObjects(By.clazz(android.widget.CheckedTextView.class));
    }

    private static void selectMenuItem(@NonNull UiObject2 item) {
        item.clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
    }

    private static void clickOutside() {
        device.click(0, 0);
        device.waitForWindowUpdate(APP_PACKAGE_NAME, SELECTOR_TIMEOUT);
    }

    private static void setLandscapeMode() throws RemoteException {
        if (new Random().nextBoolean()) {
            device.setOrientationRight();
        } else {
            device.setOrientationLeft();
        }
    }

    private static UiObject scrollAndGetView(UiSelector selector) throws UiObjectNotFoundException {
        new UiScrollable(new UiSelector().className(android.widget.ScrollView.class))
                .scrollIntoView(selector);
        return device.findObject(selector);
    }

    private static UiObject scrollAndGetId(String id) throws UiObjectNotFoundException {
        return scrollAndGetView(new UiSelector().resourceId(getResourceId(id)));
    }

    private static void testSpinner(String spinnerResId, boolean landscapeMode) throws UiObjectNotFoundException, RemoteException {
        if (landscapeMode) {
            setLandscapeMode();
        }

        // Assert cannot move on while no choice is selected.
        scrollAndGetId("image_button_next").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        assertTrue(scrollAndGetId("welcome_text").exists());

        // Find and open spinner.
        final var spinner = scrollAndGetId(spinnerResId);
        open(spinner);
        final var dropdown = device.findObject(
                new UiSelector().className(android.widget.ListView.class)
        );
        dropdown.waitForExists(SELECTOR_TIMEOUT);
        assertTrue(dropdown.isFocused());

        // Get list of dropdown menu items.
        var menus = getUpdatedMenus();
        assertTrue(menus.size() > 0);

        // Assert no choice selected.
        assertTrue(menus.get(0).isChecked());
        for (var menu : menus) {
            if (menus.indexOf(menu) != 0) {
                assertFalse(menu.isChecked());
            }
        }

        // Select first choice.
        final int selectedIndex = 1;
        final String selectedText = menus.get(selectedIndex).getText();
        selectMenuItem(menus.get(selectedIndex));

        // Assert spinner changed text.
        assertEquals(selectedText, getSpinnerText(spinner));

        // Assert item was selected in dropdown.
        open(spinner);
        menus = getUpdatedMenus();
        assertFalse(menus.get(0).isChecked());
        assertTrue(menus.get(selectedIndex).isChecked());

        // Close spinner.
        clickOutside();
        assertEquals(selectedText, getSpinnerText(spinner));
    }

    //--------------------------- Registered tests -------------------------------------------------

    // Cite: https://github.com/android/testing-samples/blob/main/ui/uiautomator/BasicSample/app/src/androidTest/java/com/example/android/testing/uiautomator/BasicSample/ChangeTextBehaviorTest.java
    @Before
    public void startMainActivityFromHomeScreen() throws RemoteException {
        assertNotNull(device);

        // Set timeout.
        Configurator.getInstance().setWaitForSelectorTimeout(SELECTOR_TIMEOUT);

        // Start from the home screen
        device.pressHome();

        // Wait for launcher
        final String launcherPackage = Util.getLauncherPackageName();
        assertNotNull(launcherPackage);
        device.wait(Until.hasObject(By.pkg(launcherPackage).depth(0)), Util.LAUNCH_TIMEOUT);

        // Launch the app
        Context context = getApplicationContext();
        final Intent intent = context.getPackageManager().getLaunchIntentForPackage(APP_PACKAGE_NAME);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);    // Clear out any previous instances
        context.startActivity(intent);

        // Wait for the app to appear
        device.wait(Until.hasObject(By.pkg(APP_PACKAGE_NAME).depth(0)), Util.LAUNCH_TIMEOUT);
        assertEquals(APP_PACKAGE_NAME, device.getCurrentPackageName());

        // Put device in portrait mode.
        device.setOrientationNatural();
    }

    @Test
    public void testLearningLangSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        testSpinner("spinner_learning_lang", false);
    }

    @Test
    public void testLearningLangSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        testSpinner("spinner_learning_lang", true);
    }

    @Test
    public void testNativeLangSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        testSpinner("spinner_native_lang", false);
    }

    @Test
    public void testNativeLangSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        testSpinner("spinner_native_lang", true);
    }

    @Test
    public void testGridSizeSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        testSpinner("spinner_grid_size", false);
    }

    @Test
    public void testGridSizeSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        testSpinner("spinner_grid_size", true);
    }

    @Test
    public void testNavigateToNextPageInPortrait() throws UiObjectNotFoundException {
        // Select one choice from each spinner.
        scrollAndGetId("spinner_learning_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        getUpdatedMenus().get(2).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
        scrollAndGetId("spinner_native_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        getUpdatedMenus().get(1).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
        scrollAndGetId("spinner_grid_size").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        getUpdatedMenus().get(4).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);

        // Assert can move forward.
        scrollAndGetId("image_button_next").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        assertFalse(scrollAndGetId("welcome_text").exists());

        // Assert can get back.
        device.pressBack();
        assertTrue(scrollAndGetId("welcome_text").exists());
    }

    @Test
    public void testNavigateToNextPageInLandscape() throws UiObjectNotFoundException, RemoteException {
        setLandscapeMode();

        // Select one choice from each spinner.
        scrollAndGetId("spinner_learning_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        getUpdatedMenus().get(1).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
        scrollAndGetId("spinner_native_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        getUpdatedMenus().get(2).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
        scrollAndGetId("spinner_grid_size").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        getUpdatedMenus().get(3).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);

        // Assert can move forward.
        scrollAndGetId("image_button_next").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        assertFalse(scrollAndGetId("welcome_text").exists());

        // Assert can get back.
        device.pressBack();
        assertTrue(scrollAndGetId("welcome_text").exists());
    }
}