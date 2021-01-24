package com.rtsoftworld.grocerylist_sqlite.UI;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.rtsoftworld.grocerylist_sqlite.Data.DataHandler;
import com.rtsoftworld.grocerylist_sqlite.DetailsActivity;
import com.rtsoftworld.grocerylist_sqlite.Model.Grocery;
import com.rtsoftworld.grocerylist_sqlite.R;

import java.sql.DatabaseMetaData;
import java.util.List;
import java.util.zip.Inflater;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Grocery> groceryList;

    public RecyclerViewAdapter(Context context, List<Grocery> groceryList) {
        this.context = context;
        this.groceryList = groceryList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapter.ViewHolder holder, int position) {
        Grocery grocery = groceryList.get(position);

        holder.groceryItemName.setText(grocery.getName());
        holder.quantity.setText(grocery.getQuantity());
        holder.dateAdded.setText(grocery.getDateItemAdded());
    }

    @Override
    public int getItemCount() {
        return groceryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView groceryItemName;
        public TextView quantity;
        public TextView dateAdded;
        public Button editButton;
        public Button deleteButton;
        public int id;

        public ViewHolder(View view, Context ctx) {
            super(view);
            context = ctx;

            groceryItemName = view.findViewById(R.id.itemName);
            quantity = view.findViewById(R.id.quantity);
            dateAdded = view.findViewById(R.id.dateAdded);
            editButton = view.findViewById(R.id.editButton);
            deleteButton = view.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //go to next screen > DetailsActivity
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    Intent intent = new Intent(context, DetailsActivity.class);
                    intent.putExtra("name",grocery.getName());
                    intent.putExtra("quantity",grocery.getQuantity());
                    intent.putExtra("id",grocery.getId());
                    intent.putExtra("date",grocery.getDateItemAdded());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view) {

            switch (view.getId()){
                case R.id.editButton:
                    int position = getAdapterPosition();
                    Grocery grocery = groceryList.get(position);
                    edit_item(grocery);
                    break;
                case R.id.deleteButton:
                    position = getAdapterPosition();
                    grocery = groceryList.get(position);
                    deleteItem(grocery.getId());
                    break;
            }

        }


    public void deleteItem(final int id){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Delete Item");
        alertDialog.setMessage("Are you sure want to delete this item?");

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Yes", (dialogInterface, i) -> {
            //delete item
            DataHandler db = new DataHandler(context);
            db.deleteGrocery(id);
            groceryList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());

        });
        alertDialog.show();
    }


    public void edit_item(Grocery grocery){
        AlertDialog dialog;
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.popup,null);

        TextView title = view.findViewById(R.id.title);
        EditText groceryItem = view.findViewById(R.id.groceryItem);
        EditText quantity = view.findViewById(R.id.groceryQTY);
        Button saveButton = view.findViewById(R.id.saveButton);

        title.setText("Edit Grocery Item");

        alertDialog.setView(view);
        dialog = alertDialog.create();
        dialog.show();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataHandler db = new DataHandler(context);
                //update database
                grocery.setName(groceryItem.getText().toString());
                grocery.setQuantity(quantity.getText().toString());

                if (!groceryItem.getText().toString().isEmpty() && !quantity.getText().toString().isEmpty()){
                    db.updateGrocery(grocery);
                    notifyItemChanged(getAdapterPosition(),grocery);
                }else {
                    Snackbar.make(view,"Add grocery and quantity.",Snackbar.LENGTH_LONG).show();
                }
                dialog.dismiss();
            }

        });

        }
    }
}
