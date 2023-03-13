package ca.sfu.cmpt276.sudokulang;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static ca.sfu.cmpt276.sudokulang.Util.APP_PACKAGE_NAME;
import static ca.sfu.cmpt276.sudokulang.Util.SELECTOR_TIMEOUT;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.DEVICE;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.getSpinnerText;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.getUpdatedMenus;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.open;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.putDeviceInLandscapeMode;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.rotateDevice;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.scrollAndGetId;
import static ca.sfu.cmpt276.sudokulang.Util.TestHelper.selectMenuItem;

import android.os.RemoteException;

import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

class CommonTests {
    private static void clickOutside() {
        DEVICE.click(0, 0);
        DEVICE.waitForWindowUpdate(APP_PACKAGE_NAME, SELECTOR_TIMEOUT);
    }

    /**
     * @param anchorId ID of the view to check whether we are still on the same page.
     * @implNote Sharable between {@link MainActivityTest} and {@link HomePage2Test} only.
     */
    public static void testSpinner(String spinnerResId, boolean landscapeMode, String anchorId) throws UiObjectNotFoundException, RemoteException {
        if (landscapeMode) {
            putDeviceInLandscapeMode();
        }

        // Assert cannot move forward while no choice is selected.
        scrollAndGetId("image_button_next").clickAndWaitForNewWindow(SELECTOR_TIMEOUT);
        assertTrue(scrollAndGetId(anchorId).exists());

        // Find and open spinner.
        final var spinner = scrollAndGetId(spinnerResId);
        open(spinner);
        final var dropdown = DEVICE.findObject(
                new UiSelector().className(android.widget.ListView.class)
        );
        dropdown.waitForExists(SELECTOR_TIMEOUT);
        assertTrue(dropdown.isFocusable());
        assertTrue(dropdown.isEnabled());

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

        // Rotate and assert text is still there.
        rotateDevice();
        assertEquals(selectedText, getSpinnerText(scrollAndGetId(spinnerResId)));
    }
}