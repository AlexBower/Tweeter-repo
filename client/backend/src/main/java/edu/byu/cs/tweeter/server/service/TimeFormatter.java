package edu.byu.cs.tweeter.server.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeFormatter {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy  hh:mm a");

    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }

    public LocalDateTime getFromString(String string) {
        return LocalDateTime.parse(string, formatter);
    }
}
