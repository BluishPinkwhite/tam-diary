package com.example.tamcalendar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.tamcalendar.action.ActionCreateFragment;
import com.example.tamcalendar.database.DatabaseFileHandler;
import com.example.tamcalendar.database.DatabaseManager;
import com.example.tamcalendar.database.TamDatabase;
import com.example.tamcalendar.databinding.ActivityMainBinding;
import com.example.tamcalendar.emotion.EmotionCreateFragment;

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
                    EmotionCreateFragment.emotionToEdit = null;
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
        int id = item.getItemId();

        if (id == R.id.action_export) {
            DatabaseFileHandler.prepareExportActivity(this);
            return true;
        } else if (id == R.id.action_import) {
            DatabaseFileHandler.prepareImportActivity(this);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
    }
}