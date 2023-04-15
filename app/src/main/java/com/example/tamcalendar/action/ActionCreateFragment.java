package com.example.tamcalendar.action;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DatabaseManager;
import com.example.tamcalendar.data.E_Action;
import com.example.tamcalendar.data.E_Actor;
import com.example.tamcalendar.data.E_Scale;
import com.example.tamcalendar.databinding.FragmentActionCreateBinding;
import com.example.tamcalendar.spinner.ActorSpinner;
import com.example.tamcalendar.spinner.ScaleSpinner;

import java.time.LocalDate;

public class ActionCreateFragment extends Fragment {

    FragmentActionCreateBinding binding;
    TextView selectedActor, selectedScale, editTextEventName, editTextDescription;
    View selectedActorIcon, selectedScaleIcon;
    Button confirmButton;

    public static E_Actor chosenActor;
    public static E_Scale chosenScale;

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
        editTextEventName = binding.getRoot().findViewById(R.id.editTextEventName);
        editTextDescription = binding.getRoot().findViewById(R.id.description);

        confirmButton = binding.getRoot().findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            // valid data
            if (chosenActor != null &&
                    chosenScale != null &&
                    editTextEventName.getText().length() > 0 &&
                    editTextDescription.getText().length() > 0) {

                LocalDate chosenDate = DatabaseManager.fromDateSort(MainActivity.selectedDayDateSort);

                // collect and insert data object
                MainActivity.database.daoAction().insert(
                        new E_Action(editTextEventName.getText().toString(),
                                editTextDescription.getText().toString(),
                                chosenDate.getYear(),
                                chosenDate.getMonthValue(),
                                chosenDate.getDayOfMonth(),
                                MainActivity.selectedDayDateSort,
                                chosenActor.ID,
                                chosenScale.ID)
                );

                // navigate back to calendar
                NavHostFragment.findNavController(ActionCreateFragment.this)
                        .navigate(R.id.action_actionCreate_to_CalendarFragment);
            }
        });


        new ActorSpinner(
                getActivity(),
                selectedActor,
                getString(R.string.select_actor),
                getString(R.string.actor),
                selectedActorIcon,
                () -> MainActivity.database.daoActor().list()
        );

        new ScaleSpinner(
                getActivity(),
                selectedScale,
                getString(R.string.select_scale),
                getString(R.string.scale),
                selectedScaleIcon,
                () -> MainActivity.database.daoScale().list()
        );

        // hide (+) FAB
        try {
            ((MainActivity) requireActivity()).binding.fab.hide();
        } catch (IllegalStateException | NullPointerException ignored) {
        }
    }
}