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
import com.example.tamcalendar.calendar.CalendarFragment;
import com.example.tamcalendar.data.category.E_Category;
import com.example.tamcalendar.data.emotion.EmotionWithCategories;
import com.example.tamcalendar.data.value.E_Value;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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


        CalendarFragment.refreshAllCategoriesIDMap();
        fillCategoryDisplayValuesToContainer(getContext(), v.findViewById(R.id.childContainer), item);

        return v;
    }

    public static void fillCategoryDisplayValuesToContainer(Context context, LinearLayout valueContainer, EmotionWithCategories item) {

        // alphabetical order of category names
        List<E_Category> categories = item.values.stream()
                .map(e_value -> CalendarFragment.allCategoriesByID.get(e_value.F_Category))
                .sorted(Comparator.comparing(e_category -> e_category != null ? e_category.name : ""))
                .collect(Collectors.toList());

        // fill in child category values
        for (int i = 0; i < categories.size(); i++) {
            E_Category category = categories.get(i);
            if (category == null) continue;

            Optional<E_Value> value = item.values.stream()
                    .filter(e_value -> e_value.F_Category == category.categoryID)
                    .findFirst();

            View valueDisplay = View.inflate(context, R.layout.emotion_display_compact_row, null);

            fillValueText(
                    valueDisplay.findViewById(R.id.name),
                    valueDisplay.findViewById(R.id.scaleColorIcon),
                    valueDisplay.findViewById(R.id.scaleText),
                    category, value.get());

            valueContainer.addView(valueDisplay);
        }
    }

    public static void fillValueText(TextView label, View colorIcon, TextView selectedName,
                                     E_Category category, E_Value value) {
        if (label != null)
            label.setText(category != null ? category.name : "[removed]");

        colorIcon.setBackgroundColor(value.color);
        selectedName.setText(value.name);
    }
}
