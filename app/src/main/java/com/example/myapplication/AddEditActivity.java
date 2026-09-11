package com.example.myapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddEditActivity extends AppCompatActivity {

    private EditText etName, etQuantity, etExpiry;
    private Spinner spinnerUnit;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private int editId = -1;

    // Units available in the dropdown
    private static final String[] UNITS = {
            "unit", "g", "kg", "ml", "l", "tbsp", "tsp", "pinch", "cloves", "slices", "packet"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);

        dbHelper = new DatabaseHelper(this);

        etName = findViewById(R.id.etName);
        etQuantity = findViewById(R.id.etQuantity);
        spinnerUnit = findViewById(R.id.spinnerUnit);
        etExpiry = findViewById(R.id.etExpiry);
        btnSave = findViewById(R.id.btnSave);

        // Populate the unit spinner
        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, UNITS);
        unitAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUnit.setAdapter(unitAdapter);

        // If editing an existing item, populate fields
        editId = getIntent().getIntExtra("item_id", -1);
        if (editId != -1) {
            etName.setText(getIntent().getStringExtra("item_name"));
            etQuantity.setText(String.valueOf(getIntent().getIntExtra("item_quantity", 0)));
            etExpiry.setText(getIntent().getStringExtra("item_expiry"));

            String existingUnit = getIntent().getStringExtra("item_unit");
            if (existingUnit != null) {
                int index = unitAdapter.getPosition(existingUnit);
                if (index >= 0) spinnerUnit.setSelection(index);
            }
        }

        btnSave.setOnClickListener(v -> saveItem());
    }

    /**
     * Validates the form and either inserts a new pantry item or updates
     * an existing one. Shows clear error messages on invalid input.
     */
    private void saveItem() {
        String name = etName.getText().toString().trim();
        String qtyStr = etQuantity.getText().toString().trim();
        String unit = spinnerUnit.getSelectedItem() != null
                ? spinnerUnit.getSelectedItem().toString()
                : "unit";
        String expiry = etExpiry.getText().toString().trim();

        // Validation: name
        if (TextUtils.isEmpty(name)) {
            etName.setError("Ingredient name is required");
            etName.requestFocus();
            return;
        }
        if (name.length() < 2) {
            etName.setError("Name must be at least 2 characters");
            etName.requestFocus();
            return;
        }

        // Validation: quantity
        if (TextUtils.isEmpty(qtyStr)) {
            etQuantity.setError("Quantity is required");
            etQuantity.requestFocus();
            return;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(qtyStr);
        } catch (NumberFormatException e) {
            etQuantity.setError("Quantity must be a whole number");
            etQuantity.requestFocus();
            return;
        }
        if (quantity <= 0) {
            etQuantity.setError("Quantity must be greater than 0");
            etQuantity.requestFocus();
            return;
        }

        // Save
        boolean success;
        if (editId == -1) {
            success = dbHelper.addPantryItem(name, quantity, unit, expiry);
        } else {
            success = dbHelper.updatePantryItem(editId, name, quantity, unit, expiry);
        }

        if (success) {
            Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to save item", Toast.LENGTH_SHORT).show();
        }
    }
}