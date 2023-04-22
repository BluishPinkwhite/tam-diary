package com.example.tamcalendar.data.emotion;

import androidx.room.Entity;

@Entity(primaryKeys = {"emotionID", "valueID"},
        tableName = "emotionCategoryCrossRef")
public class EmotionCategoryCrossRef {
    public int emotionID;
    public int valueID;
}
