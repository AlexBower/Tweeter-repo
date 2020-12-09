package edu.byu.cs.tweeter.model.service;

import android.annotation.SuppressLint;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@SuppressLint("NewApi")
public class TimeFormatter {

    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd yyyy  hh:mm:ss a");


    public static String format(LocalDateTime localDateTime) {
        return localDateTime.format(formatter);
    }

    public static LocalDateTime getFromString(String string) {
        return LocalDateTime.parse(string, formatter);
    }
}