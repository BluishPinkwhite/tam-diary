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
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.databinding.FragmentActionCreateBinding;

import java.util.ArrayList;
import java.util.Arrays;

public class ActionCreateFragment extends Fragment {

    FragmentActionCreateBinding binding;
    TextView selectedActor, selectedScale;
    ArrayList<String> actorOptions;
    Dialog dialog;

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


        // TODO get from actor DAO
        actorOptions = new ArrayList<>();
        actorOptions.addAll(Arrays.asList("Jose", "Tam", "Koleno"));

        // create dialog on selectedActor click to select option
        searchableSpinnerSetup(selectedActor, actorOptions);
        // TODO get from scale DAO
        searchableSpinnerSetup(selectedScale, new ArrayList<>(Arrays.asList("Pure joy", "Good", "Bad")));


        // hide FAB
        ((MainActivity)getActivity()).binding.fab.hide();
    }

    private void searchableSpinnerSetup(TextView spinner, ArrayList<String> options) {
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
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, options);
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
                    spinner.setText(adapter.getItem(position));
                    dialog.dismiss();
                }
            });
        });
    }
}