package com.example.tamcalendar.emotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;
import com.example.tamcalendar.data.value.E_Value;

import java.util.List;
import java.util.function.BiFunction;

public class EmotionArrayAdapter extends ArrayAdapter<EmotionWithCategories> {

    private final Runnable onChange;
    private final BiFunction<Integer, Integer, String> getTimeRefString;

    public EmotionArrayAdapter(@NonNull Context context, List<EmotionWithCategories> originalItems,
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
        EmotionWithCategories item = getItem(position);

        // set text and color
        TextView textView = v.findViewById(R.id.name);
        textView.setText(getTimeRefString.apply(R.string.time_ref, item.emotion.hour));

        View scaleColorIcon = v.findViewById(R.id.scaleColorIcon);
        scaleColorIcon.setBackgroundColor(item.emotion.scaleColor);

        TextView scaleText = v.findViewById(R.id.scaleText);
        scaleText.setText(item.emotion.scaleName);


        // fill in child category values
        LinearLayout valueContainer = v.findViewById(R.id.childContainer);
        for (E_Value value :
                item.values) {
            View valueDisplay = inflater.inflate(R.layout.emotion_display_compact_row, null);

            TextView name = valueDisplay.findViewById(R.id.name);
            // TODO
            name.setText(value.F_Category + " category");

            View colorIcon = valueDisplay.findViewById(R.id.scaleColorIcon);
            colorIcon.setBackgroundColor(value.color);

            TextView scaleName = valueDisplay.findViewById(R.id.scaleText);
            scaleName.setText(value.name);

            valueContainer.addView(valueDisplay);
        }

        return v;
    }
}
