package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.category.FullCategory;
import com.example.tamcalendar.data.value.E_Value;
import com.example.tamcalendar.emotion.CategoryArrayAdapter;
import com.example.tamcalendar.emotion.EmotionCreateFragment;

import java.util.List;
import java.util.concurrent.Callable;

public class ValueSpinner extends SearchableColorNameSpinner<E_Value> {

    private final FullCategory parentCategory;

    public ValueSpinner(Context context, TextView parentSpinner, String headerText, String actionText, View colorIcon,
                        Callable<List<E_Value>> dataGetFunction, FullCategory parentCategory) {
        super(context, parentSpinner, headerText, actionText, colorIcon, dataGetFunction);
        this.parentCategory = parentCategory;
    }

    @Override
    protected void onListItemSelected(E_Value item) {
        super.onListItemSelected(item);

        CategoryArrayAdapter.selectedValueAtCategoryIndex.put(parentCategory, item);
        EmotionCreateFragment.categoryToEdit = MainActivity.database.daoCategory().getFull(item.valueID);
    }

    @Override
    protected E_Value createNewInstanceFromData() {
        return new E_Value(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor(),
                parentCategory.category.categoryID);
    }
}
