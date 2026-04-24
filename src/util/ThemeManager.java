package util;

import java.awt.Color;
import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class ThemeManager {

    private static boolean darkMode = false;

    /* ===============================
       LIGHT THEME
    =============================== */
    public static final Color LIGHT_BG =
            new Color(245, 247, 250);

    public static final Color LIGHT_CARD =
            Color.WHITE;

    public static final Color LIGHT_TEXT =
            new Color(25, 25, 25);

    public static final Color LIGHT_SUBTEXT =
            new Color(90, 90, 90);

    public static final Color LIGHT_BORDER =
            new Color(210, 210, 210);

    public static final Color LIGHT_BUTTON =
            Color.WHITE;

    public static final Color LIGHT_TABLE_ROW =
            Color.WHITE;

    public static final Color LIGHT_TABLE_SELECTED =
            new Color(66, 133, 244);



    /* ===============================
       DARK THEME
    =============================== */
    public static final Color DARK_BG =
            new Color(24, 26, 27);

    public static final Color DARK_CARD =
            new Color(44, 47, 51);

    public static final Color DARK_TEXT =
            new Color(245, 245, 245);

    public static final Color DARK_SUBTEXT =
            new Color(180, 180, 180);

    public static final Color DARK_BORDER =
            new Color(95, 95, 95);

    public static final Color DARK_BUTTON =
            new Color(52, 58, 64);

    public static final Color DARK_TABLE_ROW =
            new Color(44, 47, 51);

    public static final Color DARK_TABLE_SELECTED =
            new Color(66, 133, 244);



    /* ===============================
       PRIMARY COLOR
    =============================== */
    public static final Color PRIMARY =
            new Color(66, 133, 244);



    /* ===============================
       FUNCTIONS
    =============================== */

    public static void toggleTheme() {
        darkMode = !darkMode;
    }

    public static void setDarkMode(boolean value) {
        darkMode = value;
    }

    public static boolean isDarkMode() {
        return darkMode;
    }

    public static String getThemeButtonText() {
        return darkMode ? "Light Mode" : "Dark Mode";
    }

    public static Color getBackgroundColor() {
        return darkMode ? DARK_BG : LIGHT_BG;
    }

    public static Color getCardColor() {
        return darkMode ? DARK_CARD : LIGHT_CARD;
    }

    public static Color getTextColor() {
        return darkMode ? DARK_TEXT : LIGHT_TEXT;
    }

    public static Color getSubTextColor() {
        return darkMode ? DARK_SUBTEXT : LIGHT_SUBTEXT;
    }

    public static Color getButtonColor() {
        return darkMode ? DARK_BUTTON : LIGHT_BUTTON;
    }

    public static Color getBorderColor() {
        return darkMode ? DARK_BORDER : LIGHT_BORDER;
    }

    public static Color getTableRowColor() {
        return darkMode ? DARK_TABLE_ROW : LIGHT_TABLE_ROW;
    }

    public static Color getTableSelectionColor() {
        return darkMode ? DARK_TABLE_SELECTED : LIGHT_TABLE_SELECTED;
    }

    public static Border getBorder() {
        return BorderFactory.createLineBorder(getBorderColor());
    }

    public static Color getSuccessColor() {
        return new Color(46, 204, 113);
    }

    public static Color getDangerColor() {
        return new Color(231, 76, 60);
    }

    public static Color getWarningColor() {
        return new Color(241, 196, 15);
    }
}