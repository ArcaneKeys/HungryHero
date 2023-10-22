package pl.artur.hungryhero.utils;

import java.util.Calendar;
import java.util.List;

import pl.artur.hungryhero.models.OpeningHours;
import pl.artur.hungryhero.models.Table;

public class Utils {

    public static String getOpeningHoursForDayOfWeek(OpeningHours openingHours, int dayOfWeek) {
        if (openingHours == null) {
            return "N/A"; // Jeśli brak danych o godzinach otwarcia, zwróć "N/A" (Not Available)
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
                return "Zamknięte"; // Domyślna wartość w przypadku braku danych dla danego dnia tygodnia
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

}
