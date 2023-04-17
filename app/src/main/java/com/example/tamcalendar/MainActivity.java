package com.example.tamcalendar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tamcalendar.action.ActionCreateFragment;
import com.example.tamcalendar.data.DatabaseManager;
import com.example.tamcalendar.data.TamDatabase;
import com.example.tamcalendar.databinding.ActivityMainBinding;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    public ActivityMainBinding binding;

    public static LocalDate todayDate;
    public static int selectedDayDateSort;
    public static int todayDateSort;
    public static TamDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        todayDate = LocalDate.now();
        todayDateSort = DatabaseManager.createDateSort(todayDate);
        selectedDayDateSort = todayDateSort;

        database = DatabaseManager.createDatabase(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

        binding.fabAddEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                try {
                    ActionCreateFragment.actionToEdit = null;
                    navController.navigate(R.id.action_CalendarFragment_to_ActionCreate);
                } catch (IllegalArgumentException ignored) {
                }
            }
        });

        binding.fabAddFeeling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    navController.navigate(R.id.action_CalendarFragment_to_EmotionCreate);
                } catch (IllegalArgumentException ignored) {
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_export) {

            Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("*/*");
            startActivityForResult(intent, 69420);

            return true;
        } else if (id == R.id.action_import) {

            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("*/*");
            startActivityForResult(intent, 389057);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 69420) { // EXPORT
            try {
                Uri uri = data.getData();
                FileInputStream in = new FileInputStream(getDatabasePath("tam-calendar"));
                OutputStream out = getContentResolver().openOutputStream(uri);

                long transferred = 0;
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer, 0, 8192)) >= 0) {
                    out.write(buffer, 0, read);
                    transferred += read;
                    System.out.println("Exporting DB: " + transferred + "B");
                }

                Toast.makeText(this, "Export complete!", Toast.LENGTH_SHORT).show();
                System.out.println("Export complete!");

                in.close();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 389057) { // IMPORT
            try {
                Uri uri = data.getData();
                InputStream in = getContentResolver().openInputStream(uri);
                FileOutputStream out = new FileOutputStream(getDatabasePath("tam-calendar"));

                long transferred = 0;
                byte[] buffer = new byte[8192];
                int read;
                while ((read = in.read(buffer, 0, 8192)) >= 0) {
                    out.write(buffer, 0, read);
                    transferred += read;
                    System.out.println("Importing DB: " + transferred + "B");
                }

                Toast.makeText(this, "Import complete!", Toast.LENGTH_SHORT).show();
                System.out.println("Import complete!");

                in.close();
                out.close();

                // restart app to apply new DB
                Context context = getBaseContext();
                PackageManager packageManager = context.getPackageManager();
                Intent intent = packageManager.getLaunchIntentForPackage(context.getPackageName());
                ComponentName componentName = intent.getComponent();
                Intent mainIntent = Intent.makeRestartActivityTask(componentName);
                context.startActivity(mainIntent);
                Runtime.getRuntime().exit(0);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, e.getClass().getName() + ": " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}