package com.example.clay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;
import io.realm.RealmConfiguration;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbarMain;

    private Realm mRealm;

    private Button mBibleButton;
    private Button mNewNoteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbarMain = findViewById(R.id.toolbar_main);
        setSupportActionBar(mToolbarMain);

        mBibleButton = findViewById(R.id.button_bible);
        mBibleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BibleActivity.class);
                startActivity(intent);
            }
        });

        mNewNoteButton = findViewById(R.id.button_new_note);
        mNewNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NoteActivity.class);
                startActivity(intent);
            }
        });

        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("bible.realm")
                .build();
        mRealm = Realm.getInstance(realmConfig);

        if (mRealm.isEmpty()) {
            // Create a table for Bible for the first time
            // TODO: progress bar while creating
            Log.v("Realm Check", "Creating");
            mRealm.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    InputStream inputStream = getResources().openRawResource(R.raw.t_kjv);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    try {
                        String csvLine;
                        while ((csvLine = reader.readLine()) != null) {

                            // Split a comma-separated string but ignoring commas in double quotes
                            String[] row = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

                            String id = row[0].replaceAll("^\"|\"$", "");
                            int book = Integer.parseInt(row[1].replaceAll("^\"|\"$", ""));
                            int chapter = Integer.parseInt(row[2].replaceAll("^\"|\"$", ""));
                            int verse = Integer.parseInt(row[3].replaceAll("^\"|\"$", ""));
                            String content = row[4].replaceAll("^\"|\"$", "");
                            Verse currentVerse = new Verse(id, book, chapter, verse, content);
                            realm.insert(currentVerse);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException("Error in reading CSV file: " + e);
                    }
                    finally {
                        try {
                            inputStream.close();
                        }
                        catch (IOException e) {
                            throw new RuntimeException("Error while closing input stream: " + e);
                        }
                    }
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.v("TAGGED", "SAVED");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.v("TAGGED", "FAILED");
                }
            });
        } else {
            Log.v("Realm Check", "Existed");
        }

        Utils.createSearchList();
    }
}
