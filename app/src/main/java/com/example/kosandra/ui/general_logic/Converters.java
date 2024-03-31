package com.example.kosandra.ui.general_logic;

import android.text.TextUtils;

import androidx.room.TypeConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class Converters {
    @TypeConverter
    public static LocalDate fromTimestamp(Long value) {
        return value == null ? null : LocalDate.ofEpochDay(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(LocalDate date) {
        return date == null ? null : date.toEpochDay();
    }

    @TypeConverter
    public static LocalTime fromString(String value) {
        return value == null ? null : LocalTime.parse(value);
    }

    @TypeConverter
    public static String localTimeToString(LocalTime time) {
        return time == null ? null : time.toString();
    }

    @TypeConverter
    public static String fromCodeMaterial(String[] codeMaterial) {
        return codeMaterial == null ? null :  TextUtils.join(",", codeMaterial);
    }

    @TypeConverter
    public static String[] toCodeMaterial(String codeMaterialString) {
        return codeMaterialString == null ? null : codeMaterialString.split(",");
    }

    @TypeConverter
    public static String fromCountMaterial(int[] countMaterial) {
        StringBuilder builder = new StringBuilder();
        for (int count : countMaterial) {
            builder.append(count).append(",");
        }
        return builder.toString();
    }

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
