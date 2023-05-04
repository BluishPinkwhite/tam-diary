package com.example.tamcalendar.calendar;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TableRow;

import androidx.annotation.NonNull;

public class CalendarGestureDetector extends GestureDetector.SimpleOnGestureListener {

    private TableRow[] weeks;
    private TableRow activeRow;

    private long lastHorizontalSwipeTime;
    private Runnable onSwipeLeft, onSwipeRight;

    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public CalendarGestureDetector(TableRow[] weeks, Runnable onSwipeLeft, Runnable onSwipeRight) {
        this.weeks = weeks;
        this.onSwipeLeft = onSwipeLeft;
        this.onSwipeRight = onSwipeRight;
    }

    @Override
    public boolean onFling(@NonNull MotionEvent e1, @NonNull MotionEvent e2, float velocityX, float velocityY) {

        TamCalendar.lastCalendarFlingTimestamp = System.currentTimeMillis();

        // HORIZONTAL SWIPE
        // cooldown to stop duplicate calls
        if (lastHorizontalSwipeTime + 150 < System.currentTimeMillis()) {
            lastHorizontalSwipeTime = System.currentTimeMillis();
            // right to left swipe
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onSwipeRight.run();
                return true;
            }
            // left to right swipe
            else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                    && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onSwipeLeft.run();
                return true;
            }
        }


        // VERTICAL SWIPE
        for (TableRow week : weeks) {
            // select first row if none were selected
            if (activeRow == null)
                activeRow = week;

            // collapse other rows
            if (week != activeRow) {
                // open/collapse calendar view
                // top to bottom swipe
                if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    week.setVisibility(View.GONE);
                    // bottom to top swipe
                } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                        && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                    week.setVisibility(View.VISIBLE);
                }
            }
        }

        return false;
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
