package com.walid.knowledgeworld.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME_MODE = "theme_mode";

    public static final int THEME_MODE_SYSTEM = 0;
    public static final int THEME_MODE_LIGHT = 1;
    public static final int THEME_MODE_DARK = 2;

    private static SharedPreferences getPrefs(Context context) {
        return context.getApplicationContext().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static int getSavedThemeMode(Context context) {
        return getPrefs(context).getInt(KEY_THEME_MODE, THEME_MODE_SYSTEM);
    }

    public static void saveThemeMode(Context context, int mode) {
        getPrefs(context).edit().putInt(KEY_THEME_MODE, mode).apply();
    }

    public static void applyTheme(int mode) {
        switch (mode) {
            case THEME_MODE_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_MODE_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_MODE_SYSTEM:
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
        }
    }

    public static void init(Context context) {
        int savedMode = getSavedThemeMode(context);
        applyTheme(savedMode);
    }

    public static void showThemeSelectionDialog(Activity activity, Runnable onThemeChanged) {
        int currentMode = getSavedThemeMode(activity);
        String[] options = new String[]{
                "حسب إعدادات النظام",
                "الوضع النهاري (Light Mode)",
                "الوضع الليلي (Dark Mode)"
        };

        new AlertDialog.Builder(activity)
                .setTitle("اختر مظهر التطبيق 🎨")
                .setSingleChoiceItems(options, currentMode, (dialog, which) -> {
                    if (which != currentMode) {
                        saveThemeMode(activity, which);
                        applyTheme(which);
                        if (onThemeChanged != null) {
                            onThemeChanged.run();
                        } else {
                            activity.recreate();
                        }
                    }
                    dialog.dismiss();
                })
                .setNegativeButton("إلغاء", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
