package com.example.tamcalendar.database;

import androidx.room.Room;

import com.example.tamcalendar.MainActivity;

import java.time.LocalDate;

public class DatabaseManager {

    public static final String DATABASE_NAME = "tam-calendar";
    public static final String DATABASE_NAME_FULL = DATABASE_NAME + "";

    public static TamDatabase createDatabase(MainActivity activity) {
        return Room.databaseBuilder(activity.getApplicationContext(), TamDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries()
                .addMigrations(TamDatabase.MIGRATION_3_4)
                .addMigrations(TamDatabase.MIGRATION_4_5)
                //.fallbackToDestructiveMigration()
                .build();
    }

    public static int createDateSort(int year, int month, int day) {
        return year * 10_000 + month * 100 + day;
    }

    public static int createDateSort(LocalDate date) {
        return createDateSort(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }


    public static int dateSortToday() {
        LocalDate date = LocalDate.now();
        return createDateSort(date.getYear(), date.getMonthValue(), date.getDayOfMonth());
    }

    public static LocalDate fromDateSort(int dateSort) {
        int year = dateSort / 10_000;
        dateSort %= 10_000;

        int month = dateSort / 100;
        dateSort %= 100;

        int day = dateSort;
        return LocalDate.of(year, month, day);
    }
}
