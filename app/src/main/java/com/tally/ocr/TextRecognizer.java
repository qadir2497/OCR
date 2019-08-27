package com.tally.ocr;

import android.content.Context;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;

public class TextRecognizer {

    private TextRecognizer() {
    }

    private static Context mContext = null;
    private static TextView mTextView = null;

    public static TextRecognizer mTextRecognizerInstance = null;

    public static synchronized TextRecognizer getTextRecognizerInstance(Context context, TextView textView) {
        if (mTextRecognizerInstance == null)
            mTextRecognizerInstance = new TextRecognizer();
        mContext = context;
        mTextView = textView;
        return mTextRecognizerInstance;
    }

    public void recognizeText(FirebaseVisionImage image) {

        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance().getCloudTextRecognizer();

        recognizer.processImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                Log.v("result", "task complted");
                processTextBlock(firebaseVisionText);
            }
        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                mTextView.setText("Unable to Process");
                                e.printStackTrace();
                            }
                        });
    }

    private void processTextBlock(FirebaseVisionText text) {
        List<FirebaseVisionText.TextBlock> blocks = text.getTextBlocks();

        if(blocks.size()==0)
        {
            mTextView.append("Unable to Process");
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
}
