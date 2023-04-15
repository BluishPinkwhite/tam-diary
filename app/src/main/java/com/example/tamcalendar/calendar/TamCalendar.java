package com.example.tamcalendar.calendar;

import static com.example.tamcalendar.MainActivity.database;
import static com.example.tamcalendar.MainActivity.selectedDayDateSort;
import static com.example.tamcalendar.MainActivity.todayDate;
import static com.example.tamcalendar.data.DatabaseManager.createDateSort;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Action;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TamCalendar extends FrameLayout {

    private TextView dateText;
    private TableLayout table;
    private TamCalendarDay[][] days;

    public TamCalendar(@NonNull Context context) {
        super(context);
        initView();
    }

    public TamCalendar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.tam_calendar, this);

        Button prevM = findViewById(R.id.buttonPrevM);
        prevM.setOnClickListener(view -> {
            todayDate = todayDate.minusMonths(1);
            setData();
        });

        Button nextM = findViewById(R.id.buttonNextM);
        nextM.setOnClickListener(view -> {
            todayDate = todayDate.plusMonths(1);
            setData();
        });

        table = findViewById(R.id.table);
        days = new TamCalendarDay[6][7];
        TamCalendarDay selectedDay = null;

        // day names
        TableRow dayNameRow = (TableRow) table.getChildAt(0);
        for (int i = 0; i < 7; i++) {
            TextView dayName = (TextView) dayNameRow.getChildAt(i);
            dayName.setText(DayOfWeek.of(i + 1).getDisplayName(TextStyle.SHORT, Locale.CANADA));
        }

        // TD refs
        for (int rowI = 0; rowI < days.length; rowI++) {
            TableRow row = (TableRow) table.getChildAt(rowI + 1);

            for (int dayI = 0; dayI < days[rowI].length; dayI++) {
                TamCalendarDay day = (TamCalendarDay) row.getChildAt(dayI);
                days[rowI][dayI] = day;
                day.setParentUpdateListener(() -> invalidateRecursive(this));
            }
        }

        dateText = findViewById(R.id.dateText);

        setData();
    }

    public void invalidateRecursive(ViewGroup layout) {

        int count = layout.getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = layout.getChildAt(i);
            if (child instanceof TamCalendarDay) {
                TamCalendarDay day = (TamCalendarDay) child;
                day.setData();
            } else if (child instanceof ViewGroup)
                invalidateRecursive((ViewGroup) child);
        }
    }

    public void setActionDataOfDay(int dateSort, List<DAO_Action.FullActionData> fullActionData) {
        for (TamCalendarDay[] row :
                days) {
            for (TamCalendarDay day :
                    row) {
                // find day with desired dateSort
                if (day.getDateSort() == dateSort) {
                    day.setData(fullActionData);
                    return;
                }
            }
        }
    }


    ///////////////////////

    private void updateData() {
        dateText.setText(todayDate.getMonth().name() + " " + todayDate.getYear());

        LocalDate start = todayDate
                .minusMonths(1)
                .with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY))
                .plusDays(1);
        LocalDate end = start.plusDays(34);

        Map<Integer, List<DAO_Action.FullActionData>> dateSortActionMap = database.daoAction().listBetween(
                createDateSort(start), createDateSort(end)
        );

        LocalDate entryDate = start;

        for (int rowI = 0; rowI < days.length; rowI++) {
            for (int dayI = 0; dayI < days[rowI].length; dayI++) {
                TamCalendarDay day = days[rowI][dayI];

                int entryDateSort = createDateSort(entryDate);
                List<DAO_Action.FullActionData> actionData = dateSortActionMap.get(entryDateSort);

                day.setData(entryDate.getYear(), entryDate.getMonthValue(), entryDate.getDayOfMonth(),
                        entryDateSort, actionData);

                entryDate = entryDate.plusDays(1);

                // default bottom action view data
                if (selectedDayDateSort == entryDateSort) {
                    CalendarFragment.replaceListAdapterSelectedDayActionData(actionData);
                }
            }
        }
    }

    public void setData() {
        updateData();

        // redraw
        invalidate();
    }
}
