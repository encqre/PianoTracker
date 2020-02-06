package com.untrustworthypillars.pianotracker.formatting;

import android.text.format.DateFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class DateFormatting {

    public static String dateToMonthNameAndDay(Date date) {
        return DateFormat.format("MMM d", date).toString();
    }

    public static String dateDeltaToNow(Date date) {
        String deltaString = "";

        Instant instant = Instant.ofEpochMilli(date.getTime());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        LocalDate localdate = localDateTime.toLocalDate();
        LocalDate now = LocalDate.now();

        long dayDiff = ChronoUnit.DAYS.between(localdate, now);

        if (dayDiff == 0) {
            deltaString = "Today (" + DateFormat.format("MMM d", date).toString() + ")";
        } else {
            deltaString = dayDiff + " days ago (" + DateFormat.format("MMM d", date).toString() + ")";
        }

        return deltaString;
    }

}
