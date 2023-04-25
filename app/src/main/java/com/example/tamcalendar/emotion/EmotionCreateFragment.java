package com.example.tamcalendar.emotion;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tamcalendar.FragmentBase;
import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.category.E_Category;
import com.example.tamcalendar.data.category.FullCategory;
import com.example.tamcalendar.data.emotion.E_Emotion;
import com.example.tamcalendar.data.emotion.EmotionValueCrossRef;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;
import com.example.tamcalendar.data.scale.E_Scale;
import com.example.tamcalendar.data.value.E_Value;
import com.example.tamcalendar.databinding.FragmentEmotionCreateBinding;
import com.example.tamcalendar.spinner.ScaleSpinner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EmotionCreateFragment extends FragmentBase {

    FragmentEmotionCreateBinding binding;
    TextView selectedScale, editTextDescription;
    View selectedScaleIcon;
    Button confirmButton;
    /**
     * Hour selector number picker. To get value use getHourValue().
     */
    private NumberPicker hourSelector;

    ListView categoryListView;
    List<FullCategory> categoryList;

    static Dialog addNewDialog;


    // refs used to edit self (or create new emotion)
    public static E_Scale chosenScale;
    public static FullCategory categoryToEdit;
    public static EmotionWithCategories emotionToEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = FragmentEmotionCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedScale = binding.selectedScale;
        selectedScaleIcon = binding.colorIconScale;
        editTextDescription = binding.description;

        confirmButton = binding.confirmButton;
        confirmButton.setOnClickListener(v -> {
            // valid data
            if (isEmotionDataValid()) {
                // new action
                if (emotionToEdit == null) {
                    insertNewEmotion();
                }
                // edit action
                else {
                    updateEmotion();
                }

                // navigate back to calendar
                NavHostFragment.findNavController(EmotionCreateFragment.this)
                        .navigate(R.id.action_emotion_create_to_Calendar);
            }
        });


        // category setup
        categoryList = new ArrayList<>();
        updateData();
        CategoryArrayAdapter.selectedValueAtCategoryName.clear();

        categoryListView = binding.categoryList;
        CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(getContext(), categoryList);
        categoryListView.setAdapter(categoryAdapter);


        getActivity().runOnUiThread(
                () -> ((CategoryArrayAdapter) categoryListView.getAdapter()).notifyDataSetChanged()
        );

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                new CategoryOptionsDialog(getContext(), categoryAdapter, position,
                        // update = refresh selected day's data
                        () -> refreshCategoryList(),
                        // open edit dialog
                        () -> showNewCategoryDialog(true));
                return true;
            }
        });


        hourSelector = binding.timePicker;
        hourSelector.setWrapSelectorWheel(false);

        // reverse order of hours
        String[] values = new String[24];
        for (int i = 0; i < values.length; i++) {
            values[i] = String.valueOf(23 - i);
        }
        hourSelector.setMinValue(0);
        hourSelector.setMaxValue(23);
        hourSelector.setDisplayedValues(values);

        if (emotionToEdit == null) {
            // set current hour
            hourSelector.setValue(getHourValue(LocalTime.now().getHour()));
        }
        // EDITING
        else {
            editTextDescription.setText(emotionToEdit.emotion.description);

            selectedScale.setText(emotionToEdit.emotion.scaleName);
            selectedScaleIcon.setBackgroundColor(emotionToEdit.emotion.scaleColor);

            hourSelector.setValue(getHourValue(emotionToEdit.emotion.hour));

            // fill in selected category values
            //   category ref texts are done in CategoryArrayAdapter
            refreshCategoryList();
            for (E_Value selectedValue : emotionToEdit.values) {
                Optional<FullCategory> category = categoryList.stream()
                        .filter(fullCategory -> fullCategory.category.categoryID == selectedValue.F_Category)
                        .findFirst();

                if (category.isPresent()) {
                    CategoryArrayAdapter.selectedValueAtCategoryName.put(category.get().category.name, selectedValue);
                }
            }
        }

        new ScaleSpinner(
                getActivity(),
                selectedScale,
                getString(R.string.select_scale),
                getString(R.string.scale),
                selectedScaleIcon,
                () -> MainActivity.database.daoScale().list(),
                item -> EmotionCreateFragment.chosenScale = item
        );


        View addCategoryButton = binding.addButton;
        addCategoryButton.setOnClickListener(v -> {
            showNewCategoryDialog(false);
        });


        // hide (+) FAB
        try {
            ((MainActivity) requireActivity()).binding.fabAddEvent.hide();
            ((MainActivity) requireActivity()).binding.fabAddFeeling.hide();
        } catch (IllegalStateException | NullPointerException ignored) {
        }
    }


    //////////////////

    private void insertNewEmotion() {
        // insert new emotion from provided data; ID for category value cross refs
        long emotionID = MainActivity.database.daoEmotion().insert(
                new E_Emotion(editTextDescription.getText().toString(),
                        getHourValue(),
                        MainActivity.selectedDayDateSort,
                        chosenScale == null ? -1 : chosenScale.scaleID));

        insertCategoryRefsToEmotion(emotionID);
    }

    private void insertCategoryRefsToEmotion(long emotionID) {
        // refresh categories for any category/value changes
        refreshCategoryList();

        // many-to-many refs - emotion to category values
        for (FullCategory fullCategory : categoryList) {
            // get selected value name and find it in FullCategory list
            E_Value selectedValue = CategoryArrayAdapter.selectedValueAtCategoryName.get(fullCategory.category.name);

            // if value selected, save it to DB, else do nothing
            if (selectedValue != null) {
                MainActivity.database.daoEmotion().insertEmotionCategoryRef(
                        new EmotionValueCrossRef(
                                emotionID,
                                selectedValue.valueID
                        )
                );
            }
        }
    }

    private void updateEmotion() {
        E_Emotion emotion = MainActivity.database.daoEmotion().get(
                emotionToEdit.emotion.emotionID);

        emotion.description = editTextDescription.getText().toString();
        emotion.hour = getHourValue();
        emotion.dateSort = MainActivity.selectedDayDateSort;
        emotion.F_scale = chosenScale == null ? -1 : chosenScale.scaleID;

        // delete cross refs to recreate them
        MainActivity.database.daoEmotion().deleteAllEmotionCategoryRefsByEmotionID(emotion.emotionID);
        insertCategoryRefsToEmotion(emotion.emotionID);

        MainActivity.database.daoEmotion().update(emotion);
    }


    private boolean isEmotionDataValid() {
        return true;
    }


    /////////////////////////

    @Override
    protected int getFragmentTitle() {
        return emotionToEdit == null ? R.string.title_new_emotion : R.string.title_edit_emotion;
    }

    private void refreshCategoryList() {
        categoryList.clear();
        categoryList.addAll(MainActivity.database.daoCategory().listFull());
    }

    protected void showNewCategoryDialog(boolean edit) {
        if (!edit) {
            categoryToEdit = null;
        }

        if (addNewDialog != null)
            addNewDialog.dismiss();

        addNewDialog = new Dialog(getContext());
        addNewDialog.setContentView(R.layout.dialog_add_option_name);
        addNewDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView header = addNewDialog.findViewById(R.id.header);
        header.setText(getContext().getString(edit ? R.string.edit_existing : R.string.add_new, getContext().getString(R.string.category)));

        Button addButton = addNewDialog.findViewById(R.id.addButton);
        addButton.setOnClickListener(
                categoryConfirmButtonOnClick(addButton)
        );
        addButton.setText(edit ? R.string.edit : R.string.add);

        addNewDialog.show();
    }

    protected View.OnClickListener categoryConfirmButtonOnClick(Button addButton) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCategoryDataValid()) {
                    // item edit - updateDB
                    if (categoryToEdit != null) {
                        updateCategoryDB(categoryToEdit);
                    }
                    // new item - insertDB
                    else {
                        insertNewCategoryDB();
                    }

                    // close dialog
                    updateData();
                    addNewDialog.dismiss();
                } else {
                    addButton.setBackgroundColor(Color.RED);

                    Animation anim = new AlphaAnimation(0, 1);
                    anim.setDuration(30);
                    anim.setRepeatCount(3);
                    anim.setRepeatMode(Animation.REVERSE);
                    addButton.startAnimation(anim);

                    addButton.postOnAnimationDelayed(() -> {
                        addButton.setBackgroundColor(getResources().getColor(
                                com.google.android.material.R.color.design_default_color_primary,
                                getContext().getTheme()));
                    }, 250);
                }
            }
        };
    }

    private void updateData() {
        refreshCategoryList();

        binding.noCategories.setVisibility(categoryList.isEmpty() ? View.VISIBLE : View.GONE);

        if (categoryListView != null)
            ((CategoryArrayAdapter) categoryListView.getAdapter()).notifyDataSetChanged();
    }

    private void insertNewCategoryDB() {
        EditText nameText = addNewDialog.findViewById(R.id.edit_text);

        // collect and insert data object
        MainActivity.database.daoCategory().insert(
                new E_Category(nameText.getText().toString()));
    }

    private void updateCategoryDB(FullCategory category) {
        EditText nameText = addNewDialog.findViewById(R.id.edit_text);

        E_Category e_category = MainActivity.database.daoCategory().get(category.category.categoryID);
        e_category.name = nameText.getText().toString();

        MainActivity.database.daoCategory().update(e_category);
    }

    private boolean isCategoryDataValid() {
        EditText nameText = addNewDialog.findViewById(R.id.edit_text);
        return !nameText.getText().toString().isEmpty();
    }

    public int getHourValue() {
        return getHourValue(hourSelector.getValue());
    }

    public int getHourValue(int val) {
        return 23 - val;
    }
}