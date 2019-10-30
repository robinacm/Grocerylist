package com.example.grocerylist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import com.example.grocerylist.GroceryContract.*;


// Detta är en hjälparklass som används för att kommunicera med SQLite, skapar databas med tabell som har flera kolumner
public class GroceryDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "grocerylist.db";
    public static final int DATABASE_VERSION = 1;

    public GroceryDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Kallas när vi först skapar vår databas
    // Etablerar databasen, ger kolumn namn med hjälp av vår andra hjälparklass GroceryContract
    @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            final String SQL_CREATE_GROCERYLIST_TABLE = "CREATE TABLE " +
                    GroceryEntry.TABLE_NAME + " (" +
                    GroceryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    GroceryEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                    GroceryEntry.COLUMN_AMOUNT + " INTEGER NOT NULL, " +
                    GroceryEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ");"; // Stänger SQLite statement och strängen för själva tabellen i databasen

            sqLiteDatabase.execSQL(SQL_CREATE_GROCERYLIST_TABLE); // Exekverar och skapar databasem
        }

        // Vill man lägga till en ny kolumn i tabellen så droppar vi tabellen och skapar en ny
        // Alternativ hade varit att inkrementera DATABASE_VERSION och uppdatera den dåvarande tabellen för att inte förlora allt
        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GroceryEntry.TABLE_NAME); // Droppar tabell
            onCreate(sqLiteDatabase); // Skickar tabell till onCreate med ny kolumn
        }
    }
