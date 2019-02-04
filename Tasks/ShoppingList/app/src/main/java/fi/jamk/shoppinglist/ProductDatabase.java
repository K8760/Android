package fi.jamk.shoppinglist;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by pasi on 27/09/15.
 */
public class ProductDatabase {
    // Database table
    public static final String TABLE_PRODUCTS = "products";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_PRODUCT = "product";
    public static final String COLUMN_COUNT = "count";
    public static final String COLUMN_PRICE = "price";


    // Database creation SQL statement
    private static final String DATABASE_CREATE = "CREATE TABLE "
        + TABLE_PRODUCTS
        + "("
        + COLUMN_ID + " integer primary key autoincrement, "
        + COLUMN_PRODUCT + " text not null, "
        + COLUMN_COUNT + " int, "
        + COLUMN_PRICE + " float "
        + ");";

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUCTS);
        onCreate(database);
    }
}
