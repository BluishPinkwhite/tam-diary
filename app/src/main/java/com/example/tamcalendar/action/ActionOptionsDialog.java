package com.example.tamcalendar.action;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Action;

public class ActionOptionsDialog {

    private AlertDialog dialog;
    private ActionArrayAdapter adapter;
    private int position;
    private Runnable navigateToActionCreateFrag;

    public ActionOptionsDialog(Context context, ActionArrayAdapter adapter, int position, Runnable invalidate,
                               Runnable navigateToActionCreateFrag) {
        this.adapter = adapter;
        this.position = position;
        this.navigateToActionCreateFrag = navigateToActionCreateFrag;

        init(context, invalidate);
    }

    private void init(Context context, Runnable invalidate) {
        dialog = new AlertDialog.Builder(context)
                .setTitle(R.string.options)
                .setCancelable(true)
                .setItems(R.array.selectItemOptions, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0: // Edit
                                prepareItemEdit(adapter.getItem(position));
                                invalidate.run();
                                break;
                            case 1: // Delete
                                deleteItemDB(adapter.getItem(position));
                                invalidate.run();
                                break;
                            default:
                                System.err.println(getClass().getName() +
                                        " showItemOptionMenu unknown button! (got " + which + ")");
                        }
                    }
                })
                .create();

        dialog.show();
    }

    private void deleteItemDB(DAO_Action.FullActionData item) {
        MainActivity.database.daoAction().deleteByID(item.ID);
        adapter.notifyDataSetChanged();
    }

    private void prepareItemEdit(DAO_Action.FullActionData item) {
        ActionCreateFragment.actionToEdit = item;
        ActionCreateFragment.chosenActor = MainActivity.database.daoActor().getByName(item.actorName);
        ActionCreateFragment.chosenScale = MainActivity.database.daoScale().getByName(item.scaleName);

        navigateToActionCreateFrag.run();
    }
}
