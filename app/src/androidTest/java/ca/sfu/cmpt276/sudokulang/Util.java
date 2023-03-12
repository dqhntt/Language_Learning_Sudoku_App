package ca.sfu.cmpt276.sudokulang;

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
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.Configurator;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import androidx.test.uiautomator.Until;

import java.util.List;
import java.util.Random;

class Util {
    public static final String APP_PACKAGE_NAME = "ca.sfu.cmpt276.sudokulang";
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
     *                    e.g. "@+id/welcome_text" -> "welcome_text"
     * @return Fully qualified name of {@code shortIdName}
     * for use with {@code By.res(...)} or {@code UiSelector.resourceId(...)}.
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

        public static void putDeviceInLandscapeMode() throws RemoteException {
            DEVICE.setOrientationNatural();
            if (new Random().nextBoolean()) {
                DEVICE.setOrientationRight();
            } else {
                DEVICE.setOrientationLeft();
            }
        }

        public static void open(@NonNull UiObject spinner) throws UiObjectNotFoundException {
            if (spinner.exists()) {
                spinner.clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
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

        @NonNull
        private static UiObject scrollAndGetView(UiSelector selector) throws UiObjectNotFoundException {
            new UiScrollable(new UiSelector().className(android.widget.ScrollView.class))
                    .scrollIntoView(selector);
            return DEVICE.findObject(selector);
        }

        @NonNull
        public static UiObject scrollAndGetId(String id) throws UiObjectNotFoundException {
            return scrollAndGetView(new UiSelector().resourceId(getResourceId(id)));
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

        public static void navigateToHomePage2FromMainActivity() throws UiObjectNotFoundException {
            // Select one choice from each spinner.
            scrollAndGetId("spinner_learning_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            getUpdatedMenus().get(2).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
            scrollAndGetId("spinner_native_lang").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            getUpdatedMenus().get(1).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
            scrollAndGetId("spinner_grid_size").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            getUpdatedMenus().get(3).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);

            // Press next.
            scrollAndGetId("image_button_next").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        }

        public static void navigateToGameActivityFromHomePage2() throws UiObjectNotFoundException {
            // Select one choice from each spinner.
            scrollAndGetId("spinner_lang_level").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            getUpdatedMenus().get(2).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
            scrollAndGetId("spinner_sudoku_level").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
            getUpdatedMenus().get(1).clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);

            // Press next.
            scrollAndGetId("image_button_next").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        }
    }
}