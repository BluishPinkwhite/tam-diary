package com.example.tamcalendar.action;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.E_Actor;
import com.example.tamcalendar.databinding.FragmentActionCreateBinding;
import com.example.tamcalendar.spinner.ColorArrayAdapter;
import com.example.tamcalendar.spinner.ColorNameable;

import java.util.ArrayList;
import java.util.Arrays;

import top.defaults.colorpicker.ColorPickerPopup;

public class ActionCreateFragment extends Fragment {

    FragmentActionCreateBinding binding;
    TextView selectedActor, selectedScale;
    View selectedActorIcon, selectedScaleIcon;
    ArrayList<E_Actor> actorOptions;
    Dialog dialog;
    Dialog dialogAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActionCreateBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedActor = binding.getRoot().findViewById(R.id.selectedActor);
        selectedScale = binding.getRoot().findViewById(R.id.selectedScale);

        selectedActorIcon = binding.getRoot().findViewById(R.id.colorIconActor);
        selectedScaleIcon = binding.getRoot().findViewById(R.id.colorIconScale);


        // TODO get from actor DAO
        actorOptions = new ArrayList<>(Arrays.asList(
                new E_Actor("Jose", 0xFFFF2244),
                new E_Actor("Tam", 0xFF44DDD3),
                new E_Actor("Koleno", 0xFF0997CC),
                new E_Actor("Poleno", 0xFF33DC56)
        ));

        // create dialog on selectedActor click to select option
        searchableSpinnerSetup(selectedActor, selectedActorIcon, actorOptions,
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAdd = new Dialog(getActivity());
                        dialogAdd.setContentView(R.layout.dialog_add_option);
                        dialogAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                        TextView header = dialogAdd.findViewById(R.id.header);
                        header.setText(R.string.add_new_actor);

                        View colorPreview = dialogAdd.findViewById(R.id.color_preview);
                        colorPreview.setOnClickListener(colorPreviewView -> {
                            ColorPickerPopup colorPickerPopup = new ColorPickerPopup.Builder(getContext())
                                    .initialColor(((ColorDrawable) (colorPreview.getBackground())).getColor())
                                    .enableBrightness(false)
                                    .enableAlpha(false)
                                    .okTitle(getString(R.string.select))
                                    .cancelTitle(getString(R.string.cancel))
                                    .showIndicator(true)
                                    .showValue(false)
                                    .build();
                            colorPickerPopup.show(colorPreviewView, new ColorPickerPopup.ColorPickerObserver() {
                                @Override
                                public void onColorPicked(int color) {
                                    colorPreview.setBackgroundColor(color);
                                }
                            });
                        });

                        Button addButton = dialogAdd.findViewById(R.id.addButton);
                        addButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (!((EditText)(dialogAdd.findViewById(R.id.edit_text))).getText().toString().isEmpty()) {
                                    colorPreview.setBackgroundColor(0x00FF00);
                                }
                                else colorPreview.setBackgroundColor(0xFF0000);
                            }
                        });

                        dialogAdd.show();
                    }
                });


        /*/ TODO get from scale DAO
        searchableSpinnerSetup(selectedScale, selectedScaleIcon, new ArrayList<>(Arrays.asList(
                new E_Actor("Pure joy", 0xFF33DC56),
                new E_Actor("Good", 0xFF44DDD3),
                new E_Actor("Weird", 0xFF0997CC),
                new E_Actor("Bad", 0xFFFF2244)
        )));


         */

        // hide FAB
        try {
            ((MainActivity) requireActivity()).binding.fab.hide();
        }
        catch (IllegalStateException | NullPointerException ignored) {}
    }

    private void searchableSpinnerSetup(TextView spinner, View colorIcon, ArrayList<? extends ColorNameable> options, View.OnClickListener addButtonFunc) {
        spinner.setOnClickListener(selActor -> {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_searchable_spinner);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            dialog.show();

            // set header text
            TextView header = dialog.findViewById(R.id.header);
            header.setText(getString(R.string.select_actor));

            // add options to list view
            ListView listView = dialog.findViewById(R.id.list_view);
            ColorArrayAdapter<? extends ColorNameable> adapter = new ColorArrayAdapter<>(getContext(), options);
            listView.setAdapter(adapter);

            // option select setup
            EditText editText = dialog.findViewById(R.id.edit_text);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    spinner.setText(adapter.getItem(position).name);
                    colorIcon.setBackgroundColor(adapter.getItem(position).color);
                    dialog.dismiss();
                }
            });

            // add dialog
            View addButton = dialog.findViewById(R.id.addButton);
            addButton.setOnClickListener(addButtonFunc);
        });
    }
}