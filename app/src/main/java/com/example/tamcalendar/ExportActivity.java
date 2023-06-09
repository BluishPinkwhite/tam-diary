package com.example.tamcalendar;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamcalendar.database.DatabaseFileHandler;

public class ExportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_export);

        Button buttonDB = findViewById(R.id.buttonSaveDB);
        buttonDB.setOnClickListener(v -> {
            DatabaseFileHandler.prepareExport(this, 1);
        });

        Button buttonSHM = findViewById(R.id.buttonSaveSHM);
        buttonSHM.setOnClickListener(v -> {
            DatabaseFileHandler.prepareExport(this, 2);
        });

        Button buttonWAL = findViewById(R.id.buttonSaveWAL);
        buttonWAL.setOnClickListener(v -> {
            DatabaseFileHandler.prepareExport(this, 3);
        });


        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(v -> {
            finish();
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 69420) { // EXPORT
            DatabaseFileHandler.handleExport(this, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
