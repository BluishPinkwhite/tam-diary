package com.example.tamcalendar.views;

import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

import androidx.appcompat.app.AlertDialog;

import com.example.tamcalendar.R;

public abstract class OptionsDialog<T> {

    private AlertDialog dialog;
    private ArrayAdapter<T> adapter;
    private int position;
    private Runnable navigateToCreateFrag;

    public OptionsDialog(Context context, ArrayAdapter<T> adapter, int position, Runnable invalidate,
                         Runnable navigateToCreateFrag) {
        this.adapter = adapter;
        this.position = position;
        this.navigateToCreateFrag = navigateToCreateFrag;

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
                                runPrepareItemEdit(adapter.getItem(position));
                                invalidate.run();
                                break;
                            case 1: // Delete
                                runDeleteItem(adapter.getItem(position));
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


    private void runDeleteItem(T item) {
        deleteItemDB(item);

        adapter.notifyDataSetChanged();
    }

    protected abstract void deleteItemDB(T item);

    private void runPrepareItemEdit(T item) {
        prepareItemEdit(item);

        navigateToCreateFrag.run();
    }

    protected abstract void prepareItemEdit(T item);
}
