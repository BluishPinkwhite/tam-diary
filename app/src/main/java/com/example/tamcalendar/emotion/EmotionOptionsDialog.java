package com.example.tamcalendar.emotion;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.DAO_Emotion;
import com.example.tamcalendar.views.OptionsDialog;

public class EmotionOptionsDialog extends OptionsDialog<DAO_Emotion.FullEmotionData> {

    public EmotionOptionsDialog(Context context, ArrayAdapter<DAO_Emotion.FullEmotionData> adapter, int position, Runnable invalidate, Runnable navigateToCreateFrag) {
        super(context, adapter, position, invalidate, navigateToCreateFrag);
    }

    protected void deleteItemDB(DAO_Emotion.FullEmotionData item) {
        MainActivity.database.daoEmotion().deleteByID(item.ID);
    }

    protected void prepareItemEdit(DAO_Emotion.FullEmotionData item) {
        EmotionCreateFragment.emotionToEdit = item;
        EmotionCreateFragment.chosenScale = MainActivity.database.daoScale().getByName(item.scaleName);
    }
}
