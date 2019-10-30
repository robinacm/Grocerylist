package com.example.grocerylist;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

    // Denna klass är till för att synliggöra allt användaren lägger till i sin matkasse
    // Själva klassen är en adapter som har en lista på vyhållare, dessa hållare har ett namn och en summa.
    // Det GroceryAdapter gör är att den tillåter oss lägga in alla items (hållare) till en recyclerview.
public class GroceryAdapter extends RecyclerView.Adapter<GroceryAdapter.GroceryViewHolder> {

    private Context context;
    private Cursor cursor;

    // cursor hämtar datan från vår databas för att kunna visa items som användaren redan lagt till
    // När vi skapar adaptern så tar vi emot en cursor som argument.
    public GroceryAdapter(Context context, Cursor cursor){
        this.context = context;
        this.cursor = cursor;
    }


    // Skapar en inre klass som fungerar som en hållare av item objekt
    // Varje item har ett namn och ett antal som sedan ska läggas till i en lista (recyclerview)
    public class GroceryViewHolder extends RecyclerView.ViewHolder {

        public TextView itemName; // Visar namnet på itemet i listan
        public TextView countText;

        public GroceryViewHolder(View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.textView_item_name);
            countText = itemView.findViewById(R.id.textView_item_amount);
        }
    }

    // När ett item ska läggas till i listan så skapas en ViewHolder som innehåller informationen.
    // Vi inflatear sedan listan (recyclerviewen) och lägger till ett nytt item.
    @Override
    public GroceryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.grocery_item, parent, false); // Vi synliggör itemet i recyclerviewen
        return new GroceryViewHolder(view);
    }

    // Ser till så att data visas i vårt item. När man exempelvis scrollar ner så vill vi visa
    // datan som ska synas där. Detta är pga. recyclerView som sparar minne och inte laddar in hela listan så vi måste uppdatera.
    @Override
    public void onBindViewHolder(GroceryViewHolder holder, int position) {
        if(!cursor.moveToPosition(position)){
            return;
        }

        // Når värden i databas
        String name = cursor.getString(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_NAME));
        int amount = cursor.getInt(cursor.getColumnIndex(GroceryContract.GroceryEntry.COLUMN_AMOUNT));

        long id = cursor.getLong(cursor.getColumnIndex(GroceryContract.GroceryEntry._ID)); // läser ID (primärnyckel)

        holder.itemName.setText(name);
        holder.countText.setText(String.valueOf(amount));
        holder.itemView.setTag(id); // Ger itemet en tag som även är vår primära nyckel, den används senare när vi vill identifiera items/ta bort de
    }

    // Vi vill returnera en siffra som representerar antal items som vi har i vår databas
    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    // Byter ut vår cursor till en ny
    // Detta är för att recyclerView ska uppdatera vad vi ser på skärmen
    public void swapCursor(Cursor newCursor){
        if(cursor != null){
            cursor.close();
        }
        cursor = newCursor;

        if(newCursor != null){
            notifyDataSetChanged();
        }
    }
}
