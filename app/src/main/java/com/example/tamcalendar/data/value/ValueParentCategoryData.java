package com.example.tamcalendar.data.value;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.tamcalendar.data.category.E_Category;

public class ValueParentCategoryData {
    @Embedded
    public E_Value value;
    @Relation(
            parentColumn = "F_Category",
            entityColumn = "categoryID"
    )
    public E_Category parentCategory;
}
