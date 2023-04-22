package com.example.tamcalendar.data.emotion;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Base;
import com.example.tamcalendar.data.E_Base;

@Entity(tableName = "emotions")
public class E_Emotion implements E_Base<E_Emotion> {
    @PrimaryKey(autoGenerate = true)
    public long emotionID;

    public String description;

    public int dateSort;
    public int hour;

    public long F_scale;

    public E_Emotion() {
    }

    @Ignore
    public E_Emotion(String description, int hour, int dateSort, long F_scale) {
        this.description = description;
        this.hour = hour;
        this.dateSort = dateSort;
        this.F_scale = F_scale;
    }

    @Override
    public <DAO extends DAO_Base<E_Emotion>> DAO getDAO() {
        return (DAO) MainActivity.database.daoEmotion();
    }

    @Override
    public E_Emotion copy(E_Emotion other) {
        this.description = other.description;
        this.hour = other.hour;
        this.dateSort = other.dateSort;
        F_scale = other.F_scale;
        return this;
    }
}
