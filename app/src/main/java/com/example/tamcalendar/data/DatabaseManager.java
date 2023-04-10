package com.example.tamcalendar.data;

import androidx.room.Room;

import com.example.tamcalendar.MainActivity;

import java.time.LocalDate;

public class DatabaseManager {

    public static TamDatabase createDatabase(MainActivity activity) {
        return Room.databaseBuilder(activity.getApplicationContext(), TamDatabase.class, "tam-calendar")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
    }

    public static int createDateSort(int year, int month, int day) {
        return year * 10_000 + month * 100 + day;
    }

    public static int createDateSort(LocalDate date) {
        return createDateSort(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static LocalDate fromDateSort(int dateSort) {
        int year = dateSort / 10_000;
        int month = (dateSort % 10_000) / 100;
        int day = (dateSort % 1000_000);
        return LocalDate.of(year, month, day);
    }
}
