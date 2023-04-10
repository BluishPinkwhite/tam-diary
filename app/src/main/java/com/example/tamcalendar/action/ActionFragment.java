package com.example.tamcalendar.action;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.databinding.FragmentActionBinding;
import com.google.android.material.tabs.TabLayout;

public class ActionFragment extends Fragment {

    FragmentActionBinding binding;

    ActionPagerAdapter pagerAdapter;

    TabLayout tabLayout;
    ViewPager2 viewPager;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentActionBinding.inflate(inflater, container, false);

        tabLayout = binding.getRoot().findViewById(R.id.actionTabLayout);
        viewPager = binding.getRoot().findViewById(R.id.pager);
        pagerAdapter = new ActionPagerAdapter(this);


        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });

        tabLayout.getTabAt(1).select();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // hide MainActivity FAButton on create
        ((MainActivity)getActivity()).binding.fab.hide();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
