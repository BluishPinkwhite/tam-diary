package com.example.tamcalendar.spinner;

import androidx.annotation.NonNull;

import com.example.tamcalendar.data.E_Base;

public abstract class ColorNameHaver<T> implements E_Base<T> {
    public int color;
    public String name;

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
