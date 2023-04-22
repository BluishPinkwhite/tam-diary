package com.example.tamcalendar.data.category;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.tamcalendar.data.value.E_Value;

import java.util.List;

public class FullCategory {
    @Embedded
    public E_Category category;

    @Relation(
            parentColumn = "categoryID",
            entityColumn = "F_Category"
    )
    public List<E_Value> values;
}
