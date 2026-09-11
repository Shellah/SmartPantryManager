package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private Button btnSave, btnCancel;
    private TextView tvTitle;
    private DatabaseHelper dbHelper;

    private boolean isEditMode = false;
    private int editItemId = -1;

    // Available units
    private final String[] units = {"unit", "g", "kg", "ml", "L", "tbsp", "tsp", "pinch", "cloves", "slices", "loaf", "packet"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper = new DatabaseHelper(this);

        // Find views
        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        etExpiry = findViewById(R.id.etExpiry);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        btnSave = findViewById(R.id.btnSave);
        btnCancel = findViewById(R.id.btnCancel);
        tvTitle = findViewById(R.id.tvTitle);

        // Setup spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, units);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(adapter);

        // Check if we're editing an existing item
        if (getIntent().hasExtra("item_id")) {
            isEditMode = true;
            editItemId = getIntent().getIntExtra("item_id", -1);
            String name = getIntent().getStringExtra("item_name");
            int quantity = getIntent().getIntExtra("item_quantity", 0);
            String unit = getIntent().getStringExtra("item_unit");
            String expiry = getIntent().getStringExtra("item_expiry");

            tvTitle.setText("Edit Ingredient");
            etName.setText(name);
            etQuantity.setText(String.valueOf(quantity));
            etExpiry.setText(expiry);

            // Set spinner to the correct unit
            if (unit != null) {
                for (int i = 0; i < units.length; i++) {
                    if (units[i].equals(unit)) {
                        spinnerUnit.setSelection(i);
                        break;
                    }
                }
            }
        } else {
            tvTitle.setText("Add Ingredient");
        }

        // Save button
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem();
            }
        });

        // Cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });
    }

    private void saveItem() {
        String name = etName.getText().toString().trim();
        String quantityStr = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem().toString();
        String expiry = etExpiry.getText().toString().trim();

        // Validation
        if (name.isEmpty()) {
            etName.setError("Please enter ingredient name");
            etName.requestFocus();
            return;
        }

        if (quantityStr.isEmpty()) {
            etQuantity.setError("Please enter quantity");
            etQuantity.requestFocus();
            return;
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityStr);
            if (quantity <= 0) {
                etQuantity.setError("Quantity must be greater than 0");
                etQuantity.requestFocus();
                return;
            }
        } catch (NumberFormatException e) {
            etQuantity.setError("Please enter a valid number");
            etQuantity.requestFocus();
            return;
        }

        if (isEditMode) {
            // Update existing
            boolean success = dbHelper.updatePantryItem(editItemId, name, quantity, unit, expiry);
            if (success) {
                Toast.makeText(this, "Updated: " + name, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to update", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Add new
            boolean success = dbHelper.addPantryItem(name, quantity, unit, expiry);
            if (success) {
                Toast.makeText(this, "Added: " + name, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to add", Toast.LENGTH_SHORT).show();
            }
        }
    }
}