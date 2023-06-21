package org.Hamm.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {
    /**
     * Validates an email address.
     *
     * @param email the email address to validate
     * @return true if the email address is valid, false otherwise
     */
    public static boolean validateEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    /**
     * Validates a tuition code.
     *
     * @param tuition the tuition code to validate
     * @return true if the tuition code is valid, false otherwise
     */
    public static boolean validateTuition(String tuition) {
        String regex = "^[0-9]{4}[A-Z]{3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tuition);
        return matcher.matches();
    }

    /**
     * Validates an old tuition code.
     *
     * @param tuition the old tuition code to validate
     * @return true if the old tuition code is valid, false otherwise
     */
    public static boolean validateOldTuition(String tuition) {
        String regex = "^[A-Z]{2}[0-9]{5}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(tuition);
        return matcher.matches();
    }

    /**
     * Validates a phone number.
     *
     * @param phone the phone number to validate
     * @return true if the phone number is valid, false otherwise
     */
    public static boolean validatePhone(String phone) {
        String regex = "^[0-9]{9}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }


}

