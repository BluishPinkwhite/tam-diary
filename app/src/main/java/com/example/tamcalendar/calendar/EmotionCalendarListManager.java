package com.example.tamcalendar.calendar;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;
import com.example.tamcalendar.emotion.EmotionArrayAdapter;
import com.example.tamcalendar.emotion.EmotionCreateFragment;
import com.example.tamcalendar.emotion.EmotionOptionsDialog;

import java.util.List;

public class EmotionCalendarListManager {

    public static void setActionListOnItemClick(ListView listView, CalendarFragment fragment) {
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // has not just flung calendar view
            if (TamCalendar.lastCalendarFlingTimestamp + 150 < System.currentTimeMillis()) {

                Dialog dialog = new Dialog(fragment.getContext());
                dialog.setContentView(R.layout.dialog_emotion_detail);
                dialog.setCancelable(true);

                TextView header = dialog.findViewById(R.id.time);
                header.setText(fragment.getString(R.string.time_ref,
                        CalendarFragment.emotionAdapter.getItem(position).emotion.hour));

                TextView description = dialog.findViewById(R.id.description);
                description.setText(CalendarFragment.emotionAdapter.getItem(position).emotion.description);

                // childContainer for categories
                EmotionArrayAdapter.fillCategoryDisplayValuesToContainer(fragment.getContext(),
                        dialog.findViewById(R.id.childContainer), CalendarFragment.emotionAdapter.getItem(position));

                dialog.show();
            }
        });
    }

    public static void setActionListOnItemLongClick(ListView listView, CalendarFragment fragment) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // create options dialog
                new EmotionOptionsDialog(fragment.getContext(), CalendarFragment.emotionAdapter, position,
                        // update = refresh selected day's data
                        () -> {
                            List<EmotionWithCategories> fullEmotionData = MainActivity.database.daoEmotion().fullCategoryListFromDay(MainActivity.selectedDayDateSort);

                            CalendarFragment.replaceListAdapterSelectedDayEmotionData(fullEmotionData);
                            fragment.getBinding().calendar.setEmotionDataOfDay(MainActivity.selectedDayDateSort, fullEmotionData);
                        },
                        // navigate to Edit
                        () -> {
                            EmotionCreateFragment.emotionToEdit = CalendarFragment.emotionAdapter.getItem(position);

                            // navigate to create screen to Edit
                            NavHostFragment.findNavController(fragment).navigate(R.id.action_CalendarFragment_to_EmotionCreate);
                        });
                return true;
            }
        });
    }
}
