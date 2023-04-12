package com.example.tamcalendar.spinner;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tamcalendar.R;

import java.util.ArrayList;
import java.util.List;

public abstract class SearchableSpinner<T> extends View {

    protected TextView parentSpinner; // the text view the user clicks on to open this spinner dialog
    protected ArrayAdapter<T> adapter;
    protected Dialog dialog;
    protected ListView listView;
    protected View addButton;

    protected List<T> objects = new ArrayList<>();

    public SearchableSpinner(Context context, TextView parentSpinner, String headerText) {
        super(context);
        this.parentSpinner = parentSpinner;

        searchableSpinnerSetup(headerText);
    }

    private void searchableSpinnerSetup(String headerText) {
        parentSpinner.setOnClickListener(selActor -> {
            dialog = new Dialog(getContext());
            dialog.setContentView(R.layout.dialog_searchable_spinner);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            // set header text
            TextView header = dialog.findViewById(R.id.header);
            header.setText(headerText);

            // add options to list view
            listView = dialog.findViewById(R.id.list_view);
            adapter = createListAdapter();
            listView.setAdapter(adapter);
            updateData();

            // option select setup - "Search" edit text
            EditText editText = dialog.findViewById(R.id.edit_text);
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    adapter.getFilter().filter(s);
                }

                @Override
                public void afterTextChanged(Editable editable) {
                }
            });

            listView.setOnItemClickListener(
                    createOnListItemClickListener()
            );

            // add dialog
            addButton = dialog.findViewById(R.id.addButton);
            if (this instanceof SearchableSpinnerWithAdd) {
                addButton.setOnClickListener(
                        createAddButtonClickListener()
                );
            } else { // hide if supposed to be turned off (was lazy to add another xml for it)
                addButton.setVisibility(GONE);
            }

            dialog.show();
        });
    }

    protected abstract OnClickListener createAddButtonClickListener();

    public void updateData() {
        objects.clear();
        objects.addAll(getData());
        adapter.notifyDataSetChanged();
    }

    protected abstract List<T> getData();

    protected abstract ArrayAdapter<T> createListAdapter();

    protected abstract boolean canAddNewItem();

    protected abstract void insertNewItemIntoDB();

    protected AdapterView.OnItemClickListener createOnListItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                T item = (T) adapter.getItem(position);

                parentSpinner.setText(item.toString());
                onListItemSelected(item);

                dialog.dismiss();
            }
        };
    }

    protected abstract void onListItemSelected(T item);

    public Dialog getDialog() {
        return dialog;
    }
}
