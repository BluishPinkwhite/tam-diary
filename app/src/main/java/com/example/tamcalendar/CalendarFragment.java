package com.example.tamcalendar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.example.tamcalendar.databinding.FragmentCalendarBinding;

public class CalendarFragment extends Fragment {

    private FragmentCalendarBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // show MainActivity FAButton on create
        getActivity().getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onCreate(@NonNull LifecycleOwner owner) {
                DefaultLifecycleObserver.super.onCreate(owner);
                ((MainActivity)getActivity()).binding.fab.show();
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

}