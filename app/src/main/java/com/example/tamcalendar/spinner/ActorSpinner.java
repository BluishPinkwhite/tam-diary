package com.example.tamcalendar.spinner;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.TextView;

import com.example.tamcalendar.data.actor.E_Actor;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class ActorSpinner extends SearchableColorNameSpinner<E_Actor> {

    private Consumer<E_Actor> onItemSelected;
    public ActorSpinner(Context context, TextView parentSpinner, String headerText, String actionText,
                        View colorIcon, Callable<List<E_Actor>> dataGetFunction,Consumer<E_Actor> onItemSelected) {
        super(context, parentSpinner, headerText, actionText, colorIcon, dataGetFunction);
        this.onItemSelected = onItemSelected;
    }

    @Override
    protected E_Actor createNewInstanceFromData() {
        return new E_Actor(nameEditText.getText().toString(),
                ((ColorDrawable) (colorPreview.getBackground())).getColor());
    }

    @Override
    protected void onListItemSelected(E_Actor item) {
        super.onListItemSelected(item);

        onItemSelected.accept(item);
    }
}
