package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.DEVICE;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.navigateToHomePage2FromMainActivity;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.putDeviceInLandscapeMode;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.scrollAndGetId;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.startAppInPortraitMode;
import static ca.sfu.cmpt276.sudokulang.Util.getResourceId;

import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {
    private static final String mAnchorId = "welcome_text";

    @Before
    public void startMainActivityFromHomeScreen() throws RemoteException {
        startAppInPortraitMode();
    }

    @Test
    public void testLearningLangSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_learning_lang", false, mAnchorId);
    }

    @Test
    public void testLearningLangSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_learning_lang", true, mAnchorId);
    }

    @Test
    public void testNativeLangSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_native_lang", false, mAnchorId);
    }

    @Test
    public void testNativeLangSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_native_lang", true, mAnchorId);
    }

    @Test
    public void testGridSizeSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_grid_size", false, mAnchorId);
    }

    @Test
    public void testGridSizeSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_grid_size", true, mAnchorId);
    }

    @Test
    public void testNavigateToNextPageInPortrait() throws UiObjectNotFoundException {
        navigateToHomePage2FromMainActivity();

        // Assert can move forward.
        assertFalse(DEVICE.hasObject(By.res(getResourceId(mAnchorId))));

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(DEVICE.hasObject(By.res(getResourceId(mAnchorId))));
    }

    @Test
    public void testNavigateToNextPageInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        navigateToHomePage2FromMainActivity();

        // Assert can move forward.
        assertFalse(scrollAndGetId(mAnchorId).exists());

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(scrollAndGetId(mAnchorId).exists());
    }
}