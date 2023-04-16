package com.example.tamcalendar.calendar;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableRow;

import androidx.annotation.NonNull;

public class CalendarGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private TableRow[] weeks;
    private TableRow activeRow;

    public CalendarGestureDetector(TableRow[] weeks) {
        this.weeks = weeks;
    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {
        for (TableRow week :
                weeks) {

            if (activeRow == null)
                activeRow = week;

            if (week != activeRow) {
                if (velocityY > 0) {
                    week.setVisibility(View.VISIBLE);
                } else {
                    week.setVisibility(View.GONE);
                }
            }
        }
        return true;
    }

    @Override
    public boolean onDown(@NonNull MotionEvent e) {
        return true;
    }

    @Override
    public boolean onSingleTapUp(@NonNull MotionEvent e) {
        return true;
    }

    public void setActiveRow(TableRow activeRow) {
        this.activeRow = activeRow;
    }
}
