package com.tally.ocr;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.TextView;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

public class MyFirebaseVisionImage {

    private Context mContext = null;
    private Bitmap mBitmap = null;
    TextRecognizer mTextRecognizer = null;
    FirebaseVisionImage mfirebaseImage = null;
    TextView mTextView = null;

    MyFirebaseVisionImage(Context context, Bitmap bitmap, TextView textView) {
        mContext = context;
        mBitmap = bitmap;
        mTextView = textView;
        mTextRecognizer = TextRecognizer.getTextRecognizerInstance(mContext, mTextView);
    }

    public void getImage() {
        mfirebaseImage = FirebaseVisionImage.fromBitmap(mBitmap);
        mTextRecognizer.recognizeText(mfirebaseImage);
    }
}
