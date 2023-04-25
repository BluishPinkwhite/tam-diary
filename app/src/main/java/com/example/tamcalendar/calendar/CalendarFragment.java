package com.example.tamcalendar.calendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.tamcalendar.FragmentBase;
import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.action.ActionArrayAdapter;
import com.example.tamcalendar.data.action.FullActionData;
import com.example.tamcalendar.data.category.E_Category;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;
import com.example.tamcalendar.databinding.FragmentCalendarBinding;
import com.example.tamcalendar.emotion.EmotionArrayAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends FragmentBase {

    private FragmentCalendarBinding binding;

    static ActionArrayAdapter actionAdapter;
    private static List<FullActionData> selectedDayActionData = new ArrayList<>(); // use replaceSelectedDayActionData

    static EmotionArrayAdapter emotionAdapter;
    private static List<EmotionWithCategories> selectedDayEmotionData = new ArrayList<>(); // use replaceSelectedDayEmotionData
    public static Map<Long, E_Category> allCategoriesByID = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // setup bottom action list view (updateData() on change)
        binding.calendarActionList.setAdapter(actionAdapter = new ActionArrayAdapter(getContext(),
                selectedDayActionData, this::updateData));

        ActionCalendarListManager.setActionListOnItemClick(binding.calendarActionList, this);
        ActionCalendarListManager.setActionListOnItemLongClick(binding.calendarActionList, this);

        // setup bottom emotion list view
        binding.calendarEmotionList.setAdapter(emotionAdapter = new EmotionArrayAdapter(getContext(),
                selectedDayEmotionData, this::updateData, this::getString));

        EmotionCalendarListManager.setActionListOnItemClick(binding.calendarEmotionList, this);
        EmotionCalendarListManager.setActionListOnItemLongClick(binding.calendarEmotionList, this);

        // REMOVED
        /*/ pull up/down calendar on outside fling action

        binding.calendarActionList.setOnTouchListener(
                binding.calendar.listViewFlingListener::onTouch);
        binding.getRoot().setOnTouchListener(
                binding.calendar.listViewFlingListener::onTouch);
         */


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

        updateData();
    }

    private void updateData() {
        if (binding != null && binding.noActions != null)
            binding.noActions.setVisibility(selectedDayActionData.isEmpty() ? View.VISIBLE : View.GONE);

        if (binding != null && binding.noEmotion != null)
            binding.noEmotion.setVisibility(selectedDayEmotionData.isEmpty() ? View.VISIBLE : View.GONE);
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
    public static void replaceListAdapterSelectedDayActionData(List<FullActionData> newData) {
        if (selectedDayActionData == null)
            selectedDayActionData = new ArrayList<>();

        selectedDayActionData.clear();
        if (newData != null) {
            selectedDayActionData.addAll(newData);
        }

        refreshAllCategoriesIDMap();

        if (actionAdapter != null) {
            actionAdapter.notifyDataSetChanged();
        }
    }

    public static void refreshAllCategoriesIDMap() {
        allCategoriesByID = MainActivity.database.daoCategory().listMapByID();
    }

    /**
     * Replaces selected day's emotions with new data (on change of selected day)
     *
     * @param newData list of E_Emotion to replace current data with
     */
    public static void replaceListAdapterSelectedDayEmotionData(List<EmotionWithCategories> newData) {
        if (selectedDayEmotionData == null)
            selectedDayEmotionData = new ArrayList<>();

        selectedDayEmotionData.clear();
        if (newData != null) {
            selectedDayEmotionData.addAll(newData);
        }

        if (emotionAdapter != null) {
            emotionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected int getFragmentTitle() {
        return R.string.app_name;
    }

    public FragmentCalendarBinding getBinding() {
        return binding;
    }
}