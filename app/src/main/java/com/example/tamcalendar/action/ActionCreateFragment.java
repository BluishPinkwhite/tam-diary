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
import com.example.tamcalendar.data.E_Actor;
import com.example.tamcalendar.databinding.FragmentActionCreateBinding;
import com.example.tamcalendar.spinner.SearchableColorNameSpinner;

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


        // TODO get from actor DAO
        /*actorOptions = new ArrayList<>(Arrays.asList(
                new E_Actor("Jose", 0xFFFF2244),
                new E_Actor("Tam", 0xFF44DDD3),
                new E_Actor("Koleno", 0xFF0997CC),
                new E_Actor("Poleno", 0xFF33DC56)
        ));
         */

        // create dialog on selectedActor click to select option
        //searchableSpinnerSetup(selectedActor, selectedActorIcon, new ArrayList<E_Actor>(),
        //        addPopupSetup());getContext().getString(R.string.select_actor)
        new SearchableColorNameSpinner<E_Actor>(
                getActivity(),
                selectedActor,
                getString(R.string.select_actor),
                selectedActorIcon,
                () -> MainActivity.database.daoActor().list()
        );


        /*/ TODO get from scale DAO
        searchableSpinnerSetup(selectedScale, selectedScaleIcon, new ArrayList<>(Arrays.asList(
                new E_Actor("Pure joy", 0xFF33DC56),
                new E_Actor("Good", 0xFF44DDD3),
                new E_Actor("Weird", 0xFF0997CC),
                new E_Actor("Bad", 0xFFFF2244)
        )));


         */

        // hide (+) FAB
        try {
            ((MainActivity) requireActivity()).binding.fab.hide();
        } catch (IllegalStateException | NullPointerException ignored) {
        }
    }
}