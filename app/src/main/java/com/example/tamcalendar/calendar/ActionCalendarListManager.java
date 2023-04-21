package com.example.tamcalendar.calendar;

import android.app.Dialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.navigation.fragment.NavHostFragment;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.action.ActionCreateFragment;
import com.example.tamcalendar.action.ActionOptionsDialog;
import com.example.tamcalendar.data.DAO_Action;

import java.util.List;

public class ActionCalendarListManager {

    public static void setActionListOnItemClick(ListView listView, CalendarFragment fragment) {
        listView.setOnItemClickListener((parent, view1, position, id) -> {
            // has not just flung calendar view
            if (TamCalendar.lastCalendarFlingTimestamp + 150 < System.currentTimeMillis()) {

                Dialog dialog = new Dialog(fragment.getContext());
                dialog.setContentView(R.layout.dialog_action_detail);
                dialog.setCancelable(true);

                TextView header = dialog.findViewById(R.id.header);
                header.setText(CalendarFragment.actionAdapter.getItem(position).name);

                TextView description = dialog.findViewById(R.id.description);
                description.setText(CalendarFragment.actionAdapter.getItem(position).description);

                dialog.show();
            }
        });
    }

    public static void setActionListOnItemLongClick(ListView listView, CalendarFragment fragment) {
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // create options dialog
                new ActionOptionsDialog(fragment.getContext(), CalendarFragment.actionAdapter, position,
                        // update = refresh selected day's data
                        () -> {
                            List<DAO_Action.FullActionData> fullActionData = MainActivity.database.daoAction().fullListFromDay(MainActivity.selectedDayDateSort);

                            CalendarFragment.replaceListAdapterSelectedDayActionData(fullActionData);
                            fragment.getBinding().calendar.setActionDataOfDay(MainActivity.selectedDayDateSort, fullActionData);
                        },
                        // navigate to Edit
                        () -> {
                            ActionCreateFragment.actionToEdit = CalendarFragment.actionAdapter.getItem(position);

                            // navigate to create screen to Edit
                            NavHostFragment.findNavController(fragment).navigate(R.id.action_CalendarFragment_to_ActionCreate);
                        });
                return true;
            }
        });
    }
}
