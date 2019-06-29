package com.example.hustbillbook.tools;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CalendarUtils {

    @org.jetbrains.annotations.NotNull
    public static String getCurrentDate() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return (year + "-" + month + "-" + day);
    }

    public static List<String> getWeekDays() {
        Calendar calendar = Calendar.getInstance();
        int firstDayOfWeek = calendar.getFirstDayOfWeek();
        calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek);

        List<String> result = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, firstDayOfWeek + i);
            result.add(calendar.get(Calendar.YEAR) + "-" +
                    (calendar.get(Calendar.MONTH) + 1) + "-" +
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        return result;
    }

    public static List<String> getMonthDays() {
        Calendar calendar = Calendar.getInstance();
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

        List<String> result = new ArrayList<>();
        for (int i = 1; i <= daysInMonth; i++) {
            calendar.set(Calendar.DAY_OF_MONTH, i);
            result.add(calendar.get(Calendar.YEAR) + "-" +
                    (calendar.get(Calendar.MONTH) + 1) + "-" +
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        return result;
    }

    public static List<String> getYearDays() {
        Calendar calendar = Calendar.getInstance();
        int daysInYear = calendar.getActualMaximum(Calendar.DAY_OF_YEAR);

        List<String> result = new ArrayList<>();
        for (int i = 1; i <= daysInYear; i++) {
            calendar.set(Calendar.DAY_OF_YEAR, i);
            result.add(calendar.get(Calendar.YEAR) + "-" +
                    (calendar.get(Calendar.MONTH) + 1) + "-" +
                    calendar.get(Calendar.DAY_OF_MONTH));
        }
        return result;
    }
}
