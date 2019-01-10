package h2mcom.android.storegoods.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class StoreDpHelper extends SQLiteOpenHelper {
    private static final String Date_Base_file = "Store.db";
    private static  int Datebase_version = 2;

    public StoreDpHelper(Context context) {
        super( context, Date_Base_file, null, Datebase_version );
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String Sql_Lite_DateBase = "CREATE TABLE " + StoreContract.StoreEntry.table_name + " ("
                + StoreContract.StoreEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + StoreContract.StoreEntry.Column_Of_Product + " TEXT NOT NULL, "
                + StoreContract.StoreEntry.column_Of_Quntity + " REAL NOT NULL DEFAULT 0,"
                + StoreContract.StoreEntry.coulmn_Of_Price + " REAL NOT NULL, "
                + StoreContract.StoreEntry.coulmn_of_supplier + " TEXT, "
                + StoreContract.StoreEntry.Coulmn_of_ImagePath + " BLOB, "
                + StoreContract.StoreEntry.coulmn_of_Barcode + " TEXT, "
                + StoreContract.StoreEntry.column_of_price_Unite + " INTEGER NOT NULL, "
                + StoreContract.StoreEntry.column_of_quantity_Unite + " INTEGER NOT NULL);";
      db.execSQL( Sql_Lite_DateBase );
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
