package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.data.E_Scale;

import java.util.List;
import java.util.concurrent.Callable;

public class ScaleSpinner extends SearchableColorNameSpinner<E_Scale> {

    public ScaleSpinner(Context context, TextView parentSpinner, String headerText, String addNewItemText,
                        View colorIcon, Callable<List<E_Scale>> dataGetFunction) {
        super(context, parentSpinner, headerText, addNewItemText, colorIcon, dataGetFunction);
    }

    @Override
    protected E_Scale createNewInstanceFromData() {
        return new E_Scale(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor());
    }
}
