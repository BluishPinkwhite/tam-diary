package com.example.tamcalendar.spinner;

import androidx.annotation.NonNull;

public class ColorNameable {
    public int color = 0;
    public String name = "[text]";

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
