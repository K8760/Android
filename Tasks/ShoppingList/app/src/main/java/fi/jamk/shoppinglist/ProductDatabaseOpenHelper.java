package fi.jamk.shoppinglist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by pasi on 27/09/15.
 */
public class ProductDatabaseOpenHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "products.db";
    private static final int DATABASE_VERSION = 1;

    // Constructor
    public ProductDatabaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // database creation
    @Override
    public void onCreate(SQLiteDatabase database) {
        ProductDatabase.onCreate(database);
    }

    // database upgrade
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        ProductDatabase.onUpgrade(database, oldVersion, newVersion);
    }

}
