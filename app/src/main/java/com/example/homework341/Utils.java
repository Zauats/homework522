package com.example.homework341;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;

public class Utils
{
    private static int sTheme;
    private static int sMargin;


    public final static int THEME_GREEN = 0;
    public final static int THEME_BLUE = 1;
    public final static int THEME_RED = 2;

    public final static int MARGIN1 = 0;
    public final static int MARGIN2 = 1;
    public final static int MARGIN3 = 2;

    /**
     * Set the theme of the Activity, and restart it by creating a new Activity of the same type.
     */
    public static void changeToTheme(Activity activity, int theme)
    {
        sTheme = theme;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    public static void changeToMargin(Activity activity, int margin)
    {
        sMargin = margin;
        activity.finish();

        activity.startActivity(new Intent(activity, activity.getClass()));
    }

    /** Set the theme of the activity, according to the configuration. */
    public static void onActivityCreateSetTheme(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case THEME_RED:
                activity.setTheme(R.style.AppThemeRed);
                break;
            case THEME_BLUE:
                activity.setTheme(R.style.AppThemeBlue);
                break;
            case THEME_GREEN:
                activity.setTheme(R.style.AppThemeGreen);
                break;
        }
    }

    public static void onActivityCreateSetMargin(Activity activity)
    {
        switch (sTheme)
        {
            default:
            case MARGIN1:
                activity.setTheme(R.style.Margin1);
                break;
            case MARGIN2:
                activity.setTheme(R.style.Margin2);
                break;
            case MARGIN3:
                activity.setTheme(R.style.Margin3);
                break;
        }
    }


}