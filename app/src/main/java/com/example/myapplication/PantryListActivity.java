package com.example.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryListActivity extends AppCompatActivity implements PantryAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private PantryAdapter adapter;
    private DatabaseHelper dbHelper;
    private TextView tvEmpty;
    private Button btnAddItem, btnViewRecipes, btnSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantry_list);

        dbHelper = new DatabaseHelper(this);

        recyclerView = findViewById(R.id.recyclerViewPantry);
        tvEmpty = findViewById(R.id.tvEmpty);
        btnAddItem = findViewById(R.id.btnAddItem);
        btnViewRecipes = findViewById(R.id.btnViewRecipes);
        btnSettings = findViewById(R.id.btnSettings);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addTestDataIfEmpty();

        btnAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantryListActivity.this, AddEditActivity.class));
            }
        });

        btnViewRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantryListActivity.this, SuggestedRecipesActivity.class));
            }
        });

        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PantryListActivity.this, SettingsActivity.class));
            }
        });

        loadPantryItems();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPantryItems();
    }

    private void addTestDataIfEmpty() {
        List<PantryItem> existing = dbHelper.getAllPantryItems();
        if (existing.isEmpty()) {
            dbHelper.addPantryItem("Eggs", 6, "unit", "2024-12-31");
            dbHelper.addPantryItem("Milk", 1, "L", "2024-12-20");
            dbHelper.addPantryItem("Cheese", 200, "g", "2024-12-25");
            dbHelper.addPantryItem("Bread", 1, "loaf", "2024-12-22");
            dbHelper.addPantryItem("Salt", 5, "pinch", "");
        }
    }

    private void loadPantryItems() {
        List<PantryItem> items = dbHelper.getAllPantryItems();

        if (items.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }

        if (adapter == null) {
            adapter = new PantryAdapter(items, this);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onEditClick(PantryItem item) {
        Intent intent = new Intent(PantryListActivity.this, AddEditActivity.class);
        intent.putExtra("item_id", item.getId());
        intent.putExtra("item_name", item.getName());
        intent.putExtra("item_quantity", item.getQuantity());
        intent.putExtra("item_unit", item.getUnit());
        intent.putExtra("item_expiry", item.getExpiryDate());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(PantryItem item) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Item")
                .setMessage("Delete " + item.getName() + " from pantry?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deletePantryItem(item.getId());
                        loadPantryItems();
                        Toast.makeText(PantryListActivity.this, "Deleted: " + item.getName(), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
}