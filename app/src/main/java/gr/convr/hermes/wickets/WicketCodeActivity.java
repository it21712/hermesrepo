package gr.convr.hermes.wickets;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;
import gr.convr.hermes.R;

public class WicketCodeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wicket_code);

        ImageView qrImage = findViewById(R.id.wicket_code_img);

        generateQR("sample_id", 400, qrImage);
    }

    private void generateQR(String text, int size, ImageView qrImage){

        QRGEncoder encoder = new QRGEncoder(text, null, QRGContents.Type.TEXT, size);
        try {
            Bitmap bitmap = encoder.encodeAsBitmap();
            qrImage.setImageBitmap(bitmap);
        } catch (WriterException e) {
            Log.v("QR WRITER STATUS", e.toString());
        }

    }
}
