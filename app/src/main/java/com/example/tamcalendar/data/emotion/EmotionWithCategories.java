package com.example.tamcalendar.data.emotion;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.tamcalendar.data.category.E_Category;

import java.util.List;

public class EmotionWithCategories {
    @Embedded
    public E_Emotion emotion;
    @Relation(
            parentColumn = "ID",
            entityColumn = "ID",
            associateBy = @Junction(
                    EmotionCategoryCrossRef.class
            )
    )
    public List<E_Category> categories;
}
