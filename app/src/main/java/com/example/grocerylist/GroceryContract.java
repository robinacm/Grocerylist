package com.example.grocerylist;


import android.provider.BaseColumns;

    // I denna klass definerar vi strängar som är konstanter för tabell namnen samt raderna för tabellen
    // Det är enbart en hjälparklass
public class GroceryContract {

    // Lämnar konstruktorn tom, behöver bara tillgång till de publika konstanterna
    private GroceryContract(){}

    // Skapar en inre klass för tabellen - räcker med en för vi har bara 1 tabell
    // Klassen implementerar BaseColumns, den ger oss en ID kolumn för vår tabell
    public static final class GroceryEntry implements BaseColumns{
        public static final String TABLE_NAME = "groceryList";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AMOUNT = "amount";
        public static final String COLUMN_TIMESTAMP = "timeStamp";
    }
}
