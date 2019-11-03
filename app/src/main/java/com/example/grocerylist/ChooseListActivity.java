package com.example.grocerylist;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseListActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private GroceryAdapter adapter;

    private Button btnChooseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_list);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        database = dbHelper.getWritableDatabase();

        btnChooseList = findViewById(R.id.button_chooseList);

        btnChooseList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAllItems();
            }
        });
    }

    private void getAllItems() {
        database.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,null,null,null,null /* Behöver inte dessa för queryn*/,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
