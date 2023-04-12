package com.example.tamcalendar.spinner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.TextView;

import com.example.tamcalendar.R;

public abstract class SearchableSpinnerWithAdd<T> extends SearchableSpinner<T> {

    protected Dialog addNewDialog;
    protected TextView header;

    private final String addNewItemText;

    public SearchableSpinnerWithAdd(Context context, TextView parentSpinner, String headerText, String addNewItemText) {
        super(context, parentSpinner, headerText);

        this.addNewItemText = addNewItemText;
    }


    protected View.OnClickListener addOnClickListenerSetup(Button addButton) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (canAddNewItem()) {
                    insertNewItemIntoDB();

                    // close dialog, update data
                    updateData();
                    addNewDialog.dismiss();
                } else {
                    addButton.setBackgroundColor(Color.RED);

                    Animation anim = new AlphaAnimation(0, 1);
                    anim.setDuration(30);
                    anim.setRepeatCount(3);
                    anim.setRepeatMode(Animation.REVERSE);
                    addButton.startAnimation(anim);

                    addButton.postOnAnimationDelayed(() -> {
                        addButton.setBackgroundColor(getResources().getColor(
                                com.google.android.material.R.color.design_default_color_primary,
                                getContext().getTheme()));
                    }, 250);
                }
            }
        };
    }


    @Override
    protected OnClickListener createAddButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewDialog = new Dialog(getContext());
                addNewDialog.setContentView(R.layout.dialog_add_option);
                addNewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                header = addNewDialog.findViewById(R.id.header);
                header.setText(addNewItemText);

                newItemDialogExtraSetup();

                Button addButton = addNewDialog.findViewById(R.id.addButton);
                addButton.setOnClickListener(
                        addOnClickListenerSetup(addButton)
                );

                addNewDialog.show();
            }
        };
    }

    protected abstract void newItemDialogExtraSetup();
}
