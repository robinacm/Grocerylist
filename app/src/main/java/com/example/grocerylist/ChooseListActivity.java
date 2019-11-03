package com.example.grocerylist;

import android.database.Cursor;
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
                getRowInDB();
            }
        });
    }

    private void getAllItems() {
        Cursor c = database.rawQuery("SELECT * FROM groceryList", null);

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            buffer.append(c.getString(1) + " ");
            buffer.append(c.getString(2) + " ");
            buffer.append(c.getString(3) + "\n");
        }

        System.out.println("Printing out data: " + buffer.toString());
    }

    private void getRowInDB() {
        Cursor c = database.rawQuery("SELECT name,_id FROM groceryList WHERE _id=20", null);

        StringBuffer buffer = new StringBuffer();
        while(c.moveToNext()){
            buffer.append(c.getString(0) + " ");
            buffer.append(c.getString(1) + "\n");
        }

        System.out.println("Printing out data: " + buffer.toString());
    }
}
