package com.example.tamcalendar.calendar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.action.ActionArrayAdapter;
import com.example.tamcalendar.data.DAO_Action;
import com.example.tamcalendar.databinding.FragmentCalendarBinding;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;
    private ListView listView;

    private static ActionArrayAdapter adapter;
    private static List<DAO_Action.FullActionData> selectedDayActionData; // use replaceSelectedDayActionData

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup bottom action list view
        listView = binding.getRoot().findViewById(R.id.calendar_activity_list);
        listView.setAdapter(adapter = new ActionArrayAdapter(getContext(), selectedDayActionData));

        listView.setOnItemClickListener(
                (parent, view1, position, id) -> {
                    Dialog dialog = new Dialog(getContext());
                    dialog.setContentView(R.layout.dialog_action_detail);
                    dialog.setCancelable(true);

                    TextView header = dialog.findViewById(R.id.header);
                    header.setText(adapter.getItem(position).name);

                    TextView description = dialog.findViewById(R.id.description);
                    description.setText(adapter.getItem(position).description);

                    dialog.show();
                }
        );


        // show MainActivity FAButton on create
        getActivity().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onCreate(owner);
                ((MainActivity) getActivity()).binding.fab.show();
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

    public static void replaceSelectedDayActionData(List<DAO_Action.FullActionData> newData) {
        if (selectedDayActionData == null)
            selectedDayActionData = new ArrayList<>();

        selectedDayActionData.clear();
        if (newData != null) {
            selectedDayActionData.addAll(newData);
        }

        if (adapter != null)
            adapter.notifyDataSetChanged();
    }
}