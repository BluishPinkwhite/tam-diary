package com.example.tamcalendar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tamcalendar.data.DatabaseFileHandler;

public class ImportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_import);

        Button buttonDB = findViewById(R.id.buttonSaveDB);
        buttonDB.setOnClickListener(v -> {
            DatabaseFileHandler.prepareImport(this, 1);
        });

        Button buttonSHM = findViewById(R.id.buttonSaveSHM);
        buttonSHM.setOnClickListener(v -> {
            DatabaseFileHandler.prepareImport(this, 2);
        });

        Button buttonWAL = findViewById(R.id.buttonSaveWAL);
        buttonWAL.setOnClickListener(v -> {
            DatabaseFileHandler.prepareImport(this, 3);
        });



        Button buttonCancel = findViewById(R.id.buttonCancel);
        buttonCancel.setOnClickListener(v -> {
            finish();
        });


        Button buttonFinish = findViewById(R.id.buttonFinish);
        buttonFinish.setOnClickListener(v -> {
            // restart app to apply new DB
            Context context = getApplicationContext();
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
            Runtime.getRuntime().exit(0);
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 389057) { // IMPORT
            DatabaseFileHandler.handleImport(this, data);
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
