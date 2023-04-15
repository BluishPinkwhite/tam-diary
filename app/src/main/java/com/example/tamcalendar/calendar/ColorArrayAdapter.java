package com.example.tamcalendar.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.spinner.ColorNameHaver;

import java.util.List;

public class ColorArrayAdapter<T extends ColorNameHaver> extends ArrayAdapter<T> {

    public ColorArrayAdapter(@NonNull Context context, List<T> originalItems) {
        super(context, android.R.layout.simple_list_item_1, originalItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.spinner_color_row, null);

        TextView textView = v.findViewById(R.id.text);
        textView.setText(getItem(position).name);

        View colorIcon = v.findViewById(R.id.colorIcon);
        colorIcon.setBackgroundColor(getItem(position).color);

        return v;
    }
}
