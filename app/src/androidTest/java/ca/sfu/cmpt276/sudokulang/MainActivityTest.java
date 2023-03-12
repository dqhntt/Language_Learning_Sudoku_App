package ca.sfu.cmpt276.sudokulang;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.Util.APP_PACKAGE_NAME;
import static ca.sfu.cmpt276.sudokulang.Util.SELECTOR_TIMEOUT;

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
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

// See: https://stackoverflow.com/questions/40881680/whats-is-the-difference-between-uiobject-and-uiobject2-other-than-uiautomator-2
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final UiDevice device = UiDevice.getInstance(getInstrumentation());

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
    public void testLearningLangSpinner() throws UiObjectNotFoundException {
        // Open it.
        final var spinner = device.findObject(
                new UiSelector().resourceId("ca.sfu.cmpt276.sudokulang:id/spinner_learning_lang")
        );
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
}