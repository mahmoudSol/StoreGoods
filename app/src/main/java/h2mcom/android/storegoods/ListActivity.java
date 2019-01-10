package h2mcom.android.storegoods;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import h2mcom.android.storegoods.Data.StoreContract;

public class ListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int Store_Loader = 0;

    StoreCurorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_list );

        FloatingActionButton fab = (FloatingActionButton)findViewById( R.id.fab_floating );

        fab.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( ListActivity.this,EditActivity.class );
                startActivity( intent );
            }
        } );

        ListView listView = (ListView)findViewById( R.id.list_of_goods );
        adapter = new StoreCurorAdapter( this,null );
        View emptyView = findViewById(R.id.empty_view);
        listView.setEmptyView( emptyView );
        listView.setAdapter( adapter );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent( ListActivity.this,EditActivity.class );
                Uri currentPetUri = ContentUris.withAppendedId( StoreContract.StoreEntry.CONTENT_URI,id);
                intent.setData( currentPetUri );
                startActivity( intent );
            }
        } );
        getLoaderManager().initLoader( Store_Loader,null,this );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu_list,menu );

        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_delete_all:
                deleteAllPets();
                return true;

        }
        return super.onOptionsItemSelected( item );
    }




    private void deleteAllPets() {
        int rowsDeleted = getContentResolver().delete( StoreContract.StoreEntry.CONTENT_URI, null, null);
        Log.v("CatalogActivity", rowsDeleted + " rows deleted from Store database");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String [] projection = {
                StoreContract.StoreEntry._ID, StoreContract.StoreEntry.Column_Of_Product, StoreContract.StoreEntry.coulmn_of_Barcode, StoreContract.StoreEntry.Coulmn_of_ImagePath
        };
        String selection = null;
        String selectionArgs[] =null;
        return new CursorLoader( getApplicationContext(), StoreContract.StoreEntry.CONTENT_URI,projection,selection,selectionArgs,null );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        adapter.swapCursor( data );

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor( null );

    }
}
