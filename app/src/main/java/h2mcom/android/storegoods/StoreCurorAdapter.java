package h2mcom.android.storegoods;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.sql.Blob;

import h2mcom.android.storegoods.Data.StoreContract;

public class StoreCurorAdapter extends CursorAdapter {
    public StoreCurorAdapter(Context context, Cursor c) {
        super( context, c, 0 );
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from( context ).inflate( R.layout.list_item,viewGroup,false );
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imageView = (ImageView)view.findViewById( R.id.photo_product );
        TextView textView = (TextView)view.findViewById( R.id.nameOfProduct );
        TextView textView1 = (TextView)view.findViewById( R.id.quantityOfProduct );

        int namecolumnprduct = cursor.getColumnIndex( StoreContract.StoreEntry.Column_Of_Product );
        int quantityproduct = cursor.getColumnIndex( StoreContract.StoreEntry.coulmn_of_Barcode );
        byte [] blob = cursor.getBlob( cursor.getColumnIndex( StoreContract.StoreEntry.Coulmn_of_ImagePath ) );
        if (blob == null){
            imageView.setImageResource( R.mipmap.no_photo );
        } else {
            ByteArrayInputStream inputStream = new ByteArrayInputStream( blob );
            Bitmap bitmap = BitmapFactory.decodeStream( inputStream );
            imageView.setImageBitmap( bitmap );
        }
        String nameofProduct = cursor.getString( namecolumnprduct );
        String quantityofProduct = cursor.getString( quantityproduct );
        if (TextUtils.isEmpty( nameofProduct )){
            nameofProduct = context.getString( R.string.dafault_name );
        }
        if (TextUtils.isEmpty( quantityofProduct )){
            quantityofProduct = context.getString( R.string.barcod_empty );
        }
        textView.setText( nameofProduct );
        textView1.setText( quantityofProduct );





    }
}
