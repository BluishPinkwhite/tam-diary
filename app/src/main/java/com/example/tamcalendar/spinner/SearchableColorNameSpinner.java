package com.example.tamcalendar.spinner;

import static com.example.tamcalendar.MainActivity.database;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.E_Actor;

import java.util.List;
import java.util.concurrent.Callable;

import top.defaults.colorpicker.ColorPickerPopup;

public class SearchableColorNameSpinner<T extends ColorNameHaver> extends SearchableSpinner<T> {

    private final View colorIcon;
    private final Callable<List<T>> dataGetFunction;
    protected Dialog addNewDialog;

    public SearchableColorNameSpinner(Context context, TextView parentSpinner, String headerText,
                                      View colorIcon, Callable<List<T>> dataGetFunction) {
        super(context, parentSpinner, headerText);

        this.colorIcon = colorIcon;
        this.dataGetFunction = dataGetFunction;
    }


    @Override
    protected AdapterView.OnItemClickListener createOnListItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                T item = (T) adapter.getItem(position);

                parentSpinner.setText(item.name);
                colorIcon.setBackgroundColor(item.color);
                dialog.dismiss();
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

                TextView header = addNewDialog.findViewById(R.id.header);
                header.setText(R.string.add_new_actor);

                View colorPreview = addNewDialog.findViewById(R.id.color_preview);
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

                Button addButton = addNewDialog.findViewById(R.id.addButton);
                addButton.setOnClickListener(
                        addOnClickListenerSetup(addButton, colorPreview)
                );

                addNewDialog.show();
            }
        };
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
        return new ColorArrayAdapter<>(getContext(), getData());
    }


    private View.OnClickListener addOnClickListenerSetup(Button addButton, View colorPreview) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // is name filled out
                EditText nameEditText = (EditText) (addNewDialog.findViewById(R.id.edit_text));
                if (!nameEditText.getText().toString().isEmpty()) {
                    // insert new into DB
                    database.daoActor().insert(
                            new E_Actor(
                                    nameEditText.getText().toString(),
                                    ((ColorDrawable) (colorPreview.getBackground())).getColor())
                    );

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
}
