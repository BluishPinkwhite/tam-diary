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
import android.widget.ArrayAdapter;
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
import com.example.tamcalendar.data.DAO_Category;
import com.example.tamcalendar.data.DAO_Emotion;
import com.example.tamcalendar.data.E_Category;
import com.example.tamcalendar.data.E_Scale;
import com.example.tamcalendar.databinding.FragmentEmotionCreateBinding;
import com.example.tamcalendar.spinner.ScaleSpinner;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EmotionCreateFragment extends FragmentBase {

    FragmentEmotionCreateBinding binding;
    TextView selectedScale, editTextDescription;
    View selectedScaleIcon;
    Button confirmButton;
    NumberPicker hourSelector;

    ListView categoryListView;
    List<DAO_Category.FullCategory> categoryList;

    Dialog addNewDialog;


    // refs used to edit self (or create new emotion)
    public static E_Scale chosenScale;
    public static DAO_Category.FullCategory categoryToEdit;
    public static DAO_Emotion.FullEmotionData emotionToEdit;

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
            //if (editTextEventName.getText().length() > 0)
            {
                /*/ new action
                if (actionToEdit == null) {
                    insertNewAction();
                }
                // edit action
                else {
                    updateAction();
                }

                 */

                // navigate back to calendar
                NavHostFragment.findNavController(EmotionCreateFragment.this)
                        .navigate(R.id.action_emotion_create_to_Calendar);
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

        // set current hour
        hourSelector.setValue(23 - LocalTime.now().getHour());

        /*
        if (actionToEdit != null) {
            editTextDescription.setText(actionToEdit.description);

            selectedScale.setText(actionToEdit.scaleName);
            selectedScaleIcon.setBackgroundColor(actionToEdit.scaleColor);
        }
         */

        new ScaleSpinner(
                getActivity(),
                selectedScale,
                getString(R.string.select_scale),
                getString(R.string.scale),
                selectedScaleIcon,
                () -> MainActivity.database.daoScale().list()
        );


        // category setup
        categoryListView = binding.categoryList;
        categoryList = new ArrayList<>();
        ArrayAdapter<DAO_Category.FullCategory> categoryAdapter = new CategoryArrayAdapter(getContext(), categoryList);
        categoryListView.setAdapter(categoryAdapter);

        categoryListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return true;
            }
        });


        updateData();

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

        ((CategoryArrayAdapter) categoryListView.getAdapter()).notifyDataSetChanged();
    }

    private void insertNewCategoryDB() {
        EditText nameText = addNewDialog.findViewById(R.id.edit_text);

        // collect and insert data object
        MainActivity.database.daoCategory().insert(
                new E_Category(nameText.getText().toString()));
    }

    private void updateCategoryDB(DAO_Category.FullCategory category) {
        EditText nameText = addNewDialog.findViewById(R.id.edit_text);

        E_Category e_category = MainActivity.database.daoCategory().get(category.category.ID);
        e_category.name = nameText.getText().toString();

        MainActivity.database.daoCategory().update(e_category);
    }

    private boolean isCategoryDataValid() {
        EditText nameText = addNewDialog.findViewById(R.id.edit_text);
        return !nameText.getText().toString().isEmpty();
    }
}