package com.example.tamcalendar.calendar;

import static com.example.tamcalendar.MainActivity.selectedDayDateSort;
import static com.example.tamcalendar.MainActivity.todayDate;
import static com.example.tamcalendar.MainActivity.todayDateSort;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamcalendar.ParentUpdate;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Action;

import java.util.List;

public class TamCalendarDay extends FrameLayout {

    private ParentUpdate parentUpdateListener;

    private int year;
    private int month;
    private int day;
    private int dateSort;

    public boolean selected;

    private List<DAO_Action.FullActionData> actions;

    private Button dayButton;
    private TextView amountText;

    public TamCalendarDay(@NonNull Context context) {
        super(context);
        initView();
    }

    public TamCalendarDay(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.tam_calendar_day, this);

        dayButton = findViewById(R.id.day);
        dayButton.setOnClickListener(view -> {
            selectedDayDateSort = dateSort;
            Toast.makeText(getContext(), toString(), Toast.LENGTH_SHORT).show();

            updateData();
            invalidate();

            if (parentUpdateListener != null)
                parentUpdateListener.updateParent();
        });

        amountText = findViewById(R.id.amount);
    }

    ///////////////////////


    private void updateData() {
        // set text
        if (actions == null || actions.size() == 0) amountText.setText("");
        else amountText.setText("" + actions.size());

        dayButton.setText("" + day);

        selected = (dateSort == selectedDayDateSort);

        // white/selected
        GradientDrawable foreground = new GradientDrawable();
        foreground.setColor(selected ? 0xFF72FF70 : 0xFFFFFFFF);
        foreground.setShape(GradientDrawable.OVAL);
        InsetDrawable inset = new InsetDrawable(foreground, 30);
        LayerDrawable layers = new LayerDrawable(new Drawable[]{});


        // disabled if next/prev month bleed over
        if (month != todayDate.getMonthValue()) {
            dayButton.setEnabled(false);
            dayButton.setTypeface(null, Typeface.ITALIC);
            dayButton.setBackground(inset);
            dayButton.setTextSize(11);
            dayButton.setTextColor(0xFFAAAAAA); // light gray
        }
        // current month (active) days
        else {
            dayButton.setTextSize(14);
            dayButton.setEnabled(true);
            dayButton.setTextColor(Color.BLACK);

            // current day (of month)
            if (day == todayDate.getDayOfMonth()) {
                dayButton.setTypeface(null, Typeface.BOLD);

                GradientDrawable border = new GradientDrawable();
                border.setColor(0xFFFFFFFF);
                border.setStroke(3, 0xFF000000);
                border.setCornerRadius(32);

                layers.addLayer(border);
            }
            // past days
            else if (dateSort < todayDateSort) {
                dayButton.setTypeface(null, Typeface.NORMAL);
            }
            // future days
            else {
                dayButton.setTextSize(12);
                dayButton.setTypeface(null, Typeface.ITALIC);
            }

            // actions found
            if (actions != null) {
                // get colors for the ring (half actors, half scales)
                int[] ringColors = new int[actions.size() * 2 + 1];
                for (int index = 0; index < actions.size(); index++) {
                    DAO_Action.FullActionData actionData = actions.get(index);

                    ringColors[index] = actionData.actorColor;
                    ringColors[ringColors.length - 2 - index] = actionData.scaleColor;

                    // duplicate first as last to close color circle (remove hard edge)
                    if (index == 0) {
                        ringColors[ringColors.length - 1] = actionData.actorColor;
                    }
                }

                GradientDrawable background = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        ringColors
                );

                background.setShape(GradientDrawable.OVAL);
                background.setGradientType(GradientDrawable.SWEEP_GRADIENT);

                layers.addLayer(background);
            }
        }

        layers.addLayer(inset);
        dayButton.setBackground(layers);
    }

    //////////////////////

    public void setData(int year, int month, int day, int dateSort,
                        List<DAO_Action.FullActionData> actions) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateSort = dateSort;
        this.actions = actions;

        setData();
    }

    public void setData() {
        updateData();
        // force redraw
        invalidate();
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }

    public void setParentUpdateListener(ParentUpdate listener) {
        parentUpdateListener = listener;
    }
}
