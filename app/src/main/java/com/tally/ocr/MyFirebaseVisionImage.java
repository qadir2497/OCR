package com.tally.ocr;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

public class MyFirebaseVisionImage {

    private Context mContext = null;
    private Bitmap mBitmap = null;
    TextRecognizer mTextRecognizer = null;
    FirebaseVisionImage mfirebaseImage = null;

    MyFirebaseVisionImage(Context context, Bitmap bitmap) {
        mContext = context;
        mBitmap = bitmap;
        mTextRecognizer = TextRecognizer.getTextRecognizerInstance(mContext);
    }

    public void getImage() {
        mfirebaseImage = FirebaseVisionImage.fromBitmap(mBitmap);
        mTextRecognizer.recognizeText(mfirebaseImage);
    }
}
