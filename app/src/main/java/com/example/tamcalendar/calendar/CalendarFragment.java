package com.example.tamcalendar.calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tamcalendar.FragmentBase;
import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.action.ActionArrayAdapter;
import com.example.tamcalendar.action.ActionCreateFragment;
import com.example.tamcalendar.data.DAO_Action;
import com.example.tamcalendar.databinding.FragmentCalendarBinding;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends FragmentBase {

    private FragmentCalendarBinding binding;
    private ListView listView;
    private TamCalendar calendar;

    private static ActionArrayAdapter adapter;
    private static List<DAO_Action.FullActionData> selectedDayActionData; // use replaceSelectedDayActionData

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        calendar = binding.getRoot().findViewById(R.id.calendar);

        // setup bottom action list view
        listView = binding.getRoot().findViewById(R.id.calendar_activity_list);
        listView.setAdapter(adapter = new ActionArrayAdapter(getContext(), selectedDayActionData));

        listView.setOnItemClickListener(
                (parent, view1, position, id) -> {
                    // has not just flung calendar view
                    if (TamCalendar.lastCalendarFlingTimestamp + 150 < System.currentTimeMillis()) {

                        Dialog dialog = new Dialog(getContext());
                        dialog.setContentView(R.layout.dialog_action_detail);
                        dialog.setCancelable(true);

                        TextView header = dialog.findViewById(R.id.header);
                        header.setText(adapter.getItem(position).name);

                        TextView description = dialog.findViewById(R.id.description);
                        description.setText(adapter.getItem(position).description);

                        dialog.show();
                    } else Toast.makeText(getContext(), "Blocked!", Toast.LENGTH_SHORT).show();
                }
        );

        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view,
                                                   int position, long id) {
                        // create options dialog
                        new ActionOptionsDialog(getContext(), adapter, position,
                                // update = refresh selected day's data
                                () -> {
                                    List<DAO_Action.FullActionData> fullActionData =
                                            MainActivity.database.daoAction().fullListFromDay(
                                                    MainActivity.selectedDayDateSort
                                            );

                                    replaceListAdapterSelectedDayActionData(fullActionData);
                                    calendar.setActionDataOfDay(MainActivity.selectedDayDateSort, fullActionData);
                                },
                                // navigate to Edit
                                () -> {
                                    ActionCreateFragment.actionToEdit = adapter.getItem(position);

                                    // navigate to create screen to Edit
                                    NavHostFragment.findNavController(CalendarFragment.this)
                                            .navigate(R.id.action_CalendarFragment_to_ActionCreate);
                                });
                        return true;
                    }
                }
        );


        // pull up/down calendar on outside fling action
        listView.setOnTouchListener((v, event) -> {
            return calendar.listViewFlingListener.onTouch(v, event);
        });
        binding.getRoot().setOnTouchListener((v, event) -> {
            return calendar.listViewFlingListener.onTouch(v, event);
        });


        // show MainActivity FAButton on create
        getActivity().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onCreate(owner);
                ((MainActivity) requireActivity()).binding.fabAddEvent.show();
                ((MainActivity) requireActivity()).binding.fabAddFeeling.show();
                getActivity().getLifecycle().removeObserver(this);
            }
        });


        /*binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this).navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

         */
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Replaces selected day's actions with new data (on change of selected day)
     *
     * @param newData list of E_Action to replace current data with
     */
    public static void replaceListAdapterSelectedDayActionData(List<DAO_Action.FullActionData> newData) {
        if (selectedDayActionData == null)
            selectedDayActionData = new ArrayList<>();

        selectedDayActionData.clear();
        if (newData != null) {
            selectedDayActionData.addAll(newData);
        }

        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getFragmentTitle() {
        return R.string.app_name;
    }
}