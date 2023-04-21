package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.data.E_Scale;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class ScaleSpinner extends SearchableColorNameSpinner<E_Scale> {

    private Consumer<E_Scale> onItemSelected;

    public ScaleSpinner(Context context, TextView parentSpinner, String headerText, String actionText,
                        View colorIcon, Callable<List<E_Scale>> dataGetFunction, Consumer<E_Scale> onItemSelected) {
        super(context, parentSpinner, headerText, actionText, colorIcon, dataGetFunction);
        this.onItemSelected = onItemSelected;
    }

    @Override
    protected E_Scale createNewInstanceFromData() {
        return new E_Scale(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor());
    }

    @Override
    protected void onListItemSelected(E_Scale item) {
        super.onListItemSelected(item);

        onItemSelected.accept(item);
    }
}
