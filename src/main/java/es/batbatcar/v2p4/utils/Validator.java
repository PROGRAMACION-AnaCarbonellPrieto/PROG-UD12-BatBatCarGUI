package es.batbatcar.v2p4.utils;

import java.time.LocalDate;
import java.time.LocalTime;
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
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }
    
    public static boolean isValidNumber(int num) {
    	if (num > 0 && num <= 6) return true;
    	return false;
    }
    
    public static boolean isValidNumber(long num) {
    	if (num > 0) return true;
    	return false;
    }
    
    public static boolean isValidNumber(float num) {
    	if (num > 0) return true;
    	return false;
    }
    
    public static boolean isValidText(String text, char separator) {
    	return text.matches("^[A-Z][a-z]+(" + separator + "[A-Z][a-z]+)+");
    }
}

