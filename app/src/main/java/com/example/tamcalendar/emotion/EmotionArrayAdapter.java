package com.example.tamcalendar.emotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.emotion.FullEmotionData;

import java.util.List;
import java.util.function.BiFunction;

public class EmotionArrayAdapter extends ArrayAdapter<FullEmotionData> {

    private final Runnable onChange;
    private final BiFunction<Integer, Integer, String> getTimeRefString;

    public EmotionArrayAdapter(@NonNull Context context, List<FullEmotionData> originalItems,
                               Runnable onChange, BiFunction<Integer, Integer, String> getTimeRefString) {
        super(context, android.R.layout.simple_list_item_1, originalItems);
        this.onChange = onChange;
        this.getTimeRefString = getTimeRefString;
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
        FullEmotionData item = getItem(position);

        TextView textView = v.findViewById(R.id.name);
        textView.setText(getTimeRefString.apply(R.string.time_ref, item.hour));

        View scaleColorIcon = v.findViewById(R.id.scaleColorIcon);
        scaleColorIcon.setBackgroundColor(item.scaleColor);

        TextView scaleText = v.findViewById(R.id.scaleText);
        scaleText.setText(item.scaleName);

        // fill in child category values
        //TODO


        return v;
    }
}
