package com.example.qrscannerproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class GeneratingActivity extends AppCompatActivity {
    EditText textInput;
    Button generateBTN;
    ImageView imageViewOutput;
    BarcodeEncoder barcodeEncoder;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generating);

        textInput = findViewById(R.id.inputText);
        generateBTN = findViewById(R.id.generateQrBTN);
        imageViewOutput = findViewById(R.id.outputImage);

        generateBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputString = textInput.getText().toString();
                barcodeEncoder = new BarcodeEncoder();
                bitmap = null;
                try {
                    bitmap = barcodeEncoder.encodeBitmap(inputString, BarcodeFormat.QR_CODE, 400,400);
                } catch (WriterException e) {
                    Toast.makeText(GeneratingActivity.this, e.toString(),Toast.LENGTH_SHORT).show();
                }
                imageViewOutput.setImageBitmap(bitmap);
            }
        });
    }
}