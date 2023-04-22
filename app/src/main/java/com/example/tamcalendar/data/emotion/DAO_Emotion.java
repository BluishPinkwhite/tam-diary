package com.example.tamcalendar.data.emotion;

import androidx.room.Dao;
import androidx.room.MapInfo;
import androidx.room.Query;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;
import java.util.Map;

@Dao
public interface DAO_Emotion extends DAO_Base<E_Emotion> {

    @Query("SELECT * FROM emotions " +
            " ORDER BY dateSort DESC, hour ASC")
    List<E_Emotion> list();


    @MapInfo(keyColumn = "dateSort")
    @Query("SELECT emotions.*, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM emotions " +
            "LEFT JOIN scale ON F_scale = scale.ID " +
            "WHERE dateSort BETWEEN :startDateSort AND :endDateSort " +
            "ORDER BY dateSort ASC, hour ASC")
    Map<Integer, List<FullEmotionData>> listBetween(int startDateSort, int endDateSort);


    @Query("SELECT emotions.*, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM emotions " +
            "LEFT JOIN scale ON F_scale = scale.ID " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY hour, scaleName")
    List<FullEmotionData> fullListFromDay(int dateSort);

    @Query("SELECT * FROM emotions " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY hour")
    List<E_Emotion> listFromDay(int dateSort);

    @Query("SELECT * FROM emotions " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Emotion get(int ID);

    @Query("DELETE FROM emotions " +
            "WHERE ID = :ID")
    void deleteByID(int ID);
}
