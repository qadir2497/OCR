package com.tally.ocr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    private static final int CAMERA_REQUEST = 1888;
    Bitmap photo_bm;
     byte[] photo_ba;

    private ImageView mImageView;
    private TextView mTextView;

    private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();

        if(blocks.size()==0)
        {
            mTextView.setError("No Text");
         }
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            int k=0;
            for(int j=0;j<lines.size();j++) {
                String s = lines.get(j).getText();
                mTextView.append(s);
                mTextView.append("\n");
            }
        }

    }

    private void runTextRecognition_bm(Bitmap mSelectedImage) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(mSelectedImage);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        recognizer.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
                                processTextRecognitionResult(texts);
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mTextView.setError("No Text");
                            }
                        });
    }
    private void activity1() {
        final Button i2t_button;

        setContentView(R.layout.activity_main2);
        mImageView = this.findViewById(R.id.imageView);
        i2t_button = this.findViewById(R.id.img2txt_button);

        this.findViewById(R.id.Scanned_ImageView)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        i2t_button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                activity2();
                            }
                        });
                    }
                });

        this.findViewById(R.id.back_activity1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity_main();
            }
        });
    }


        private void activity2(){
            setContentView(R.layout.activity_2);
            mTextView = this.findViewById(R.id.scannedtext);

            runTextRecognition_bm(photo_bm);

            this.findViewById(R.id.back_activity2)
                    .setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            activity1();
                        }
                    });
    }

    /*
    private Bitmap TextToImageEncode(String Value) {
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(
                    Value,
                    FirebaseVisionBarcode.BarcodeFormat.DATA_MATRIX.QR_CODE,
                    QRcodeWidth, QRcodeWidth, null
            );

        } catch (IllegalArgumentException Illegalargumentexception) {

            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();

        int bitMatrixHeight = bitMatrix.getHeight();

        int[] pixels = new int[bitMatrixWidth * bitMatrixHeight];

        for (int y = 0; y < bitMatrixHeight; y++) {
            int offset = y * bitMatrixWidth;

            for (int x = 0; x < bitMatrixWidth; x++) {

                pixels[offset + x] = bitMatrix.get(x, y) ?
                        getResources().getColor(R.color.QRCodeBlackColor):getResources().getColor(R.color.QRCodeWhiteColor);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth, bitMatrixHeight, Bitmap.Config.ARGB_4444);

        bitmap.setPixels(pixels, 0, 350, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;
    }
*/
    void activity3()
    {
        setContentView(R.layout.activity_qr_activity1);

        final AutoCompleteTextView qr_entered_text = this.findViewById(R.id.qr_entered_text);

        final String s =  qr_entered_text.getText().toString();

        this.findViewById(R.id.scan_qr_activity1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d("Prasad:/n",s);
               // Bitmap bitmap = new Bitmap(s);
                                //activity4(bitmap);
            }
        });

    }

    void activity4(Bitmap bitmap)
    {
        setContentView(R.layout.activity_qr_activity2);

        ImageView mImageView =  this.findViewById(R.id.qr_image);

        mImageView.setImageBitmap(bitmap);

        this.findViewById(R.id.scan_qr_activity2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity3();
            }
        });

    }
    private void activity_main()
    {
        setContentView(R.layout.entryactivity);

        this.findViewById(R.id.ocr_button).setOnClickListener( new View.OnClickListener(){
                @Override
                public void onClick(View v){activity1();}}
            );

        this.findViewById(R.id.qr_scanner_button).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity3();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity_main();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "camera permission granted", Toast.LENGTH_LONG).show();
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
           photo_bm = (Bitmap) data.getExtras().get("data");
            mImageView.setImageBitmap(photo_bm);

           photo_ba =  data.getExtras().getByteArray("");
           photo_ba = data.getByteArrayExtra("");

        }
    }
}
