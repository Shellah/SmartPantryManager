package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.PantryViewHolder> {

    private List<PantryItem> pantryItems;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(PantryItem item);
        void onDeleteClick(PantryItem item);
    }

    public PantryAdapter(List<PantryItem> pantryItems, OnItemClickListener listener) {
        this.pantryItems = pantryItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PantryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_pantry, parent, false);
        return new PantryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PantryViewHolder holder, int position) {
        PantryItem item = pantryItems.get(position);

        holder.tvItemName.setText(item.getName());
        holder.tvItemDetails.setText(item.getQuantity() + " " + item.getUnit()
                + (item.getExpiryDate() != null && !item.getExpiryDate().isEmpty()
                ? " • Expires: " + item.getExpiryDate() : ""));

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onEditClick(item);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onDeleteClick(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pantryItems.size();
    }

    public static class PantryViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemName, tvItemDetails;
        Button btnEdit, btnDelete;

        public PantryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemName = itemView.findViewById(R.id.tvItemName);
            tvItemDetails = itemView.findViewById(R.id.tvItemDetails);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}