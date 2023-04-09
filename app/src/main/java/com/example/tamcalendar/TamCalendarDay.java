package com.example.tamcalendar;

import static com.example.tamcalendar.MainActivity.date;

import android.content.Context;
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

import com.example.tamcalendar.data.E_Action;

import java.util.List;

public class TamCalendarDay extends FrameLayout {

    private int year;
    private int month;
    private int day;
    private int dateSort;

    private List<E_Action> actions;

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
            Toast.makeText(getContext(), toString(), Toast.LENGTH_SHORT).show();
        });

        amountText = findViewById(R.id.amount);
    }

    ///////////////////////

    private void updateData() {
        // set text
        if (actions == null || actions.size() == 0) amountText.setText("");
        else amountText.setText("" + actions.size());

        dayButton.setText("" + day);

        // disabled if next/prev month bleed over
        if (month != date.getMonthValue()) {
            dayButton.setEnabled(false);
            dayButton.setTypeface(null, Typeface.ITALIC);
            dayButton.setBackground(null);
            dayButton.setTextSize(11);
        } // current day (of month)


        else if (day == date.getDayOfMonth()) {
            dayButton.setEnabled(true);
            dayButton.setTypeface(null, Typeface.BOLD);

            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF);
            border.setStroke(3, 0xFF000000);
            border.setCornerRadius(32);
            dayButton.setBackground(border);
        }


        // normal
        else {
            dayButton.setEnabled(true);
            dayButton.setTypeface(null, Typeface.NORMAL);
            dayButton.setTextSize(14);

            GradientDrawable background = new GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    new int[]{0xFFFFFF00, 0xFFFF0000, 0xFF00FF00, 0xFF6744FF, 0xFFFFFF00}
            );
            background.setShape(GradientDrawable.OVAL);
            background.setGradientType(GradientDrawable.SWEEP_GRADIENT);


            GradientDrawable foreground = new GradientDrawable();
            foreground.setColor(0xFFFFFFFF);
            foreground.setShape(GradientDrawable.OVAL);

            InsetDrawable inset = new InsetDrawable(foreground, 25);

            LayerDrawable layers = new LayerDrawable(new Drawable[]{background, inset});
            dayButton.setBackground(layers);
        }
    }

    //////////////////////

    public void setData(int year, int month, int day, int dateSort, List<E_Action> actions) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.dateSort = dateSort;
        this.actions = actions;

        updateData();

        // force redraw
        invalidate();
    }

    @Override
    public String toString() {
        return day + "." + month + "." + year;
    }
}
