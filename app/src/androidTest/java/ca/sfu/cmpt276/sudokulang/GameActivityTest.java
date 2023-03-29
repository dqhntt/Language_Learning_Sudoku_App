package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertFalse;
import static ca.sfu.cmpt276.sudokulang.common.Util.SELECTOR_TIMEOUT;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.getId2NoScroll;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.navigateToGameActivityFromHomePage2;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.navigateToHomePage2FromMainActivity;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.putDeviceInLandscapeMode;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.searchForId;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.startAppInPortraitMode;

import android.os.RemoteException;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.Until;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import ca.sfu.cmpt276.sudokulang.common.CommonTests;

@RunWith(AndroidJUnit4.class)
public class GameActivityTest {
    private static int boardSize;

    @Before
    public void startGameActivityFromHomeScreen() throws UiObjectNotFoundException, RemoteException {
        startAppInPortraitMode();
        boardSize = navigateToHomePage2FromMainActivity().second;
        navigateToGameActivityFromHomePage2();
    }

    @Test
    public void testKeypadAndCellViewInPortrait() throws UiObjectNotFoundException {
        CommonTests.testKeypadAndCellView(boardSize);
    }

    @Test
    public void testKeypadAndCellViewInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testKeypadAndCellView(boardSize);
    }

    @Test
    public void testGameBoardAndCellViewInPortrait() throws UiObjectNotFoundException {
        CommonTests.testGameBoardAndCellView(boardSize);
    }

    @Test
    public void testGameBoardAndCellViewInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testGameBoardAndCellView(boardSize);
    }

    @Test
    public void testCombinedInPortrait() throws UiObjectNotFoundException {
        CommonTests.testKeypadBoardEraseViewCombined(boardSize);
    }

    @Test
    public void testCombinedInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testKeypadBoardEraseViewCombined(boardSize);
    }

    @Test
    public void testRetainStateOnRotationToLandscape() throws UiObjectNotFoundException, RemoteException {
        CommonTests.testRetainStateOnRotation(boardSize);
    }

    @Test
    public void testRetainStateOnRotationToPortrait() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testRetainStateOnRotation(boardSize);
    }

    @Test
    public void testNavigateHomeButton() throws UiObjectNotFoundException {
        // Assert pressing home button exits the game.
        getId2NoScroll("main_activity").clickAndWait(Until.newWindow(), SELECTOR_TIMEOUT);
        assertFalse(searchForId("erase_button").exists());
    }
}