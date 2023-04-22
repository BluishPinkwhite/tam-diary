package com.example.tamcalendar.data.emotion;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(primaryKeys = {"emotionID", "valueID"},
        tableName = "emotionCategoryCrossRef")
public class EmotionValueCrossRef {
    public long emotionID;
    public long valueID;

    public EmotionValueCrossRef() {
    }

    @Ignore
    public EmotionValueCrossRef(long emotionID, long valueID) {
        this.emotionID = emotionID;
        this.valueID = valueID;
    }
}
