package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tamcalendar.R;
import com.example.tamcalendar.calendar.ColorArrayAdapter;

import java.util.List;
import java.util.concurrent.Callable;

import top.defaults.colorpicker.ColorPickerPopup;

public abstract class SearchableColorNameSpinner<T extends ColorNameHaver<T>> extends SearchableSpinnerWithAdd<T> {

    private final View colorIcon;
    private final Callable<List<T>> dataGetFunction;

    protected View colorPreview;
    protected EditText nameEditText;

    public SearchableColorNameSpinner(Context context, TextView parentSpinner, String headerText, String actionText,
                                      View colorIcon, Callable<List<T>> dataGetFunction) {
        super(context, parentSpinner, headerText, actionText);

        this.colorIcon = colorIcon;
        this.dataGetFunction = dataGetFunction;
    }


    @Override
    protected void onListItemSelected(T item) {
        colorIcon.setBackgroundColor(item.color);
    }

    @Override
    protected void newItemDialogExtraSetup() {
        nameEditText = addNewDialog.findViewById(R.id.edit_text);

        colorPreview = addNewDialog.findViewById(R.id.color_preview);
        colorPreview.setOnClickListener(colorPreviewView -> {
            ColorPickerPopup colorPickerPopup = new ColorPickerPopup.Builder(getContext())
                    .initialColor(((ColorDrawable) (colorPreview.getBackground())).getColor())
                    .enableBrightness(false)
                    .enableAlpha(false)
                    .okTitle(getContext().getString(R.string.select))
                    .cancelTitle(getContext().getString(R.string.cancel))
                    .showIndicator(true)
                    .showValue(false)
                    .build();

            colorPickerPopup.show(colorPreviewView, new ColorPickerPopup.ColorPickerObserver() {
                @Override
                public void onColorPicked(int color) {
                    colorPreview.setBackgroundColor(color);
                }
            });
        });
    }


    @Override
    protected List<T> getData() {
        try {
            return dataGetFunction.call();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected ArrayAdapter<T> createListAdapter() {
        return new ColorArrayAdapter<>(getContext(), objects);
    }

    @Override
    protected boolean isItemDataValid() {
        return !nameEditText.getText().toString().isEmpty();
    }

    @Override
    protected void insertNewItemDB() {
        createNewInstanceFromData().insertToDB();
    }

    @Override
    protected void prepareItemEdit(T item) {
        showNewItemDialog(true);

        editedItem = item;

        nameEditText.setText(item.name);
        colorPreview.setBackgroundColor(item.color);
    }

    @Override
    protected void deleteItemDB(T item) {
        item.deleteFromDB();
    }

    @Override
    protected void updateItemDB(T item) {
        item.copy(createNewInstanceFromData()).updateToDB();
    }
}
