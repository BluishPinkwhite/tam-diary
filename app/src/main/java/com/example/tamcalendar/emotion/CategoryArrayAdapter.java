package com.example.tamcalendar.emotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;
import com.example.tamcalendar.data.category.FullCategory;
import com.example.tamcalendar.data.value.E_Value;
import com.example.tamcalendar.spinner.ValueSpinner;

import java.util.List;

public class CategoryArrayAdapter extends ArrayAdapter<FullCategory> {

    public CategoryArrayAdapter(@NonNull Context context, List<FullCategory> originalItems) {
        super(context, android.R.layout.simple_list_item_1, originalItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.category_list_item, null);
        FullCategory item = getItem(position);

        TextView header = v.findViewById(R.id.label);
        //header.setText(item.category.name);

        TextView selectText = v.findViewById(R.id.selectedThing);
        selectText.setHint(getContext().getString(R.string.select_ref, item.category.name));

        View colorIcon = v.findViewById(R.id.colorIcon);

        // EDIT
        if (EmotionCreateFragment.emotionToEdit != null) {
            // fill in values of categories
            for (E_Value value :
                    EmotionCreateFragment.emotionToEdit.values) {
                if (value.F_Category == item.category.categoryID) {
                    EmotionArrayAdapter.fillValueText(
                            header,
                            colorIcon,
                            selectText,
                            item.category, value);
                    break;
                }
            }
        }


        //
        new ValueSpinner(
                getContext(),
                selectText,
                getContext().getString(R.string.select_ref, item.category.name),
                item.category.name,
                colorIcon,
                () -> MainActivity.database.daoValue().listByCategory(item.category.categoryID),
                item
        );

        return v;
    }
}
