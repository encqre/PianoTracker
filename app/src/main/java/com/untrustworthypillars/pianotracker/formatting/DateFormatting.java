package com.untrustworthypillars.pianotracker.formatting;

import android.text.format.DateFormat;

import java.util.Date;

public class DateFormatting {

    public static String dateToMonthNameAndDay(Date date) {
        return DateFormat.format("MMM d", date).toString();
    }

}
