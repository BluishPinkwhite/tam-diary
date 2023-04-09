package com.example.tamcalendar.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.Map;

public class TamCalendarViewModel extends ViewModel {

    private MutableLiveData<Map<Integer, List<E_Action>>> currentActionBetweenMap;

    public MutableLiveData<Map<Integer, List<E_Action>>> getActionBetweenMap() {
        if (currentActionBetweenMap == null) {
            currentActionBetweenMap = new MutableLiveData<>();
        }
        return currentActionBetweenMap;
    }
}
