package com.example.tamcalendar.spinner;

import androidx.annotation.NonNull;

public abstract class ColorNameHaver {
    public int color;
    public String name;
    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
