package com.example.grocerylist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

    // Author: Robin Shirvani

    /*
    *   Detta är en liten applikation som fungerar som en matvarulista.
    *   Har implementerat en egen Adapter (GroceryAdapter) för att kunna lägga till flera items och sedan
    *   koppla de till en RecyclerView. Användaren ger ett namn och ett antal på vad den behöver
    *   och klickar sedan add för att lägga till det i listan. Det finns sedan möjlighet att
    *   ta bort items ur recyclerViewen genom att hålla ner och swipa höger eller vänster.
    *
    *   GroceryDBHelper är till för att skapa en SQLite databas. Appen utnyttjar SQLite för att
    *   behålla senaste listan även när applikationen stängts ner.
    * */

public class MainActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    private GroceryAdapter adapter;
    private EditText editTextName;
    private TextView textViewAmount;
    private Button btnDecrease, btnIncrease, btnAdd, btnAllGroceries;
    private int amount = 0;

    // Assignar/initierar databas, knappar, textfält samt RecyclerView och adapter
    // Tilldelar en egenskapad adapter till vår recyclerView
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GroceryDBHelper dbHelper = new GroceryDBHelper(this);
        database = dbHelper.getWritableDatabase();

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GroceryAdapter(this, getAllItems());
        recyclerView.setAdapter(adapter);

        // Lägger till en touchHelper för att kunna ta bort items som swipas åt sidan.
        // Vi tar emot argument LEFT och RIGHT för att agera/hantera swips åt båda hållen
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Kallar på medtod som identifierar item som ska tas bort ur databasen
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long)viewHolder.itemView.getTag()); // Skickar argument till metod, i adapterklassen så gav vi viewHolder en "tag", detta är även primära nyckeln.

            }
        }).attachToRecyclerView(recyclerView); // Ger vår recyclerView denna touchHelper

        editTextName = findViewById(R.id.editText_name);
        textViewAmount = findViewById(R.id.textView_amount);
        btnDecrease = (Button)findViewById(R.id.button_decrease);
        btnIncrease = (Button)findViewById(R.id.button_increase);
        btnAdd = (Button)findViewById(R.id.button_add);
        btnAllGroceries = (Button)findViewById(R.id.button_allGroceries);


        // Knapplyssnare till öka knappen. Kallar på metod som inkrementerar amount när man klickar på "+" knappen.
        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increase();
            }
        });

        // Knapplyssnare till minska knappen. Kallar på metod som minskar amount.
        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decrease();
            }
        });

        // Knapplyssnare till add. Kallar på metod som lägger till item i listan.
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addItem();
            }
        });

        btnAllGroceries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                allGroceriesActivity();
            }
        });
    }

    private void allGroceriesActivity() {
        startActivity(new Intent(MainActivity.this, AllItemsActivity.class));
    }

    // Ökar värdet och skriver ut det
    private void increase(){
        amount++; //inkrementerar amount
        textViewAmount.setText(String.valueOf(amount)); // Skriver ut det nya värdet på skärmen
    }


    // Kollar så att amount inte är 0, annars kan vi minska antalet vi vill ha av itemet
    private void decrease() {
        if(amount > 0){
            amount--;
            textViewAmount.setText(String.valueOf(amount));
        }
    }

    // Kontrollera först att vi har någon input från användaren
    // Lägger sedan till item i vår SQLite databas
    private void addItem(){
        if(editTextName.getText().toString().trim().length() == 0 || amount == 0){ // om vi inte har någon input från användaren eller om antalet är 0 så gör vi inget
            return;
        }

        String itemName = editTextName.getText().toString();
        ContentValues cv = new ContentValues(); // tar hjälp av ContentValues för att skapa nyckel och värde som vi kan lägga till i vår tabell
        cv.put(GroceryContract.GroceryEntry.COLUMN_NAME, itemName);
        cv.put(GroceryContract.GroceryEntry.COLUMN_AMOUNT, amount);

        // Värdena från cv läggs till i korresponderande kolumner i vår tabell med hjälp av vår hjälpar klass
        database.insert(GroceryContract.GroceryEntry.TABLE_NAME, null, cv);
        adapter.swapCursor(getAllItems()); // Efter vi lagt till items så byter vi ut cursorn så att nästa item kan hämtas

        editTextName.getText().clear(); // Tömmer textfältet så att användaren kan skriva in nytt värde
    }

    // Vi identifierar itemet som ska tas bort från listan efter swipe
    // Itemet identifieras med hjälp av en "tag" som alla items har. Denna "tag" är även primära nyckeln så att vi kan hitta unika items.
    // Gå in i tabellen och identifera ID kolumnen där den matchar med det id som vi fått som argument.
    private void removeItem(long id) {
        database.delete(GroceryContract.GroceryEntry.TABLE_NAME, GroceryContract.GroceryEntry._ID + "=" + id, null);
        adapter.swapCursor(getAllItems()); // Ger adaptern en ny cursor med kvarvarande items så att itemet som togs bort inte ligger kvar
    }

    // Returnerar en cursor som innehåller alla items och sortera de i descending ordning. Behöver cursor (i andra metoder) till adapter för att hitta items.
    // Vi hämtar alltså alla items och sorterar de så att det senast tillagda itemet ligger högst uppe.
    private Cursor getAllItems(){
        // Frågar mot databasen, hämta värden som vi kräver.
        return database.query(
                GroceryContract.GroceryEntry.TABLE_NAME,
                null,null,null,null,null /* Behöver inte dessa för queryn*/,
                GroceryContract.GroceryEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }
}
