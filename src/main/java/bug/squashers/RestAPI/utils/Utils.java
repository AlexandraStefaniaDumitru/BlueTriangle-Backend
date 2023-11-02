package bug.squashers.RestAPI.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    private static final DateTimeFormatter clientDateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter serverDateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    public static String getFormattedDate(String date) {
        LocalDate date1 = LocalDate.parse(date, clientDateFormat);
        return date1.format(serverDateFormat);
    }
}
