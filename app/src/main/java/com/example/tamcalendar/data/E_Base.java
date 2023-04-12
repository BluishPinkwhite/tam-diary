package com.example.tamcalendar.data;

public interface E_Base<TYPE> {

    <DAO extends DAO_Base<TYPE>> DAO getDAO();
    TYPE copy(TYPE other);

    default void insertToDB() {
        getDAO().insert((TYPE) this);
    }

    default void deleteFromDB() {
        getDAO().delete((TYPE) this);
    }

    default void updateToDB() {
        getDAO().update((TYPE) this);
    }
}
