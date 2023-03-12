package ca.sfu.cmpt276.sudokulang;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;

public class Util {
    public static final String APP_PACKAGE_NAME = "ca.sfu.cmpt276.sudokulang";
    public static final int LAUNCH_TIMEOUT = 5000;
    public static final int SELECTOR_TIMEOUT = 500;

    /**
     * Uses package manager to find the package name of the device launcher. Usually this package
     * is "com.android.launcher" but can be different at times. This is a generic solution which
     * works on all platforms.
     *
     * @cite <a href="https://github.com/android/testing-samples/blob/main/ui/uiautomator/BasicSample/app/src/androidTest/java/com/example/android/testing/uiautomator/BasicSample/ChangeTextBehaviorTest.java">Sample</a>
     */
    public static String getLauncherPackageName() {
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
}