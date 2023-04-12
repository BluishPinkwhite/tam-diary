package com.example.tamcalendar.spinner;

import static com.example.tamcalendar.MainActivity.database;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.E_Actor;

import java.util.List;
import java.util.concurrent.Callable;

import top.defaults.colorpicker.ColorPickerPopup;

public class SearchableColorNameSpinner<T extends ColorNameHaver> extends SearchableSpinnerWithAdd<T> {

    private final View colorIcon;
    private final Callable<List<T>> dataGetFunction;

    private View colorPreview;
    private EditText nameEditText;

    public SearchableColorNameSpinner(Context context, TextView parentSpinner, String headerText, String addNewItemText,
                                      View colorIcon, Callable<List<T>> dataGetFunction) {
        super(context, parentSpinner, headerText, addNewItemText);

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
    protected boolean canAddNewItem() {
        return !nameEditText.getText().toString().isEmpty();
    }

    @Override
    protected void insertNewItemIntoDB() {
        database.daoActor().insert(
                new E_Actor(
                        nameEditText.getText().toString(),
                        ((ColorDrawable) (colorPreview.getBackground())).getColor())
        );
    }
}
