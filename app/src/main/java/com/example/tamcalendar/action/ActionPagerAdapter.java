package com.example.tamcalendar.action;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.tamcalendar.CalendarFragment;

public class ActionPagerAdapter extends FragmentStateAdapter {

    public ActionPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
    }

    public ActionPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ActionDetailFragment();
            case 1:
                return new ActionEditFragment();
            case 2:
                return new ActionDeleteFragment();
            default:
                return new CalendarFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
