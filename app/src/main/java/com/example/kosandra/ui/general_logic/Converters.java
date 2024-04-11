package com.example.kosandra.ui.general_logic;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

/**
 * The Converters class provides type conversion methods for various data types to be used with Room database TypeConverters.
 */
public class Converters {
    /**
     * Converts a Long value to a LocalDate object.
     *
     * @param value the Long value representing the number of days since the epoch
     * @return the corresponding LocalDate object, or null if the input value is null
     */
    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    /**
     * Converts a LocalDate object to a Long value.
     *
     * @param date the LocalDate object to be converted
     * @return the corresponding Long value representing the number of days since the epoch, or null if the input date is null
     */
    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return date == null ? null : date.toEpochDay();
    }

    /**
     * Converts a String value to a LocalTime object.
     *
     * @param value the String value representing the time in ISO-8601 format
     * @return the corresponding LocalTime object, or null if the input value is null
     */
    @TypeConverter
    public static LocalTime fromString(String value) {
        return value == null ? null : LocalTime.parse(value);
    }

    /**
     * Converts a LocalTime object to a String value.
     *
     * @param time the LocalTime object to be converted
     * @return the corresponding String value in ISO-8601 format, or null if the input time is null
     */
    @TypeConverter
    public static String localTimeToString(LocalTime time) {
        return time == null ? null : time.toString();
    }

    /**
     * Converts an array of Strings to a single String value.
     *
     * @param codeMaterial the array of Strings to be joined
     * @return the joined String with elements separated by commas, or null if the input array is null
     */
    @TypeConverter
    public static String fromCodeMaterial(String[] codeMaterial) {
        return codeMaterial == null ? null : TextUtils.join(",", codeMaterial);
    }

    /**
     * Converts a comma-separated String value to an array of Strings.
     *
     * @param codeMaterialString the comma-separated String to be split
     * @return the array of Strings obtained by splitting the input String, or null if the input String is null
     */
    @TypeConverter
    public static String[] toCodeMaterial(String codeMaterialString) {
        return codeMaterialString == null ? null : codeMaterialString.split(",");
    }

    /**
     * Converts an array of integers to a comma-separated String value.
     *
     * @param countMaterial the array of integers to be concatenated into a String
     * @return the concatenated String with integers separated by commas
     */
    @TypeConverter
    public static String fromCountMaterial(int[] countMaterial) {
        StringBuilder builder = new StringBuilder();
        for (int count : countMaterial) {
            builder.append(count).append(",");
        }
        return builder.toString();
    }

    /**
     * Converts a comma-separated String value to an array of integers.
     *
     * @param countMaterialString the comma-separated String to be split into integers
     * @return the array of integers obtained by parsing the input String, or null if the input String is null or empty
     */
    @TypeConverter
    public static int[] toCountMaterial(String countMaterialString) {
        if (countMaterialString == null || countMaterialString.isEmpty()) {
            return null;
        }
        String[] parts = countMaterialString.split(",");
        int[] counts = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            counts[i] = Integer.parseInt(parts[i]);
        }
        return counts;
    }
}
