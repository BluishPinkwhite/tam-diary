package com.example.tamcalendar.action;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Action;
import com.example.tamcalendar.views.OptionsDialog;

public class ActionOptionsDialog extends OptionsDialog<DAO_Action.FullActionData> {

    public ActionOptionsDialog(Context context, ArrayAdapter<DAO_Action.FullActionData> adapter, int position, Runnable invalidate, Runnable navigateToCreateFrag) {
        super(context, adapter, position, invalidate, navigateToCreateFrag);
    }

    protected void deleteItemDB(DAO_Action.FullActionData item) {
        MainActivity.database.daoAction().deleteByID(item.ID);
    }

    protected void prepareItemEdit(DAO_Action.FullActionData item) {
        ActionCreateFragment.actionToEdit = item;
        ActionCreateFragment.chosenActor = MainActivity.database.daoActor().getByName(item.actorName);
        ActionCreateFragment.chosenScale = MainActivity.database.daoScale().getByName(item.scaleName);
    }
}
