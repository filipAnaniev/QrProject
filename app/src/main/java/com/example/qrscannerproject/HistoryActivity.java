package com.example.qrscannerproject;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HistoryActivity extends AppCompatActivity {
    ListView historyList;
    String filename = "history.txt";
    Vibrator vibe;
    ArrayAdapter<String> adapter;
    ArrayList<String> arrList;
    Button clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        arrList = new ArrayList<>();
        historyList = findViewById(R.id.HistoryList);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, arrList);
        historyList.setAdapter(adapter);
        clear = findViewById(R.id.clearBTN);
        vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        loadData(HistoryActivity.this);

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(historyList.getCount()==0){
                    Toast.makeText(HistoryActivity.this, "There are no entries!", Toast.LENGTH_SHORT).show();
                }else{
                    deleteHistoryDialogue("Are you sure you want to delete the entire history?");
                }
            }
        });

        historyList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Object obj = adapter.getItem(i);
                Uri uri = Uri.parse(obj.toString());
                if(URLUtil.isValidUrl(obj.toString()) || URLUtil.isHttpsUrl(obj.toString())|| URLUtil.isHttpUrl(obj.toString())){
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    startActivity(intent);
                }else{
                    ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText(null,obj.toString());
                    clipboardManager.setPrimaryClip(clip);
                    Toast.makeText(HistoryActivity.this, "Text was copied to clipboard.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        historyList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                vibe.vibrate(100);
                deleteEntryDialogue("Are you sure you want to delete this entry?", i);
                return false;
            }
        });
    }

    private void loadData(Context context) {
        try {
            InputStream inputStream = context.openFileInput(filename);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = reader.readLine()) != null){
                adapter.add(line);
            }

            inputStream.close();
        } catch (IOException e) {
            Toast.makeText(HistoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    private void deleteHistoryDialogue(String Message){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(Message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            OutputStreamWriter writer = new OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE));
                            writer.write("");
                            arrList.clear();
                            adapter.notifyDataSetChanged();
                            writer.close();
                        }catch (Exception e){
                            Toast.makeText(HistoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void deleteEntryDialogue(String Message, int position){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage(Message);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            arrList.remove(position);
                            adapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Toast.makeText(HistoryActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton("No",
                (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}