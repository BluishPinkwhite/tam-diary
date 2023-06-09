package com.example.tamcalendar.action;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tamcalendar.R;
import com.example.tamcalendar.data.action.FullActionData;

import java.util.List;

public class ActionArrayAdapter extends ArrayAdapter<FullActionData> {

    private Runnable onChange;

    public ActionArrayAdapter(@NonNull Context context, List<FullActionData> originalItems,
                              Runnable onChange) {
        super(context, android.R.layout.simple_list_item_1, originalItems);
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
        v = inflater.inflate(R.layout.action_compact_row, null);
        FullActionData actionData = getItem(position);

        TextView textView = v.findViewById(R.id.name);
        textView.setText(actionData.name +
                (actionData.description != null && !actionData.description.isEmpty() ? " (+)" : ""));


        View actorColorIcon = v.findViewById(R.id.actorColorIcon);
        actorColorIcon.setBackgroundColor(actionData.actorColor);

        TextView actorText = v.findViewById(R.id.actorText);
        actorText.setText(actionData.actorName);


        View scaleColorIcon = v.findViewById(R.id.scaleColorIcon);
        scaleColorIcon.setBackgroundColor(actionData.scaleColor);

        TextView scaleText = v.findViewById(R.id.scaleText);
        scaleText.setText(actionData.scaleName);

        return v;
    }
}
