package com.example.tamcalendar.data.emotion;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.example.tamcalendar.data.value.E_Value;

import java.util.List;

public class EmotionWithCategories {
    @Embedded
    public FullEmotionData emotion;
    @Relation(
            parentColumn = "emotionID",
            entityColumn = "valueID",
            associateBy = @Junction(
                    value = EmotionValueCrossRef.class,
                    parentColumn = "emotionID",
                    entityColumn = "valueID"
            )
    )
    public List<E_Value> values;
}
