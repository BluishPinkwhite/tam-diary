package com.example.tamcalendar.data.emotion;

import androidx.room.Entity;

@Entity(primaryKeys = {"emotionID", "categoryID"})
public class EmotionCategoryCrossRef {
    public int emotionID;
    public int categoryID;
}
