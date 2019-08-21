package com.tally.ocr;

import android.graphics.Bitmap;

import com.google.firebase.ml.vision.common.FirebaseVisionImage;

;

public class MyFirebaseVisionImage {

    public FirebaseVisionImage getImage(Bitmap bitmap) {
        return FirebaseVisionImage.fromBitmap(bitmap);
    }


}
