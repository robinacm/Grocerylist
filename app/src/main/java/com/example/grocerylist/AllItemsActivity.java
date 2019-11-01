package com.example.grocerylist;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class AllItemsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button btnAddGroceries, btnClearAllItems;
    private SQLiteDatabase database;
    private GroceryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        database = dbHelper.getWritableDatabase();

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);

        btnAddGroceries = (Button)findViewById(R.id.button_addNewItem);
        btnClearAllItems = (Button)findViewById(R.id.button_clearAllItems);

        btnAddGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addGroceriesActivity();
            }
        });
        btnClearAllItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllItemsAlert();
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Kallar på medtod som identifierar item som ska tas bort ur databasen
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long)viewHolder.itemView.getTag());

            }
        }).attachToRecyclerView(recyclerView); // Ger vår recyclerView denna touchHelper
    }

    private void addGroceriesActivity() {
        startActivity(new Intent(AllItemsActivity.this, MainActivity.class));
    }

    private void removeItem(long id) {
        database.delete(GroceryContract.GroceryEntry.TABLE_NAME, GroceryContract.GroceryEntry._ID + "=" + id, null);
        adapter.swapCursor(getAllItems()); // Ger adaptern en ny cursor med kvarvarande items så att itemet som togs bort inte ligger kvar
    }

    private void removeAllItemsAlert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Confirm delete");
        alert.setMessage("Are you sure you want to clear the list?");
        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clearAllItems();
                recyclerView.setAdapter(null);
                Toast.makeText(AllItemsActivity.this, "Cleared the list",Toast.LENGTH_SHORT).show();
            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                return;
            }
        });
        alert.create().show();
    }

    private void clearAllItems(){
        System.out.println(GroceryContract.GroceryEntry.TABLE_NAME);
        database.execSQL("delete from " + GroceryContract.GroceryEntry.TABLE_NAME);
    }

    private Cursor getAllItems(){
        return database.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,null,null,null,null /* Behöver inte dessa för queryn*/,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
