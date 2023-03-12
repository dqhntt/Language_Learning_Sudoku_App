package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.DEVICE;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.navigateToGameActivityFromHomePage2;
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
public class HomePage2Test {
    private static final String mAnchorId = "textView_lang_level";

    @Before
    public void startHomePage2FromMainActivity() throws RemoteException, UiObjectNotFoundException {
        startAppInPortraitMode();
        navigateToHomePage2FromMainActivity();
    }

    @Test
    public void testLangLevelSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_lang_level", false, mAnchorId);
    }

    @Test
    public void testLangLevelSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_lang_level", true, mAnchorId);
    }

    @Test
    public void testSudokuLevelSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_sudoku_level", false, mAnchorId);
    }

    @Test
    public void testSudokuLevelSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_sudoku_level", true, mAnchorId);
    }

    @Test
    public void testNavigateToNextPageInPortrait() throws UiObjectNotFoundException {
        navigateToGameActivityFromHomePage2();

        // Assert can move forward.
        assertFalse(DEVICE.hasObject(By.res(getResourceId(mAnchorId))));

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(DEVICE.hasObject(By.res(getResourceId(mAnchorId))));
    }

    @Test
    public void testNavigateToNextPageInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        navigateToGameActivityFromHomePage2();

        // Assert can move forward.
        assertFalse(scrollAndGetId(mAnchorId).exists());

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(scrollAndGetId(mAnchorId).exists());
    }
}