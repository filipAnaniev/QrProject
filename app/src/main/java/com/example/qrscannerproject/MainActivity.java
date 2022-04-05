package com.example.qrscannerproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    Button scanQRButton;
    Button generateQRButton;
    Button historyBTN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanQRButton = findViewById(R.id.scanBTN);
        generateQRButton = findViewById(R.id.generateBTN);
        historyBTN = findViewById(R.id.historyBTN);

        scanQRButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, ScanningActivity.class)));

        generateQRButton.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, GeneratingActivity.class)));

        historyBTN.setOnClickListener(view -> startActivity(new Intent(MainActivity.this, HistoryActivity.class)));

    }
}