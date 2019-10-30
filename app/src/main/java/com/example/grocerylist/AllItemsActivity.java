package com.example.grocerylist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AllItemsActivity extends AppCompatActivity {

    Button btnAddGroceries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_items);

        btnAddGroceries = (Button)findViewById(R.id.button_addNewItem);

        btnAddGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allGroceriesActivity();
            }
        });

        btnAddGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allGroceriesActivity();
            }
        });
    }

    private void allGroceriesActivity() {
        startActivity(new Intent(AllItemsActivity.this, MainActivity.class));
    }
}
