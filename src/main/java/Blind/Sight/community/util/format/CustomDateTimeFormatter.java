package Blind.Sight.community.util.format;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    private CustomDateTimeFormatter() {
    }

    public static LocalDate dateOfBirthFormatter(String dateString) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        return LocalDate.parse(dateString, formatter);
    }

    public static String getLocalDate() {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return today.format(formatter);
    }

    public static LocalDate getTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm:ss");
        return LocalDate.parse(time, formatter);
    }
}
