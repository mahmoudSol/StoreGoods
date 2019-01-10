package h2mcom.android.storegoods;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class BarcodActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{


    private ZXingScannerView mzxing;
    private static String outputResult = "";
    private AlertDialog mmmm;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_barcod );
        mzxing = new ZXingScannerView( this );
        setContentView( mzxing );
        mzxing.setResultHandler( this );
        mzxing.startCamera();
    }


    @Override
    public void handleResult(final Result result) {
        try {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(this.getApplicationContext(), notification);
        r.play();
    } catch (Exception e) {

    }
        outputResult = result.getText();
        final AlertDialog.Builder alert = new AlertDialog.Builder( this );
        alert.setTitle( "Barcode of Item" );

        alert.setMessage( "the Barcode of your Item is " + outputResult );
        alert.setPositiveButton( "Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mzxing.stopCamera();
                Intent intent = new Intent(  );
                intent.putExtra( "QR_Code",outputResult );
                setResult( RESULT_OK,intent );
                finish();
            }
        } );


      alert.show();

    }


}
