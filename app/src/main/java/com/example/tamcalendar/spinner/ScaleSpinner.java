package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.action.ActionCreateFragment;
import com.example.tamcalendar.data.E_Scale;

import java.util.List;
import java.util.concurrent.Callable;

public class ScaleSpinner extends SearchableColorNameSpinner<E_Scale> {

    public ScaleSpinner(Context context, TextView parentSpinner, String headerText, String actionText,
                        View colorIcon, Callable<List<E_Scale>> dataGetFunction) {
        super(context, parentSpinner, headerText, actionText, colorIcon, dataGetFunction);
    }

    @Override
    protected E_Scale createNewInstanceFromData() {
        return new E_Scale(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor());
    }

    @Override
    protected void onListItemSelected(E_Scale item) {
        super.onListItemSelected(item);

        ActionCreateFragment.chosenScale = item;
    }
}
