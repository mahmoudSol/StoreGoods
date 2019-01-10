package h2mcom.android.storegoods.Data;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;
public class StoreContract {
    public StoreContract() {
    }
    public static final String CONTENT_AUTHORITY = "com.example.android.storegoods";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_STORE = "Goods";
    public static class StoreEntry implements BaseColumns {
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STORE;
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STORE);
        // create name of columses
        // first create table of name
        public static final String table_name = "Goods";
        //create column of ID
        public  static final String _ID = BaseColumns._ID;
        // create column of Product name
        public static  final  String Column_Of_Product = "Product";
        // create coulmun of quantity
        public static final String column_Of_Quntity = "Quantity";
        // create column of price
        public static final String coulmn_Of_Price = "Price";
        public static final String column_of_quantity_Unite = "QuantityUnite";
        public static final String column_of_price_Unite = "priceUnite";
        // create column of supplier
        public static final String coulmn_of_supplier = "Supplier";
        public static final String Coulmn_of_ImagePath = "Image_Path";
        public static final String coulmn_of_Barcode = "Barcode";
        public static final int Quantity_Item = 0;
        public static final int Quantity_Kg = 1;
        public static final int Quantity_Metre = 2;
        public static final int Price_Item = 0;
        public static final int Price_Kg = 1;
        public static final int Price_Metre = 2;
        public static boolean isValidQuantity(int quantity) {
            if (quantity == Quantity_Item || quantity == Quantity_Kg || quantity == Quantity_Metre) {
                return true;
            }
            return false;
        }
        public static boolean isValidPrice(int price) {
            if (price == Price_Item || price == Price_Kg || price == Price_Metre) {
                return true;
            }
            return false;
        }


    }
}
