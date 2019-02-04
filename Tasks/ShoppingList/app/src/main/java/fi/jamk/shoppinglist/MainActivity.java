package fi.jamk.shoppinglist;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor>,
        AddDialogFragment.DialogListener{

    // This is the Adapter being used to display the list's data.
    private SimpleCursorAdapter adapter;
    // Context Menu for delete
    private final int UPDATE_ID = 0;
    private final int DELETE_ID = 1;
    // list view
    private ListView listView;
    private static SQLiteDatabase sqliteDB = null;
    private TextView txtView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // find list view
        listView = (ListView)  findViewById(R.id.listView);
        // register listView's context menu (to delete row)
        registerForContextMenu(listView);
        showProducts();
        Log.e("boo", String.valueOf(getSumm()));
        txtView = (TextView) findViewById(R.id.total);
        txtView.setText("Total: " + getSumm());
    }

    private float getSumm() {
        float summ = 0;
        sqliteDB = this.openOrCreateDatabase("products.db", MODE_PRIVATE, null);
        Cursor cursor = sqliteDB.rawQuery("SELECT * FROM " + ProductDatabase.TABLE_PRODUCTS, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            summ  = summ + cursor.getInt(cursor.getColumnIndex("count")) * cursor.getFloat(cursor.getColumnIndex("price"));
            cursor.moveToNext();
        }
        return summ;
    }

    // show in listview
    private void showProducts() {
        // Fields from the database (projection)
        String[] from = new String[] { ProductDatabase.COLUMN_PRODUCT, ProductDatabase.COLUMN_COUNT, ProductDatabase.COLUMN_PRICE };
        // Fields on the UI to which we map
        int[] to = new int[] { R.id.product, R.id.count, R.id.price};
        // init loader, call data if needed
        getLoaderManager().initLoader(0, null, this);
        adapter = new SimpleCursorAdapter(this, R.layout.list_item, null, from, to, 0);
        // show data in listView
        listView.setAdapter(adapter);
    }

    /** LOADER BELOW THIS ONE **/

    // Creates a new loader after the initLoader () call
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                ProductDatabase.COLUMN_ID,
                ProductDatabase.COLUMN_PRODUCT,
                ProductDatabase.COLUMN_COUNT,
                ProductDatabase.COLUMN_PRICE};
        CursorLoader cursorLoader = new CursorLoader(
                this,
                ProductContentProvider.CONTENT_URI,
                projection, null, null, "count DESC");

        return cursorLoader;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // new data is available, use it
        adapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // data is not available anymore, delete reference
        adapter.swapCursor(null);
    }

    /* insert */
    @Override
    public void onDialogPositiveClick(DialogFragment dialog, String product, int count, float price) {
        // add a new data
        ContentValues values=new ContentValues(3);
        values.put("product", product);
        values.put("count", count);
        values.put("price", price);
        getContentResolver().insert(ProductContentProvider.CONTENT_URI, values);
        Log.e("boo", String.valueOf(getSumm()));
        txtView.setText("Total: " + getSumm());
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        // TODO Auto-generated method stub
    }

    /* delete */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, UPDATE_ID, Menu.NONE, "Update");
        menu.add(Menu.NONE, DELETE_ID, Menu.NONE, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        switch (item.getItemId()) {

            case DELETE_ID:
                //Log.d("DELETE","DELETE");
                String[] args = {String.valueOf(info.id)};
                // url, where, args
                getContentResolver().delete(ProductContentProvider.CONTENT_URI, "_id=?",  args);
                txtView.setText("Total: " + getSumm());
                break;

            case UPDATE_ID:
                // find layout and name
                RelativeLayout layout = (RelativeLayout) info.targetView;
                TextView textView = (TextView) layout.getChildAt(0); // name is first element
                String product = (String) textView.getText();
                TextView textView2 = (TextView) layout.getChildAt(1); // name is first element
                int count = Integer.parseInt(textView2.getText().toString());
                TextView textView3 = (TextView) layout.getChildAt(2); // name is first element
                float price = Float.parseFloat(textView3.getText().toString());
                // generate values
                ContentValues values = new ContentValues(3);
                values.put("product", product);
                values.put("count", count);
                values.put("price", price);
                String[] args2 = {String.valueOf(info.id)};
                // uri, content values, string where, args
                getContentResolver().update(ProductContentProvider.CONTENT_URI, values, "_id=?", args2);
                break;

        }

        return(super.onOptionsItemSelected(item));
    }

    /** MENU BELOW THIS ONE **/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                AddDialogFragment eDialog = new AddDialogFragment();
                eDialog.show(getFragmentManager(), "Add a new product");
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
