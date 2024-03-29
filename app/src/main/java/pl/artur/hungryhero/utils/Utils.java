package pl.artur.hungryhero.utils;

import java.util.Calendar;
import java.util.List;

import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Table;

public class Utils {

    public static String getOpeningHoursForDayOfWeek(OpeningHours openingHours, int dayOfWeek) {
        if (openingHours == null) {
            return "N/A";
        }

        switch (dayOfWeek) {
            case Calendar.MONDAY:
                return openingHours.getMonday();
            case Calendar.TUESDAY:
                return openingHours.getTuesday();
            case Calendar.WEDNESDAY:
                return openingHours.getWednesday();
            case Calendar.THURSDAY:
                return openingHours.getThursday();
            case Calendar.FRIDAY:
                return openingHours.getFriday();
            case Calendar.SATURDAY:
                return openingHours.getSaturday();
            case Calendar.SUNDAY:
                return openingHours.getSunday();
            default:
                return "Zamknięte";
        }
    }

    public static int getMaxCapacity(List<Table> tables) {
        int maxCapacity = 0;

        if (tables != null) {
            for (Table table : tables) {
                int capacity = table.getCapacity();
                if (capacity > maxCapacity) {
                    maxCapacity = capacity;
                }
            }
        }

        return maxCapacity;
    }

    public static long getTodayTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0); // Ustawienie godziny na 0
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTimeInMillis();
    }

}
