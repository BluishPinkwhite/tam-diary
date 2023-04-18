package com.example.tamcalendar.emotion;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Category;

import java.util.List;

public class CategoryArrayAdapter extends ArrayAdapter<DAO_Category.FullCategory> {

    public CategoryArrayAdapter(@NonNull Context context, List<DAO_Category.FullCategory> originalItems) {
        super(context, android.R.layout.simple_list_item_1, originalItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.category_list_item, null);

        TextView header = v.findViewById(R.id.label);
        header.setText(getItem(position).category.name);

        TextView selectText = v.findViewById(R.id.selectedThing);
        selectText.setHint(getContext().getString(R.string.select_ref, getItem(position).category.name));

        return v;
    }
}
