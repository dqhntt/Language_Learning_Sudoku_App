package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.DEVICE;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.navigateToGameActivityFromHomePage2;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.navigateToHomePage2FromMainActivity;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.putDeviceInLandscapeMode;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.scrollAndGetId;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.startAppInPortraitMode;
import static ca.sfu.cmpt276.sudokulang.common.Util.getResourceId;

import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.sfu.cmpt276.sudokulang.common.CommonTests;

@RunWith(AndroidJUnit4.class)
public class HomePage2Test {
    private static final String anchorId = "textView_Sudoku_Level";
    private static String learningLangSelectedText;

    @Before
    public void startHomePage2FromMainActivity() throws RemoteException, UiObjectNotFoundException {
        startAppInPortraitMode();
        learningLangSelectedText = navigateToHomePage2FromMainActivity();
    }

    @Test
    public void testLangLevelSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_lang_level", false, anchorId);
    }

    @Test
    public void testLangLevelSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_lang_level", true, anchorId);
    }

    @Test
    public void testSudokuLevelSpinnerInPortrait() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_sudoku_level", false, anchorId);
    }

    @Test
    public void testSudokuLevelSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testSpinner("spinner_sudoku_level", true, anchorId);
    }

    @Test
    public void testNavigateToNextPageInPortrait() throws UiObjectNotFoundException {
        navigateToGameActivityFromHomePage2();

        // Assert can move forward.
        assertFalse(DEVICE.hasObject(By.res(getResourceId(anchorId))));

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(DEVICE.hasObject(By.res(getResourceId(anchorId))));

        // Assert text is still there.
        assertTrue(DEVICE.findObject(By.res(getResourceId("textView_lang_level")))
                .getText()
                .contains(learningLangSelectedText));
    }

    @Test
    public void testNavigateToNextPageInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        navigateToGameActivityFromHomePage2();

        // Assert can move forward.
        assertFalse(scrollAndGetId(anchorId).exists());

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(scrollAndGetId(anchorId).exists());

        // Assert text is still there.
        assertTrue(scrollAndGetId(getResourceId("textView_lang_level"))
                .getText()
                .contains(learningLangSelectedText));
    }
}