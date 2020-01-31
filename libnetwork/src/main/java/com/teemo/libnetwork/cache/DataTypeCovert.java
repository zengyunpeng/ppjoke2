package com.teemo.libnetwork.cache;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;

public class DataTypeCovert {

    @TypeConverter
    public static Long data2Long(Date date) {
        return date.getTime();
    }

    @TypeConverter
    public static Long long2Data(Date date) {
        return date.getTime();
    }
}
