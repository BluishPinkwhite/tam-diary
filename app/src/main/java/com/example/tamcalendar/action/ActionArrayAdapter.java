package com.example.tamcalendar.action;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.DAO_Action;

import java.util.List;

public class ActionArrayAdapter extends ArrayAdapter<DAO_Action.FullActionData> {

    public ActionArrayAdapter(@NonNull Context context, List<DAO_Action.FullActionData> originalItems) {
        super(context, android.R.layout.simple_list_item_1, originalItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.action_compact_row, null);

        TextView textView = v.findViewById(R.id.name);
        textView.setText(getItem(position).name);


        View actorColorIcon = v.findViewById(R.id.actorColorIcon);
        actorColorIcon.setBackgroundColor(getItem(position).actorColor);

        TextView actorText = v.findViewById(R.id.actorText);
        actorText.setText(getItem(position).actorName);


        View scaleColorIcon = v.findViewById(R.id.scaleColorIcon);
        scaleColorIcon.setBackgroundColor(getItem(position).scaleColor);

        TextView scaleText = v.findViewById(R.id.scaleText);
        scaleText.setText(getItem(position).scaleName);

        return v;
    }
}
