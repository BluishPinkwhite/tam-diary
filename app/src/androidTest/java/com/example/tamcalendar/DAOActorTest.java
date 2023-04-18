package com.example.tamcalendar;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.example.tamcalendar.data.DAO_Actor;
import com.example.tamcalendar.data.E_Actor;
import com.example.tamcalendar.database.TamDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class DAOActorTest {

    private DAO_Actor daoActor;
    private TamDatabase db;

    @Before
    public void setUp() {
        Context context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, TamDatabase.class).build();
        daoActor = db.daoActor();
    }

    @After
    public void tearDown() throws Exception {
        db.close();
    }

    @Test
    public void fullTest() {
        E_Actor actor = new E_Actor("Poleno", 0x334455);
        actor.ID = 1;
        daoActor.insert(actor);

        List<E_Actor> actors = daoActor.list();
        assert actors.size() == 1;

        assert actors.get(0).equals(actor);

        assert daoActor.get(1) != null;
        assert daoActor.get(1).equals(actor);

        assert daoActor.getByName(actor.name) != null;
        assert daoActor.getByName(actor.name).equals(actor);

        daoActor.delete(actor);

        assert daoActor.list().size() == 0;
    }
}