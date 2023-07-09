package com.sensegarden.sensegardenplaydev.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class DateTime {

    private String tag = "DATETIME";

    public String getCurrentDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CEST"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public String getCurrentDateTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //simpleDateFormat.setTimeZone(TimeZone.getTimeZone("CEST"));
        return simpleDateFormat.format(Calendar.getInstance().getTime());
    }

    public long getTimeDifference(String start1, String end1) {
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date start = simpleDateFormat.parse(start1);
            Date end = simpleDateFormat.parse(end1);

            Calendar cal = Calendar.getInstance(Locale.ENGLISH);

            cal.setTime(start);
            long startTime = cal.getTimeInMillis();
            cal.setTime(end);
            long endTime = cal.getTimeInMillis();

            TimeZone timezone = cal.getTimeZone();
            int offsetStart = timezone.getOffset(startTime);
            int offsetEnd = timezone.getOffset(endTime);
            int offset = offsetEnd - offsetStart;

            long difference = endTime - startTime + offset;

            return TimeUnit.MILLISECONDS.toHours(difference);
        } catch (ParseException e) {
            Log.d(tag, e + " error time");
            return 0;
        }
    }
}
