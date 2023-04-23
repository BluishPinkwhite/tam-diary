package com.example.tamcalendar.data.emotion;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.MapInfo;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;
import java.util.Map;

@Dao
public interface DAO_Emotion extends DAO_Base<E_Emotion> {

    @Query("SELECT * FROM emotions " +
            " ORDER BY dateSort DESC, hour ASC")
    List<E_Emotion> list();

    @Query("SELECT * FROM emotions " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY hour")
    List<E_Emotion> listFromDay(int dateSort);


    @MapInfo(keyColumn = "dateSort")
    @Query("SELECT emotions.*, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM emotions " +
            "LEFT JOIN scale ON F_scale = scale.scaleID " +
            "WHERE dateSort BETWEEN :startDateSort AND :endDateSort " +
            "ORDER BY dateSort ASC, hour ASC")
    Map<Integer, List<FullEmotionData>> listBetween(int startDateSort, int endDateSort);

    @Transaction
    @MapInfo(keyColumn = "dateSort")
    @Query("SELECT emotions.*, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM emotions " +
            "LEFT JOIN scale ON F_scale = scale.scaleID " +
            "WHERE dateSort BETWEEN :startDateSort AND :endDateSort " +
            "ORDER BY dateSort, hour ASC, scaleName ASC")
    Map<Integer, List<EmotionWithCategories>> listWithCategoriesBetween(int startDateSort, int endDateSort);


    @Query("SELECT emotions.*, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM emotions " +
            "LEFT JOIN scale ON F_scale = scale.scaleID " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY hour, scaleName")
    List<FullEmotionData> fullListFromDay(int dateSort);

    @Transaction
    @Query("SELECT emotions.*, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM emotions " +
            "LEFT JOIN scale ON F_scale = scale.scaleID " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY hour ASC, scaleName ASC")
    List<EmotionWithCategories> fullCategoryListFromDay(int dateSort);

    @Query("SELECT * FROM emotions " +
            "WHERE emotionID LIKE :ID LIMIT 1")
    E_Emotion get(long ID);

    @Query("DELETE FROM emotions " +
            "WHERE emotionID = :ID")
    void deleteByID(long ID);


    ////////////////

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertEmotionCategoryRef(EmotionValueCrossRef emotionCategoryCrossRef);

    @Query("SELECT * FROM emotionCategoryCrossRef " +
            "WHERE emotionID = :emotionID")
    List<EmotionValueCrossRef> listEmotionCategoryRefByEmotionID(long emotionID);

    @Query("DELETE FROM emotionCategoryCrossRef " +
            "WHERE emotionID = :emotionID")
    void deleteAllEmotionCategoryRefsByEmotionID(long emotionID);

    @Delete
    void deleteEmotionCategoryRef(EmotionValueCrossRef emotionCategoryCrossRef);
}
