package com.example.tamcalendar.emotion;

import android.content.Context;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.emotion.FullEmotionData;
import com.example.tamcalendar.views.OptionsDialog;

public class EmotionOptionsDialog extends OptionsDialog<FullEmotionData> {

    public EmotionOptionsDialog(Context context, EmotionArrayAdapter adapter, int position, Runnable invalidate, Runnable navigateToCreateFrag) {
        super(context, adapter, position, invalidate, navigateToCreateFrag);
    }

    protected void deleteItemDB(FullEmotionData item) {
        MainActivity.database.daoEmotion().deleteByID(item.ID);
    }

    protected void prepareItemEdit(FullEmotionData item) {
        EmotionCreateFragment.emotionToEdit = item;
        EmotionCreateFragment.chosenScale = MainActivity.database.daoScale().getByName(item.scaleName);
    }
}
