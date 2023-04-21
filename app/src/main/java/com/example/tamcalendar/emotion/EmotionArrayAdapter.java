package com.example.tamcalendar.emotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Emotion;

import java.util.List;

public class EmotionArrayAdapter extends ArrayAdapter<DAO_Emotion.FullEmotionData> {

    private Context context;
    private Runnable onChange;

    public EmotionArrayAdapter(@NonNull Context context, List<DAO_Emotion.FullEmotionData> originalItems,
                               Runnable onChange) {
        super(context, android.R.layout.simple_list_item_1, originalItems);

        this.context = context;
        this.onChange = onChange;
    }

    @Override
    public void notifyDataSetChanged() {
        onChange.run();

        super.notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.emotion_compact_row, null);
        DAO_Emotion.FullEmotionData item = getItem(position);

        TextView textView = v.findViewById(R.id.name);
        textView.setText(context.getString(R.string.time_ref, item.hour));

        View scaleColorIcon = v.findViewById(R.id.scaleColorIcon);
        scaleColorIcon.setBackgroundColor(item.scaleColor);

        TextView scaleText = v.findViewById(R.id.scaleText);
        scaleText.setText(item.scaleName);

        // fill in child category values
        //TODO


        return v;
    }
}
