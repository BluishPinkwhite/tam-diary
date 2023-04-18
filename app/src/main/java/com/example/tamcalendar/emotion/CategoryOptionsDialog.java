package com.example.tamcalendar.emotion;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Category;

public class CategoryOptionsDialog {

    private AlertDialog dialog;
    private CategoryArrayAdapter adapter;
    private int position;
    private Runnable navigateToActionCreateFrag;

    public CategoryOptionsDialog(Context context, CategoryArrayAdapter adapter, int position, Runnable invalidate,
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

    private void deleteItemDB(DAO_Category.FullCategory item) {
        MainActivity.database.daoCategory().deleteByID(item.category.ID);
        adapter.notifyDataSetChanged();
    }

    private void prepareItemEdit(DAO_Category.FullCategory item) {
        EmotionCreateFragment.categoryToEdit = item;

        navigateToActionCreateFrag.run();
    }
}