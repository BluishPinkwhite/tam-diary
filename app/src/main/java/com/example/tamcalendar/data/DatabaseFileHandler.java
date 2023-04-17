package com.example.tamcalendar.data;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.tamcalendar.ExportActivity;
import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseFileHandler {

    private static MainActivity activity;

    public static void prepareExportActivity(MainActivity activity) {
        AlertDialog dialog =
                new AlertDialog.Builder(activity)
                        .setView(R.layout.dialog_export_info)
                        .setCancelable(true)
                        .setPositiveButton(R.string.confirm, (dialog1, which) -> {
                            DatabaseFileHandler.activity = activity;

                            Intent intent = new Intent(activity, ExportActivity.class);
                            activity.startActivity(intent);
                        })
                        .setNegativeButton(R.string.cancel, (dialog1, which) -> {
                            Toast.makeText(activity, activity.getString(R.string.export_cancelled), Toast.LENGTH_SHORT).show();
                        })
                        .create();
        dialog.show();
    }

    public static void prepareExport(ExportActivity exportActivity, int fileType) {
        switch (fileType) {
            case 1:
                Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
                intent.setType("*/*");
                activity.startActivityForResult(intent, 69420);
                break;
            default:
                Toast.makeText(exportActivity, "Export invalid", Toast.LENGTH_SHORT).show();
        }
    }

    public static void prepareImport(MainActivity activity) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        activity.startActivityForResult(intent, 389057);
    }

    public static void handleExport(MainActivity activity, Intent data) {
        try {
            Uri uri = data.getData();

            FileInputStream in = new FileInputStream(
                    activity.getDatabasePath(DatabaseManager.DATABASE_NAME));
            OutputStream out = activity.getContentResolver().openOutputStream(uri);

            long transferred = 0;
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer, 0, 8192)) >= 0) {
                out.write(buffer, 0, read);
                transferred += read;
                System.out.println("Exporting DB: " + transferred + "B");
            }

            Toast.makeText(activity, "Export complete!", Toast.LENGTH_SHORT).show();
            System.out.println("Export complete!");

            in.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public static void handleImport(MainActivity activity, Intent data) {
        try {
            Uri uri = data.getData();
            InputStream in = activity.getContentResolver().openInputStream(uri);
            FileOutputStream out = new FileOutputStream(
                    activity.getDatabasePath(DatabaseManager.DATABASE_NAME));

            long transferred = 0;
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer, 0, 8192)) >= 0) {
                out.write(buffer, 0, read);
                transferred += read;
                System.out.println("Importing DB: " + transferred + "B");
            }

            Toast.makeText(activity, "Import complete!", Toast.LENGTH_SHORT).show();
            System.out.println("Import complete!");

            in.close();
            out.close();

            // restart app to apply new DB
            Context context = activity.getBaseContext();
            PackageManager packageManager = context.getPackageManager();
            Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
            ComponentName componentName = intent.getComponent();
            Intent mainIntent = Intent.makeRestartActivityTask(componentName);
            context.startActivity(mainIntent);
            Runtime.getRuntime().exit(0);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
