package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.DEVICE;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.getId2NoScroll;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.getSpinnerText;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.navigateToHomePage2FromMainActivity;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.putDeviceInLandscapeMode;
import static ca.sfu.cmpt276.sudokulang.common.Util.TestHelper.searchForId;
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
public class MainActivityTest {
    private static final String anchorId = "welcome_text";

    @Before
    public void startMainActivityFromHomeScreen() throws RemoteException {
        startAppInPortraitMode();
    }

    @Test
    public void testLearningLangSpinnerInPortrait() throws UiObjectNotFoundException {
        CommonTests.testSpinner("spinner_learning_lang");
    }

    @Test
    public void testLearningLangSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testSpinner("spinner_learning_lang");
    }

    @Test
    public void testNativeLangSpinnerInPortrait() throws UiObjectNotFoundException {
        CommonTests.testSpinner("spinner_native_lang");
    }

    @Test
    public void testNativeLangSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testSpinner("spinner_native_lang");
    }

    @Test
    public void testGridSizeSpinnerInPortrait() throws UiObjectNotFoundException {
        CommonTests.testSpinner("spinner_grid_size");
    }

    @Test
    public void testGridSizeSpinnerInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        CommonTests.testSpinner("spinner_grid_size");
    }

    @Test
    public void testNavigateToNextPageInPortrait() throws UiObjectNotFoundException {
        final var learningLangSelectedText = navigateToHomePage2FromMainActivity().first;

        // Assert can move forward.
        assertFalse(DEVICE.hasObject(By.res(getResourceId(anchorId))));

        // Assert learning lang is passed to HomePage2.
        assertTrue(getId2NoScroll("textView_lang_level")
                .getText()
                .contains(learningLangSelectedText));

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(DEVICE.hasObject(By.res(getResourceId(anchorId))));

        // Assert text is still there.
        assertEquals(learningLangSelectedText, getSpinnerText(searchForId("spinner_learning_lang")));
    }

    @Test
    public void testNavigateToNextPageInLandscape() throws UiObjectNotFoundException, RemoteException {
        putDeviceInLandscapeMode();
        final var learningLangSelectedText = navigateToHomePage2FromMainActivity().first;

        // Assert can move forward.
        assertFalse(searchForId(anchorId).exists());

        // Assert learning lang is passed to HomePage2.
        assertTrue(searchForId("textView_lang_level")
                .getText()
                .contains(learningLangSelectedText));

        // Assert can get back.
        DEVICE.pressBack();
        assertTrue(searchForId(anchorId).exists());

        // Assert text is still there.
        assertEquals(learningLangSelectedText, getSpinnerText(searchForId("spinner_learning_lang")));
    }
}