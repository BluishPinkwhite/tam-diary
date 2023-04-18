package com.example.tamcalendar.data;

import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.example.tamcalendar.ExportActivity;
import com.example.tamcalendar.ImportActivity;
import com.example.tamcalendar.MainActivity;
import com.example.tamcalendar.R;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseFileHandler {

    private static int transferType = -1;
    private static String[] types = {"lmao", "DB", "SHM", "WAL"};

    public static void prepareExportActivity(MainActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity)
                .setView(R.layout.dialog_export_info)
                .setCancelable(true).setPositiveButton(R.string.confirm, (dialog1, which) -> {
                    Intent intent = new Intent(activity, ExportActivity.class);
                    activity.startActivity(intent);
                }).setNegativeButton(R.string.cancel, (dialog1, which) -> {
                    Toast.makeText(activity, activity.getString(R.string.export_cancelled), Toast.LENGTH_SHORT).show();
                }).create();
        dialog.show();
    }

    public static void prepareExport(ExportActivity exportActivity, int fileType) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");

        transferType = fileType;

        switch (fileType) {
            case 1:
                intent.putExtra(Intent.EXTRA_TITLE, "cDB-" + DatabaseManager.dateSortToday());
                break;
            case 2:
                intent.putExtra(Intent.EXTRA_TITLE, "cSHM-" + DatabaseManager.dateSortToday());
                break;
            case 3:
                intent.putExtra(Intent.EXTRA_TITLE, "cWAL" + DatabaseManager.dateSortToday());
                break;
            default:
                Toast.makeText(exportActivity, "Export invalid", Toast.LENGTH_SHORT).show();
        }
        exportActivity.startActivityForResult(intent, 69420);
    }

    public static void handleExport(ExportActivity activity, Intent data) {
        try {
            Uri uri = data.getData();

            FileInputStream in = null;
            if (transferType == 1) {
                in = new FileInputStream(activity.getDatabasePath(
                        DatabaseManager.DATABASE_NAME_FULL).getPath());
            } else if (transferType == 2) {
                in = new FileInputStream(
                        activity.getDatabasePath(
                                DatabaseManager.DATABASE_NAME_FULL).getPath() + "-shm");
            } else if (transferType == 3) {
                in = new FileInputStream(activity.getDatabasePath(
                        DatabaseManager.DATABASE_NAME_FULL).getPath() + "-wal");
            }
            OutputStream out = activity.getContentResolver().openOutputStream(uri);

            long transferred = 0;
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer, 0, 8192)) >= 0) {
                out.write(buffer, 0, read);
                transferred += read;
                System.out.println("Exporting DB: " + transferred + "B");
            }

            Toast.makeText(activity, "Export " + types[transferType] + " complete!", Toast.LENGTH_SHORT).show();
            System.out.println("Export " + types[transferType] + " complete!");

            in.close();
            out.close();
        } catch (NullPointerException e) {
            Toast.makeText(activity, activity.getString(R.string.export_cancelled), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

//////////////////////

    public static void prepareImportActivity(MainActivity activity) {
        AlertDialog dialog = new AlertDialog.Builder(activity).setView(R.layout.dialog_import_info)
                .setCancelable(true)
                .setPositiveButton(R.string.confirm, (dialog1, which) -> {
                    Intent intent = new Intent(activity, ImportActivity.class);
                    activity.startActivity(intent);
                }).setNegativeButton(R.string.cancel, (dialog1, which) -> {
                    Toast.makeText(activity, activity.getString(R.string.import_cancelled), Toast.LENGTH_SHORT).show();
                }).create();
        dialog.show();
    }

    public static void prepareImport(ImportActivity importActivity, int fileType) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");

        transferType = fileType;

        switch (fileType) {
            case 1:
                intent.putExtra(Intent.EXTRA_TITLE, "cDB-" + DatabaseManager.dateSortToday());
                break;
            case 2:
                intent.putExtra(Intent.EXTRA_TITLE, "cSHM-" + DatabaseManager.dateSortToday());
                break;
            case 3:
                intent.putExtra(Intent.EXTRA_TITLE, "cWAL" + DatabaseManager.dateSortToday());
                break;
            default:
                Toast.makeText(importActivity, "Import invalid", Toast.LENGTH_SHORT).show();
        }
        importActivity.startActivityForResult(intent, 389057);
    }

    public static void handleImport(ImportActivity activity, Intent data) {
        try {
            Uri uri = data.getData();
            System.out.println("Importing: " + uri);

            InputStream in = activity.getContentResolver().openInputStream(uri);

            FileOutputStream out = null;
            if (transferType == 1) {
                out = new FileOutputStream(activity.getDatabasePath(
                        DatabaseManager.DATABASE_NAME_FULL).getPath());
            } else if (transferType == 2) {
                out = new FileOutputStream(activity.getDatabasePath(
                        DatabaseManager.DATABASE_NAME_FULL).getPath() + "-shm");
            } else if (transferType == 3) {
                out = new FileOutputStream(activity.getDatabasePath(
                        DatabaseManager.DATABASE_NAME_FULL).getPath() + "-wal");
            }

            long transferred = 0;
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer, 0, 8192)) >= 0) {
                out.write(buffer, 0, read);
                transferred += read;
                System.out.println("Importing DB: " + transferred + "B");
            }

            Toast.makeText(activity, "Import " + types[transferType] + " complete!", Toast.LENGTH_SHORT).show();
            System.out.println("Import " + types[transferType] + " complete!");

            in.close();
            out.close();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(activity, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}
