package com.example.tamcalendar.action;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.databinding.FragmentActionCreateBinding;
import com.example.tamcalendar.spinner.ActorSpinner;
import com.example.tamcalendar.spinner.ScaleSpinner;

public class ActionCreateFragment extends Fragment {

    FragmentActionCreateBinding binding;
    TextView selectedActor, selectedScale;
    View selectedActorIcon, selectedScaleIcon;

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


        new ActorSpinner(
                getActivity(),
                selectedActor,
                getString(R.string.select_actor),
                getString(R.string.add_new_actor),
                selectedActorIcon,
                () -> MainActivity.database.daoActor().list()
        );

        new ScaleSpinner(
                getActivity(),
                selectedScale,
                getString(R.string.select_scale),
                getString(R.string.add_new_scale),
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