package com.example.tamcalendar.spinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tamcalendar.R;

import java.util.ArrayList;

public class ColorArrayAdapter<T extends ColorNameable> extends ArrayAdapter<T> {

    private ArrayList<T> originalItems;
    private ArrayList<T> filteredItems;
    private Filter filter;

    public ColorArrayAdapter(@NonNull Context context, @NonNull ArrayList<T> objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
        this.originalItems = new ArrayList<>(objects);
        this.filteredItems = new ArrayList<>(objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.spinner_color_row, null);

        TextView textView = v.findViewById(R.id.text);
        System.out.println(position);
        textView.setText(filteredItems.get(position).name);

        View colorIcon = v.findViewById(R.id.colorIcon);
        // TODO get from DAO
        colorIcon.setBackgroundColor(filteredItems.get(position).color);

        return v;
    }

    @Override
    public int getCount() {
        return filteredItems.size();
    }

    @Nullable
    @Override
    public T getItem(int position) {
        return filteredItems.get(position);
    }

    @Override
    public Filter getFilter() {
        if (filter == null)
            filter = new NameFilter();

        return filter;
    }

    private class NameFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0) {
                ArrayList<T> list = new ArrayList<>(originalItems);
                results.values = list;
                results.count = list.size();
            } else {
                final ArrayList<T> list = new ArrayList<>(originalItems);
                final ArrayList<T> nlist = new ArrayList<>();
                int count = list.size();

                for (int i = 0; i < count; i++) {
                    final T item = list.get(i);
                    final String value = item.name.toLowerCase();

                    if (value.startsWith(prefix)) {
                        nlist.add(item);
                    }
                }
                results.values = nlist;
                results.count = nlist.size();
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems = (ArrayList<T>) results.values;

            clear();
            int count = filteredItems.size();
            for (int i = 0; i < count; i++) {
                T item = (T) filteredItems.get(i);
                add(item);
            }
        }

    }
}
