package com.example.tamcalendar;

import static com.example.tamcalendar.MainActivity.database;
import static com.example.tamcalendar.MainActivity.date;
import static com.example.tamcalendar.data.DatabaseManager.createDateSort;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamcalendar.data.E_Action;

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
            date = date.minusMonths(1);
            setData();
        });

        Button nextM = findViewById(R.id.buttonNextM);
        nextM.setOnClickListener(view -> {
            date = date.plusMonths(1);
            setData();
        });

        table = findViewById(R.id.table);
        days = new TamCalendarDay[6][7];

        // day names
        TableRow dayNameRow = (TableRow) table.getChildAt(0);
        for (int i = 0; i < 7; i++) {
            TextView dayName = (TextView) dayNameRow.getChildAt(i);
            dayName.setText(DayOfWeek.of(i +1).getDisplayName(TextStyle.SHORT, Locale.CANADA));
        }

        // TD refs
        for (int rowI = 0; rowI < days.length; rowI++) {
            TableRow row = (TableRow) table.getChildAt(rowI + 1);

            for (int dayI = 0; dayI < days[rowI].length; dayI++) {
                days[rowI][dayI] = (TamCalendarDay) row.getChildAt(dayI);
            }
        }

        dateText = findViewById(R.id.dateText);

        setData();
    }


    ///////////////////////

    private void updateData() {
        dateText.setText(date.getMonth().name() + " " + date.getYear());

        LocalDate start = date
                .minusMonths(1)
                .with(TemporalAdjusters.lastInMonth(DayOfWeek.SUNDAY))
                .plusDays(1);
        LocalDate end = start.plusDays(34);

        Map<Integer, List<E_Action>> dateSortActionMap = database.daoAction().listBetween(
                createDateSort(start), createDateSort(end)
        );

        LocalDate entryDate = start;

        for (int rowI = 0; rowI < days.length; rowI++) {
            for (int dayI = 0; dayI < days[rowI].length; dayI++) {
                TamCalendarDay day = days[rowI][dayI];

                int entryDateSort = createDateSort(entryDate);

                day.setData(entryDate.getYear(), entryDate.getMonthValue(), entryDate.getDayOfMonth(),
                        entryDateSort, dateSortActionMap.get(entryDateSort));

                entryDate = entryDate.plusDays(1);
            }
        }
    }

    public void setData() {
        updateData();

        // redraw
        invalidate();
    }
}
