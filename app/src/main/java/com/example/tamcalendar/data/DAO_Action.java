package com.example.tamcalendar.data;

import androidx.room.Dao;
import androidx.room.MapInfo;
import androidx.room.Query;

import java.util.List;
import java.util.Map;

@Dao
public interface DAO_Action extends DAO_Base<E_Action> {

    @Query("SELECT * FROM actions " +
            " ORDER BY dateSort DESC")
    List<E_Action> list();

    @Query("SELECT * FROM actions " +
            "WHERE year = :year " +
            "ORDER BY day ASC")
    List<E_Action> listFromYear(int year);

    @Query("SELECT * FROM actions " +
            "WHERE year = :year AND month = :month " +
            "ORDER BY day ASC")
    List<E_Action> listFromMonth(int year, int month);

    @MapInfo(keyColumn = "dateSort")
    @Query("SELECT * FROM actions " +
            "WHERE dateSort BETWEEN :startDateSort AND :endDateSort " +
            "ORDER BY dateSort ASC")
    Map<Integer, List<E_Action>> listBetween(int startDateSort, int endDateSort);

    @Query("SELECT * FROM actions " +
            "INNER JOIN actor ON F_actor = actor.ID " +
            "INNER JOIN scale ON F_scale = scale.ID " +
            "WHERE year = :year AND month = :month AND day = :day")
    List<E_Action> listFromDay(int year, int month, int day);

    @Query("SELECT * FROM actions " +
            "WHERE dateSort = :dateSort")
    List<E_Action> listFromDay(int dateSort);



    @Query("SELECT * FROM actions " +
            "WHERE name LIKE :name LIMIT 1")
    E_Action getByName(String name);

    @Query("SELECT * FROM actions " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Action get(int ID);
}
