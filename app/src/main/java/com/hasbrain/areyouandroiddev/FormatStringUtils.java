package com.hasbrain.areyouandroiddev;

import android.content.Context;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.util.HashMap;

/**
 * Created by thuyhien on 10/2/17.
 */

public class FormatStringUtils {
    public static final long MILLISECS_PER_YEAR = 1000L * 60 * 60 * 24 * 365;
    public static final long MILLISECS_PER_MONTH = 1000L * 60 * 60 * 24 * 30;
    public static final long MILLISECS_PER_DAY = 1000L * 60 * 60 * 24;
    public static final long MILLISECS_PER_HOUR = 1000L * 60 * 60;
    public static final String YEAR_TITLE = "year";
    public static final String MONTH_TITLE = "month";
    public static final String DAY_TITLE = "day";
    public static final String HOUR_TITLE = "hour";

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String author) {
        Spanned result;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            result = Html.fromHtml(author, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(author);
        }
        return result;
    }

    public static String getColoredSpanned(String text, int color) {
        String hexColor = String.format("#%06X", (0xFFFFFF & color));
        return "<font color='" + hexColor + "'>" + text + "</font>";
    }

    public static String formatAuthorTitle(
            String authorTitle, String author, String subReddit, int color) {
        author = getColoredSpanned(author, color);
        subReddit = getColoredSpanned(subReddit, color);
        return String.format(authorTitle, author, subReddit);
    }

    public static String getPostTime(long createdUTC, HashMap<String, String> timeTitleList) {
        long currentTime = System.currentTimeMillis();
        long betweenTime = currentTime - createdUTC * 1000L;
        int postTime = (int) (betweenTime / MILLISECS_PER_YEAR);
        if (postTime != 0) {
            return String.format(timeTitleList.get(YEAR_TITLE), postTime);
        }

        postTime = (int) (betweenTime / MILLISECS_PER_MONTH);
        if (postTime != 0) {
            return String.format(timeTitleList.get(MONTH_TITLE), postTime);
        }

        postTime = (int) (betweenTime / MILLISECS_PER_DAY);
        if (postTime != 0) {
            return String.format(timeTitleList.get(DAY_TITLE), postTime);
        }

        postTime = (int) (betweenTime / MILLISECS_PER_HOUR);
        if (postTime != 0) {
            return String.format(timeTitleList.get(HOUR_TITLE), postTime);
        }
        return "";
    }

    public static HashMap<String, String> createTimeTitleList(Context context) {
        HashMap<String, String> titleList = new HashMap<>();
        titleList.put(YEAR_TITLE,
                context.getResources().getString(R.string.title_created_years));
        titleList.put(MONTH_TITLE,
                context.getResources().getString(R.string.title_created_months));
        titleList.put(DAY_TITLE,
                context.getResources().getString(R.string.title_created_days));
        titleList.put(HOUR_TITLE,
                context.getResources().getString(R.string.title_created_hours));
        return titleList;
    }
}
