package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Category;
import com.example.tamcalendar.data.E_Value;
import com.example.tamcalendar.emotion.EmotionCreateFragment;

import java.util.List;
import java.util.concurrent.Callable;

public class ValueSpinner extends SearchableColorNameSpinner<E_Value> {

    private final DAO_Category.FullCategory parentCategory;

    public ValueSpinner(Context context, TextView parentSpinner, String headerText, String actionText, View colorIcon,
                        Callable<List<E_Value>> dataGetFunction, DAO_Category.FullCategory parentCategory) {
        super(context, parentSpinner, headerText, actionText, colorIcon, dataGetFunction);
        this.parentCategory = parentCategory;
    }

    @Override
    protected void onListItemSelected(E_Value item) {
        super.onListItemSelected(item);

        EmotionCreateFragment.categoryToEdit = MainActivity.database.daoCategory().getFull(item.ID);
    }

    @Override
    protected E_Value createNewInstanceFromData() {
        return new E_Value(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor(),
                parentCategory.category.ID);
    }
}
