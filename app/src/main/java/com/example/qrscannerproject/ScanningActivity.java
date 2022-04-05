package com.example.qrscannerproject;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.AutoFocusMode;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.budiyev.android.codescanner.ErrorCallback;
import com.budiyev.android.codescanner.ScanMode;
import com.google.zxing.Result;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class ScanningActivity extends AppCompatActivity {
    CodeScanner mCodeScanner;
    TextView outputText;
    Button historyButton;
    int CAMERA_REQUEST_CODE = 1010;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanning);
        outputText = findViewById(R.id.outputText);
        historyButton = findViewById(R.id.historyBTN);

        permissionCheck();
        scannerFunc();

        historyButton.setOnClickListener(view -> startActivity(new Intent(ScanningActivity.this, HistoryActivity.class)));
    }



    private void scannerFunc() {
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView);
        mCodeScanner.setCamera(CodeScanner.CAMERA_BACK);
        mCodeScanner.setScanMode(ScanMode.SINGLE);
        mCodeScanner.setAutoFocusMode(AutoFocusMode.SAFE);
        mCodeScanner.setFormats(CodeScanner.ALL_FORMATS);
        mCodeScanner.setFlashEnabled(false);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (outputText.getText().equals("Scan something..."))
                            outputText.setText(" ");

                        outputText.setText(result.getText());
                        writeToFile(result.getText(), ScanningActivity.this);
                    }
                });
            }
        });

        mCodeScanner.setErrorCallback(new ErrorCallback() {
            @Override
            public void onError(@NonNull Throwable thrown) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ScanningActivity.this, thrown.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        scannerView.setOnClickListener(view -> mCodeScanner.startPreview());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }



    private void permissionCheck() {
        int permissionStatus = ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA);

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            makeRequest();
        }
    }

    private void makeRequest() {
        ActivityCompat.requestPermissions(this,
                new String[]{android.Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length == 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(ScanningActivity.this,
                        "Camera permission is required to use this app!", Toast.LENGTH_SHORT).show();
            }
        }
    }



    private void writeToFile(String data, Context context) {
        String filename = "history.txt";
        try {
            OutputStreamWriter writer = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_APPEND));
            writer.append(data).append("\n");
            writer.close();
        } catch (IOException e) {
            Toast.makeText(ScanningActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

}