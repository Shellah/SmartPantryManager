package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    private Switch switchExpiry, switchMetric;
    private Button btnBackSettings;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        prefs = getSharedPreferences("SmartPantryPrefs", MODE_PRIVATE);

        switchExpiry = findViewById(R.id.switchExpiry);
        switchMetric = findViewById(R.id.switchMetric);
        btnBackSettings = findViewById(R.id.btnBackSettings);

        // Load saved preferences
        switchExpiry.setChecked(prefs.getBoolean("expiry_alerts", true));
        switchMetric.setChecked(prefs.getBoolean("metric_units", true));

        // Save changes when toggled
        switchExpiry.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("expiry_alerts", isChecked).apply();
                Toast.makeText(SettingsActivity.this,
                        "Expiry alerts: " + (isChecked ? "ON" : "OFF"),
                        Toast.LENGTH_SHORT).show();
            }
        });

        switchMetric.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                prefs.edit().putBoolean("metric_units", isChecked).apply();
                Toast.makeText(SettingsActivity.this,
                        "Metric units: " + (isChecked ? "ON" : "OFF"),
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnBackSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}