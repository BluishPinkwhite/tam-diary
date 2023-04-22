package com.example.tamcalendar.action;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.action.FullActionData;
import com.example.tamcalendar.views.OptionsDialog;

public class ActionOptionsDialog extends OptionsDialog<FullActionData> {

    public ActionOptionsDialog(Context context, ArrayAdapter<FullActionData> adapter, int position, Runnable invalidate, Runnable navigateToCreateFrag) {
        super(context, adapter, position, invalidate, navigateToCreateFrag);
    }

    protected void deleteItemDB(FullActionData item) {
        MainActivity.database.daoAction().deleteByID(item.actionID);
    }

    protected void prepareItemEdit(FullActionData item) {
        ActionCreateFragment.actionToEdit = item;
        ActionCreateFragment.chosenActor = MainActivity.database.daoActor().getByName(item.actorName);
        ActionCreateFragment.chosenScale = MainActivity.database.daoScale().getByName(item.scaleName);
    }
}
