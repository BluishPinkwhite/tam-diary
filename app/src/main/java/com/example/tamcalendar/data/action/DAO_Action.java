package com.example.tamcalendar.data.action;

import androidx.room.Dao;
import androidx.room.MapInfo;
import androidx.room.Query;

import com.example.tamcalendar.data.DAO_Base;

import java.util.List;
import java.util.Map;

@Dao
public interface DAO_Action extends DAO_Base<E_Action> {

    @Query("SELECT * FROM actions " +
            " ORDER BY dateSort DESC")
    List<E_Action> list();

    @Query("SELECT * FROM actions " +
            "WHERE year = :year " +
            "ORDER BY dateSort ASC")
    List<E_Action> listFromYear(int year);

    @Query("SELECT * FROM actions " +
            "WHERE year = :year AND month = :month " +
            "ORDER BY dateSort ASC")
    List<E_Action> listFromMonth(int year, int month);

    @MapInfo(keyColumn = "dateSort")
    @Query("SELECT actions.*, actor.name AS actorName, actor.color AS actorColor, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM actions " +
            "LEFT JOIN actor ON F_actor = actor.ID " +
            "LEFT JOIN scale ON F_scale = scale.ID " +
            "WHERE dateSort BETWEEN :startDateSort AND :endDateSort " +
            "ORDER BY dateSort ASC, name ASC")
    Map<Integer, List<FullActionData>> listBetween(int startDateSort, int endDateSort);

    @Query("SELECT actions.*, actor.name AS actorName, actor.color AS actorColor, scale.color AS scaleColor, scale.name AS scaleName " +
            "FROM actions " +
            "LEFT JOIN actor ON F_actor = actor.ID " +
            "LEFT JOIN scale ON F_scale = scale.ID " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY name")
    List<FullActionData> fullListFromDay(int dateSort);

    @Query("SELECT * FROM actions " +
            "WHERE dateSort = :dateSort " +
            "ORDER BY name")
    List<E_Action> listFromDay(int dateSort);


    @Query("SELECT * FROM actions " +
            "WHERE name LIKE :name LIMIT 1")
    E_Action getByName(String name);

    @Query("SELECT * FROM actions " +
            "WHERE ID LIKE :ID LIMIT 1")
    E_Action get(int ID);

    @Query("DELETE FROM actions " +
            "WHERE ID = :ID")
    void deleteByID(int ID);
}