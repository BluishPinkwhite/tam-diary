package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.data.E_Actor;

import java.util.List;
import java.util.concurrent.Callable;

public class ActorSpinner extends SearchableColorNameSpinner<E_Actor> {
    public ActorSpinner(Context context, TextView parentSpinner, String headerText, String addNewItemText, View colorIcon, Callable<List<E_Actor>> dataGetFunction) {
        super(context, parentSpinner, headerText, addNewItemText, colorIcon, dataGetFunction);
    }

    @Override
    protected E_Actor createNewInstanceFromData() {
        return new E_Actor(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor());
    }
}
