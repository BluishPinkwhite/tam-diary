package com.example.tamcalendar.emotion;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;
import com.example.tamcalendar.views.OptionsDialog;

public class EmotionOptionsDialog extends OptionsDialog<EmotionWithCategories> {

    public EmotionOptionsDialog(Context context, ArrayAdapter<EmotionWithCategories> adapter, int position, Runnable invalidate, Runnable navigateToCreateFrag) {
        super(context, adapter, position, invalidate, navigateToCreateFrag);
    }

    protected void deleteItemDB(EmotionWithCategories item) {
        MainActivity.database.daoEmotion().deleteByID(item.emotion.emotionID);

        // delete all refs pointing to this emotion
        MainActivity.database.daoEmotion().deleteAllEmotionCategoryRefsByEmotionID(item.emotion.emotionID);
    }

    protected void prepareItemEdit(EmotionWithCategories item) {
        EmotionCreateFragment.emotionToEdit = item;
        EmotionCreateFragment.chosenScale = MainActivity.database.daoScale().getByName(item.emotion.scaleName);
    }
}
