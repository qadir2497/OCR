package com.tally.ocr;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
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

    private void runTextRecognition_ba(byte[] mSelectedImage) {
        FirebaseVisionImageMetadata metadata
                = new FirebaseVisionImageMetadata
                .Builder()
                .setRotation(0)
                .setWidth(1280)
                .setHeight(720)
                .build();

        FirebaseVisionImage image = FirebaseVisionImage. fromByteArray(mSelectedImage,metadata);


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
                                mTextView.setText("No Text");
                            }
                        });
    }

    private void activity2(){
        mTextView = this.findViewById(R.id.scannedtext);
        runTextRecognition_bm(photo_bm);

        //runTextRecognition_ba(photo_ba);

        this.findViewById(R.id.back)
                .setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                     activity1();
                    }
                });
    }

    private void activity1(){
        final Button i2t_button;

        setContentView(R.layout.activity_main);
        mImageView =  this.findViewById(R.id.Scanned_ImageView);
        i2t_button = this.findViewById(R.id.img2txt_button);

        this.findViewById(R.id.camera_button)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(cameraIntent, CAMERA_REQUEST);

                        i2t_button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                setContentView(R.layout.activity_t_analyzer);
                                activity2();
                            }
                        });
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity1();
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
           // Log.d("Prasad M",""+photo_ba.length);

        }
    }
}
