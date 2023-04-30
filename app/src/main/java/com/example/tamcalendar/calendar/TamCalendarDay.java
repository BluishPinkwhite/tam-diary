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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamcalendar.ParentUpdate;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.action.FullActionData;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;

import java.util.LinkedList;
import java.util.List;

public class TamCalendarDay extends FrameLayout {

    private ParentUpdate parentUpdateListener;

    private int year;
    private int month;
    private int day;
    private int dateSort;

    public boolean selected;

    private List<FullActionData> actions;
    private List<EmotionWithCategories> emotions;

    private Button dayButton;
    private TextView amountText, amountText2;

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
            // if is current month
            if (TamCalendar.lastCalendarFlingTimestamp + 150 < System.currentTimeMillis())
                if (month == todayDate.getMonthValue()) {
                    selectedDayDateSort = dateSort;

                    CalendarFragment.replaceListAdapterSelectedDayActionData(actions);
                    CalendarFragment.replaceListAdapterSelectedDayEmotionData(emotions);

                    updateData();
                    invalidate();

                    if (parentUpdateListener != null)
                        parentUpdateListener.updateParent();
                }
        });

        amountText = findViewById(R.id.amount);
        amountText2 = findViewById(R.id.amount2);
    }

    ///////////////////////


    private void updateData() {
        // set text
        boolean hasActionData = actions != null && actions.size() > 0;
        boolean hasEmotionData = emotions != null && emotions.size() > 0;

        amountText.setText(hasActionData ? actions.size() + "" : "");
        amountText2.setText(hasEmotionData ? emotions.size() + "" : "");

        // day text
        dayButton.setText(String.valueOf(day));

        // selected?
        selected = (dateSort == selectedDayDateSort);

        // white/selected
        GradientDrawable foreground = new GradientDrawable();
        foreground.setColor(selected ? 0xFF72FF70 : 0xFFFFFFFF);
        foreground.setShape(GradientDrawable.OVAL);
        InsetDrawable inset = new InsetDrawable(foreground, 30);
        LayerDrawable layers = new LayerDrawable(new Drawable[]{});


        // disabled if next/prev month bleed over
        if (month != todayDate.getMonthValue()) {
            //dayButton.setEnabled(false);
            dayButton.setTypeface(null, Typeface.ITALIC);
            dayButton.setBackground(inset);
            dayButton.setTextSize(11);
            dayButton.setTextColor(0xFFAAAAAA); // light gray
        }
        // current month (active) days
        else {
            dayButton.setTextSize(14);
            //dayButton.setEnabled(true);
            dayButton.setTextColor(Color.BLACK);

            // current day
            if (dateSort == todayDateSort) {
                dayButton.setTypeface(null, Typeface.BOLD);

                GradientDrawable border = new GradientDrawable();
                border.setColor(getContext().getColor(com.google.android.material.R.color.background_material_light));
                border.setStroke(3, getContext().getColor(com.google.android.material.R.color.background_material_dark));
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

            LinkedList<Integer> actionColors = new LinkedList<>();
            LinkedList<Integer> emotionColors = new LinkedList<>();
            int actionAmount = 0, emotionAmount = 0;

            // actions found
            if (actions != null && !actions.isEmpty()) {
                // get colors for the ring
                for (FullActionData actionData : actions) {
                    actionColors.add(actionData.scaleColor);
                }
                actionAmount = actions.size();
            }

            // emotions found
            if (emotions != null && !emotions.isEmpty()) {
                // get colors for the ring
                for (EmotionWithCategories emotionData : emotions) {
                    emotionColors.add(emotionData.emotion.scaleColor);
                }
                emotionAmount = emotions.size();
            }


            // any found
            if (!actionColors.isEmpty() || !emotionColors.isEmpty()) {
                int maxSize = Math.max(Math.max(actionAmount, emotionAmount), 5);
                int[] ringColors = new int[maxSize * 2 + 3];

                // map lists to array
                for (int i = 0; i < maxSize; i++) {
                    // top half circle
                    if (i < emotionColors.size())
                        ringColors[maxSize - i] = emotionColors.get(i);

                    // bottom half circle
                    if (i < actionColors.size())
                        ringColors[maxSize + i + 2] = actionColors.get(i);
                }

                // edges (middle line white override)
                ringColors[0] = 0;
                ringColors[ringColors.length - 1] = 0;

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
                        List<FullActionData> actions, List<EmotionWithCategories> emotions) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateSort = dateSort;
        this.actions = actions;
        this.emotions = emotions;

        setData();
    }

    public void setActionData(List<FullActionData> actions) {
        this.actions = actions;

        setData();
    }


    public void setEmotionData(List<EmotionWithCategories> emotions) {
        this.emotions = emotions;

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

    public int getDateSort() {
        return dateSort;
    }
}
