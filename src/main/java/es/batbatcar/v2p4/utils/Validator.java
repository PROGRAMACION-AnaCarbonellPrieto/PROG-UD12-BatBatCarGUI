package es.batbatcar.v2p4.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class Validator {

	// Incluye aquí el resto de métodos de validación que necesites
	
    public static boolean isValidDateTime(String dateTime) {
        try {
            LocalDate.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
    public static boolean isValidDate(String date) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidTime(String time) {
        try {
            LocalDate.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

}

