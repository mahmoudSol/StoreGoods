package h2mcom.android.storegoods;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.app.Service;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import h2mcom.android.storegoods.Data.StoreContract;
import me.drakeet.materialdialog.MaterialDialog;

public class EditActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private EditText nameOfProduct;
    private EditText quantity;
    private EditText price;
    private EditText supplier;
    private Button take_photo;
    private ImageView imageshow;
    private static final int EXISTING_Store_LOADER = 0;
    private Bitmap mphoto;
    private Uri mCurrentPetUri;
    final private int REQUEST_CODE_WRITE_STORAGE = 1;
    Uri uri;
    private final int SELECT_PHOTO = 101;
    private final int CAPTURE_PHOTO = 102;
    private boolean mPetHasChanged = false;
    private Button takeBarcod;
    private TextView showBarcod;
    String numbercode;
    private Spinner mspinnerquantity;
    private Spinner mspinnerprice;
    private int mQuantity;
    private int mPrice;
    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mPetHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mPetHasChanged = true;
            return false;
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit );
        Intent intent = getIntent();
        mCurrentPetUri = intent.getData();
        if (mCurrentPetUri == null) {
            setTitle( getString( R.string.editore_add_item ) );
            invalidateOptionsMenu();
        } else {
            setTitle( getString( R.string.editore_edit_item ) );
            getLoaderManager().initLoader( EXISTING_Store_LOADER, null, this );
        }
        nameOfProduct = (EditText) findViewById( R.id.Product_name );
        quantity = (EditText) findViewById( R.id.Quantity );
        price = (EditText) findViewById( R.id.Price );
        supplier = (EditText) findViewById( R.id.Supplier );
        takeBarcod = (Button)findViewById( R.id.barcode_button );
        mspinnerprice = (Spinner)findViewById( R.id.spinner_price );
        mspinnerquantity = (Spinner)findViewById( R.id.spinner_quantity );
        showBarcod = (TextView)findViewById( R.id.Show_theBarcode );
        takeBarcod.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent( EditActivity.this,BarcodActivity.class );
                startActivityForResult( intent,1 );
            }
        } );
        takephoto();
        setupSpinner();
    }

     private void setupSpinner(){
         ArrayAdapter quantitySpinnerAdapter = ArrayAdapter.createFromResource( this,
                 R.array.array_quantity_options, android.R.layout.simple_spinner_item );

         // Specify dropdown layout style - simple list view with 1 item per line
         quantitySpinnerAdapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line );

         // Apply the adapter to the spinner
         mspinnerquantity.setAdapter( quantitySpinnerAdapter );
        mspinnerquantity.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String seleection =(String) parent.getItemAtPosition( position );
                if (!TextUtils.isEmpty( seleection )) {
                    if (seleection.equals( getString( R.string.Item_unite ) )) {
                        mQuantity = StoreContract.StoreEntry.Quantity_Item;

                    } else if (seleection.equals( getString( R.string.Kg_unite ) )) {
                        mQuantity = StoreContract.StoreEntry.Quantity_Kg;

                    } else if (seleection.equals( getString( R.string.Metre_unite ) )) {
                        mQuantity = StoreContract.StoreEntry.Quantity_Metre;
                    }}        }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        } );
         ArrayAdapter priceSpinnerAdapter = ArrayAdapter.createFromResource( this,
                 R.array.array_price_option, android.R.layout.simple_spinner_item );
         priceSpinnerAdapter.setDropDownViewResource( android.R.layout.simple_dropdown_item_1line );
         mspinnerprice.setAdapter( quantitySpinnerAdapter );
         mspinnerprice.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
             @Override
             public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String seleection =(String) parent.getItemAtPosition( position );
                 if (!TextUtils.isEmpty( seleection )) {
                     if (seleection.equals( getString( R.string.Item_unite ) )) {
                         mPrice = StoreContract.StoreEntry.Price_Item;

                     } else if (seleection.equals( getString( R.string.Kg_unite ) )) {
                         mPrice = StoreContract.StoreEntry.Price_Kg;

                     } else if (seleection.equals( getString( R.string.Metre_unite ) )){
                         mPrice = StoreContract.StoreEntry.Price_Metre;

                     }
                 }
             }

             @Override
             public void onNothingSelected(AdapterView<?> parent) {

             }
         } );
 }



    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( R.string.delete_dialog_msg );
        builder.setPositiveButton( R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the pet.
                deletePet();
            }
        } );
        builder.setNegativeButton( R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        } );

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void deletePet() {
        // Only perform the delete if this is an existing pet.
        if (mCurrentPetUri != null) {
            // Call the ContentResolver to delete the pet at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentPetUri
            // content URI already identifies the pet that we want.
            int rowsDeleted = getContentResolver().delete( mCurrentPetUri, null, null );
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText( this, getString( R.string.editor_delete_pet_failed ),
                        Toast.LENGTH_SHORT ).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText( this, getString( R.string.editor_delete_pet_successful ),
                        Toast.LENGTH_SHORT ).show();
            }
        }
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate( R.menu.menu_edit, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_button:
                saveItem();
                finish();
                return true;
            case R.id.delete_edit:
                showDeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                if (!mPetHasChanged) {
                    NavUtils.navigateUpFromSameTask( EditActivity.this );
                    return true;
                }
                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask( EditActivity.this );
                            }
                        };
                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog( discardButtonClickListener );
                return true;
        }
        return super.onOptionsItemSelected( item );        }


    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setMessage( R.string.unsaved_changes_dialog_msg );
        builder.setPositiveButton( R.string.discard, discardButtonClickListener );
        builder.setNegativeButton( R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        } );
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    public void takephoto() {
        take_photo = (Button) findViewById( R.id.Take_Phot_button );
        imageshow = (ImageView) findViewById( R.id.imgv_photo );
        take_photo.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hasWriteStoragePermission = 0;
                if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    hasWriteStoragePermission = checkSelfPermission( android.Manifest.permission.WRITE_EXTERNAL_STORAGE );
                }
                if (hasWriteStoragePermission != PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        requestPermissions( new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_CODE_WRITE_STORAGE );
                    }
                }
                listDialog();
            }
        } );
    }
    public void listDialog() {
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>( this, android.R.layout.simple_list_item_1 );
        arrayAdapter.add( "Take Photo" );
        arrayAdapter.add( "Select Gallery" );
        ListView listView = new ListView( this );
        listView.setLayoutParams( new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT ) );
        float scale = getResources().getDisplayMetrics().density;
        int dpAsPixels = (int) (8 * scale + 0.5f);
        listView.setPadding( 0, dpAsPixels, 0, dpAsPixels );
        listView.setDividerHeight( 0 );
        listView.setAdapter( arrayAdapter );
        final MaterialDialog alert = new MaterialDialog( this ).setContentView( listView );
        alert.setPositiveButton( "Cancel", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        } );
        listView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    alert.dismiss();
                    Intent i = new Intent( MediaStore.ACTION_IMAGE_CAPTURE );//Uri uri  = Uri.parse("file:///sdcard/photo.jpg");
                    String root = Environment.getExternalStoragePublicDirectory( Environment.DIRECTORY_PICTURES ).toString() + "propic.jpg";
                    uri = Uri.parse( root );//i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                    startActivityForResult( i, CAPTURE_PHOTO );
                } else {
                    alert.dismiss();
                    Intent photoPickerIntent = new Intent( Intent.ACTION_PICK );
                    photoPickerIntent.setType( "image/*" );
                    startActivityForResult( photoPickerIntent, SELECT_PHOTO );
                }
            }
        } );
        alert.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult( requestCode, resultCode, imageReturnedIntent );
        if (requestCode == 1){
            if (resultCode == RESULT_OK){
                 numbercode = imageReturnedIntent.getStringExtra( "QR_Code" );
                showBarcod.setText( numbercode );
            } }
        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Uri imageUri = imageReturnedIntent.getData();
                     mphoto = decodeUri(imageUri,100);
                     imageshow.setImageBitmap( mphoto );
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream(  );
                    mphoto.compress( Bitmap.CompressFormat.JPEG,100, bytes );
                    File file = new File( Environment.getExternalStorageDirectory()+ File.separator + "images" + ".jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream fo = new FileOutputStream( file );
                        fo.write( bytes.toByteArray() );
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText( this,"Oops not able to capture image.",Toast.LENGTH_SHORT ).show();
                    } }
                break;
            case CAPTURE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mphoto = imageReturnedIntent.getExtras().getParcelable("data");
                    imageshow.setImageBitmap(mphoto);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream(  );
                    mphoto.compress( Bitmap.CompressFormat.JPEG,100, bytes );
                    File file = new File( Environment.getExternalStorageDirectory()+ File.separator + "images" + ".jpg");
                    try {
                        file.createNewFile();
                        FileOutputStream fo = new FileOutputStream( file );
                        fo.write( bytes.toByteArray() );
                        fo.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText( this,"Oops not able to capture image.",Toast.LENGTH_SHORT ).show();
                    }
                }
                break;
                }
    }
    //COnvert and resize our image to 400dp for faster uploading our images to DB
    protected Bitmap decodeUri(Uri selectedImage, int REQUIRED_SIZE) {
        try {
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);
            // The new size we want to scale to// final int REQUIRED_SIZE =  size;// Find the correct scale value. It should be the power of 2.
            int width_tmp = o.outWidth, height_tmp = o.outHeight;
            int scale = 1;
            while (true) {
                if (width_tmp / 2 < REQUIRED_SIZE
                        || height_tmp / 2 < REQUIRED_SIZE) {
                    break;
                }
                width_tmp /= 2;
                height_tmp /= 2;
                scale *= 2;
            }// Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }




    private void saveItem(){
        String nameofItem = nameOfProduct.getText().toString().trim();
        String priceOfItem = ( price.getText().toString().trim() );
        String quantityOfItem = ( quantity.getText().toString().trim() ) ;
        String supplierOfItem = supplier.getText().toString().trim();
        if (mCurrentPetUri == null && TextUtils.isEmpty( nameofItem ) && TextUtils.isEmpty( priceOfItem ) && TextUtils.isEmpty( quantityOfItem ) && TextUtils.isEmpty( supplierOfItem )&& TextUtils.isEmpty( numbercode )) {// Since no fields were modified, we can return early without creating a new item. // No need to create ContentValues and no need to do any ContentProvider operations.
            return;}
        ContentValues values = new ContentValues(  );
        values.put( StoreContract.StoreEntry.Column_Of_Product,nameofItem );
        values.put( StoreContract.StoreEntry.coulmn_Of_Price,priceOfItem );
        values.put( StoreContract.StoreEntry.column_of_quantity_Unite , mQuantity );
        values.put( StoreContract.StoreEntry.column_of_price_Unite,mPrice );
        values.put( StoreContract.StoreEntry.column_Of_Quntity,quantityOfItem );
        values.put( StoreContract.StoreEntry.coulmn_of_supplier,supplierOfItem );
        if (numbercode != null){
            values.put( StoreContract.StoreEntry.coulmn_of_Barcode,numbercode );}
        if (mphoto != null) {
            ByteArrayOutputStream stream =new ByteArrayOutputStream(  );
            mphoto.compress( Bitmap.CompressFormat.PNG,0,stream );
            values.put( StoreContract.StoreEntry.Coulmn_of_ImagePath,stream.toByteArray() );}
        if (mCurrentPetUri == null) {// This is a NEW Item, so insert a new Item into the provider,// returning the content URI for the new pet.
            Uri newUri = getContentResolver().insert( StoreContract.StoreEntry.CONTENT_URI, values );// Show a toast message depending on whether or not the insertion was successful.
            if (newUri == null) {// If the new content URI is null, then there was an error with insertion.
                Toast.makeText( this, getString( R.string.editor_insert_product_failed ),
                        Toast.LENGTH_SHORT ).show();
            } else {// Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText( this, getString( R.string.editor_insert_product_successful ),
                        Toast.LENGTH_SHORT ).show();}
        } else {// Otherwise this is an EXISTING Item, so update the Item with content URI: mCurrentPetUri// and pass in the new ContentValues. Pass in null for the selection and selection args// because mCurrentPetUri will already identify the correct row in the database that// we want to modify.
            int rowsAffected = getContentResolver().update( mCurrentPetUri, values, null, null );// Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {// If no rows were affected, then there was an error with the update.
                Toast.makeText( this, getString( R.string.editor_update_Item_failed ),
                        Toast.LENGTH_SHORT ).show();
            } else {// Otherwise, the update was successful and we can display a toast.
                Toast.makeText( this, getString( R.string.editor_update_Item_successful ),
                        Toast.LENGTH_SHORT ).show();
            } } }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                StoreContract.StoreEntry._ID,
                StoreContract.StoreEntry.Column_Of_Product,
                StoreContract.StoreEntry.coulmn_Of_Price,
                StoreContract.StoreEntry.column_of_price_Unite,
                StoreContract.StoreEntry.column_Of_Quntity,
                StoreContract.StoreEntry.column_of_quantity_Unite,
                StoreContract.StoreEntry.coulmn_of_supplier,
                StoreContract.StoreEntry.Coulmn_of_ImagePath,
                StoreContract.StoreEntry.coulmn_of_Barcode};

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader( this,   // Parent activity context
                mCurrentPetUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null );
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        if (cursor == null || cursor.getCount() < 1) {
            return;
        }

        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = cursor.getColumnIndex( StoreContract.StoreEntry.Column_Of_Product );
            int priceColumnIndex = cursor.getColumnIndex( StoreContract.StoreEntry.coulmn_Of_Price );
            int priceUnitIndex = cursor.getColumnIndex( StoreContract.StoreEntry.column_of_price_Unite );
            int quantityColumnIndex = cursor.getColumnIndex( StoreContract.StoreEntry.column_Of_Quntity );
            int quantityUniteIndex = cursor.getColumnIndex( StoreContract.StoreEntry.column_of_quantity_Unite );
            int supplierColumnIndex = cursor.getColumnIndex( StoreContract.StoreEntry.coulmn_of_supplier );
            int barcodecolumnIndex = cursor.getColumnIndex( StoreContract.StoreEntry.coulmn_of_Barcode );

            // Extract out the value from the Cursor for the given column index
            String name = cursor.getString( nameColumnIndex );
            double priceofproduct = cursor.getDouble( priceColumnIndex );
            int priceunit = cursor.getInt( priceUnitIndex );
            double quantityofproduct = cursor.getDouble( quantityColumnIndex );
            int quantityunit= cursor.getInt( quantityUniteIndex );
            String supplierofproduct = cursor.getString( supplierColumnIndex );
            String bar = cursor.getString( barcodecolumnIndex );
            byte [] blob = cursor.getBlob( cursor.getColumnIndex( StoreContract.StoreEntry.Coulmn_of_ImagePath ) );
            if (blob == null){
                imageshow.setImageResource( R.drawable.ic_tag_faces_black_24dp );
            } else {
                ByteArrayInputStream inputStream = new ByteArrayInputStream( blob );
                Bitmap bitmap = BitmapFactory.decodeStream( inputStream );


                // Update the views on the screen with the values from the database
                imageshow.setImageBitmap( bitmap );
            }
            nameOfProduct.setText( name );
            price.setText( Double.toString( priceofproduct ) );
            quantity.setText( Double.toString( quantityofproduct ) ) ;
            supplier.setText( supplierofproduct );
            showBarcod.setText( bar );
            switch (quantityunit){
                case StoreContract.StoreEntry.Quantity_Item:
                    mspinnerquantity.setSelection( 0 );
                    break;
                case StoreContract.StoreEntry.Quantity_Kg:
                    mspinnerquantity.setSelection( 1 );
                    break;
                case StoreContract.StoreEntry.Quantity_Metre:
                    mspinnerquantity.setSelection( 2 );
                    default:
                        mspinnerquantity.setSelection( 0 );
                        break;
            }
            switch (priceunit){
                case StoreContract.StoreEntry.Price_Item:
                    mspinnerprice.setSelection( 0 );
                    break;
                case StoreContract.StoreEntry.Price_Kg:
                    mspinnerprice.setSelection( 1 );
                    break;
                case StoreContract.StoreEntry.Price_Metre:
                    mspinnerprice.setSelection( 2 );
                default:
                    mspinnerprice.setSelection( 0 );
                    break;
            }}}
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        nameOfProduct.setText( "" );
        price.setText( "" );
        quantity.setText( "" );
        supplier.setText( "");
        showBarcod.setText( "" );
        imageshow.setImageResource( R.drawable.ic_tag_faces_black_24dp );
        mspinnerprice.setSelection( 0 );
        mspinnerquantity.setSelection( 0 );

    }
}
