package fi.jamk.shoppinglist;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by pasi on 27/09/15.
 */
public class ProductContentProvider extends ContentProvider {
    private static final String AUTHORITY = "fi.jamk.shoppinglist.contentprovider";
    private static final String BASE_PATH = "products";

    public static final Uri CONTENT_URI = Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);
    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE+"/products";
    public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE+"product";

    private ProductDatabaseOpenHelper database;

    // Used for the UriMacher
    private static final int PRODUCTS   = 1;
    private static final int PRODUCT_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, BASE_PATH, PRODUCTS);
        sURIMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PRODUCT_ID);
    }

    @Override
    public boolean onCreate() {
        database = new ProductDatabaseOpenHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ProductDatabase.TABLE_PRODUCTS);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case PRODUCTS:
                break;
            case PRODUCT_ID:
                // Adding the ID to the original query
                queryBuilder.appendWhere("_id ="+uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }

        SQLiteDatabase db = database.getWritableDatabase();
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        // Make sure that potential listeners are getting notified
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = database.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case PRODUCTS:
                id = sqlDB.insert(ProductDatabase.TABLE_PRODUCTS, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(BASE_PATH + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        int count;
        switch (sURIMatcher.match(uri)) {
            case PRODUCTS:
                count = db.delete(ProductDatabase.TABLE_PRODUCTS,selection,selectionArgs);
                break;
            case PRODUCT_ID:
                String rowId = uri.getPathSegments().get(1);
                count = db.delete(
                        ProductDatabase.TABLE_PRODUCTS,
                        ProductDatabase.COLUMN_ID + "="+rowId + (!TextUtils.isEmpty(selection)? " AND ("+selection+')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI : "+ uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = database.getWritableDatabase();
        int count;
        switch (sURIMatcher.match(uri)) {
            case PRODUCTS:
                count = db.update(ProductDatabase.TABLE_PRODUCTS, values, selection, selectionArgs);
                break;
            case PRODUCT_ID:
                String rowId = uri.getPathSegments().get(1);
                count = db.update(ProductDatabase.TABLE_PRODUCTS, values,
                        ProductDatabase.COLUMN_ID + "="+rowId+
                                (!TextUtils.isEmpty(selection)? " AND ("+selection+')' : ""),
                        selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;

    }
}
