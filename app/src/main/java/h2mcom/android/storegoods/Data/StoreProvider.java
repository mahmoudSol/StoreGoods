package h2mcom.android.storegoods.Data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
public class StoreProvider extends ContentProvider {
    private StoreDpHelper mStoreDpHelper;
    public static final String LOG_TAG = StoreProvider.class.getSimpleName();
    private static final int Store = 100;
    private static final int Store_ID = 101;
    private static final UriMatcher mUriMatcher = new UriMatcher( UriMatcher.NO_MATCH );
    static {
        mUriMatcher.addURI( StoreContract.CONTENT_AUTHORITY,StoreContract.PATH_STORE,Store );
        mUriMatcher.addURI( StoreContract.CONTENT_AUTHORITY,StoreContract.PATH_STORE+"/#",Store_ID );
    }
    @Override
    public boolean onCreate() {
        mStoreDpHelper = new StoreDpHelper( getContext() );
        return true;
    }
    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase database = mStoreDpHelper.getReadableDatabase();
        Cursor cursor = null;
        int match =mUriMatcher.match( uri );
        switch (match) {
            case Store:
                cursor = database.query( StoreContract.StoreEntry.table_name, projection, selection, selectionArgs, null, null, sortOrder );
                break;
            case Store_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf( ContentUris.parseId( uri ) )};
                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query( StoreContract.StoreEntry.table_name, projection, selection, selectionArgs,
                        null, null, sortOrder );
                break;
            default:
                throw new IllegalArgumentException( "Cannot query unknown URI " + uri );
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int match = mUriMatcher.match(uri);
        switch (match) {
            case Store:
                return StoreContract.StoreEntry.CONTENT_LIST_TYPE;
            case Store_ID:
                return StoreContract.StoreEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }

    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int match = mUriMatcher.match( uri );
        switch (match){
            case Store:
                return insertItem( uri,values );
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertItem(Uri uri, ContentValues values) {
        String name = values.getAsString( StoreContract.StoreEntry.Column_Of_Product);
        Integer mquantity = values.getAsInteger( StoreContract.StoreEntry.column_of_quantity_Unite);
        Integer mprice = values.getAsInteger( StoreContract.StoreEntry.column_of_price_Unite);
        Double quantity = values.getAsDouble( StoreContract.StoreEntry.column_Of_Quntity);
        Double Price = values.getAsDouble( StoreContract.StoreEntry.coulmn_Of_Price);
        String nameOfSupplier = values.getAsString( StoreContract.StoreEntry.coulmn_of_supplier );
        byte[] Photo = values.getAsByteArray( StoreContract.StoreEntry.Coulmn_of_ImagePath );
        String Barcode = values.getAsString( StoreContract.StoreEntry.coulmn_of_Barcode );
        // Get writeable database
        SQLiteDatabase database = mStoreDpHelper.getWritableDatabase();
        long id = database.insert(StoreContract.StoreEntry.table_name, null, values);
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase database = mStoreDpHelper.getWritableDatabase();
        int rowDeletIt;
        int match = mUriMatcher.match( uri );
        switch (match){
            case Store:
                rowDeletIt = database.delete( StoreContract.StoreEntry.table_name,selection,selectionArgs );
                break;
            case Store_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowDeletIt = database.delete( StoreContract.StoreEntry.table_name,selection,selectionArgs );
            break;
           default:
               throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (rowDeletIt != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowDeletIt;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = mUriMatcher.match( uri );
        switch (match){
            case Store:
                return updateItem(uri , values , selection ,selectionArgs);
            case Store_ID:
                selection = StoreContract.StoreEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateItem(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int updateItem (Uri uri , ContentValues values ,String selection , String[] selectionArgs){
        if (values.containsKey( StoreContract.StoreEntry.Column_Of_Product)) {
            String name = values.getAsString( StoreContract.StoreEntry.Column_Of_Product);
            if (name == null) {
                throw new IllegalArgumentException("Item requires a name");
            }
        }
        if (values.containsKey( StoreContract.StoreEntry.column_Of_Quntity)) {
            Double quantity = values.getAsDouble( StoreContract.StoreEntry.column_Of_Quntity);
            if (quantity == null ) {
                throw new IllegalArgumentException("Item requires valid quantity");
            }
        }
        if (values.containsKey( StoreContract.StoreEntry.column_of_quantity_Unite)) {
            Integer uquantity = values.getAsInteger( StoreContract.StoreEntry.column_of_quantity_Unite);
            if (uquantity == null || !StoreContract.StoreEntry.isValidQuantity(uquantity)) {
                throw new IllegalArgumentException("Item requires valid Unit Of Quantity");
            }
        }
        if (values.containsKey( StoreContract.StoreEntry.coulmn_Of_Price)) {
            // Check that the weight is greater than or equal to 0 kg
            Double price = values.getAsDouble( StoreContract.StoreEntry.coulmn_Of_Price);
            if (price != null && price < 0) {
                throw new IllegalArgumentException("Item requires valid price");
            }
        }
        if (values.containsKey( StoreContract.StoreEntry.column_of_price_Unite)) {
            Integer uprice = values.getAsInteger( StoreContract.StoreEntry.column_of_price_Unite);
            if (uprice == null || !StoreContract.StoreEntry.isValidPrice(uprice)) {
                throw new IllegalArgumentException("Item requires valid Unit Of Price");
            }
        }
        if (values.size() == 0) {
            return 0;
        }
        SQLiteDatabase database = mStoreDpHelper.getWritableDatabase();
        int rowsUpdated = database.update( StoreContract.StoreEntry.table_name, values, selection, selectionArgs);
        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}
