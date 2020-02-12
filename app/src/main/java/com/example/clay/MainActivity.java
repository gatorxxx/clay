package com.example.clay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbarMain;

    private Realm mRealmBible;
    private Realm mRealmNotes;

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
        RealmConfiguration realmBibleConfig = new RealmConfiguration.Builder()
                .name("bible.realm")
                .build();
        mRealmBible = Realm.getInstance(realmBibleConfig);
        RealmConfiguration realmNotesConfig = new RealmConfiguration.Builder()
                .name("notes.realm")
                .build();
        mRealmNotes = Realm.getInstance(realmNotesConfig);

        if (mRealmBible.isEmpty()) {
            // Create a table for Bible for the first time
            // TODO: progress bar while creating
            Log.v("Realm Check", "Creating Bible Database");
            mRealmBible.executeTransactionAsync(new Realm.Transaction() {
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
                    Log.v("Realm Check", "Bible Database Created");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.v("Realm Check", "Failed to Create Bible Database");
                }
            });
        } else {
            Log.v("Realm Check", "Bible Database Existed");
        }

        Utils.createSearchList();

        if (mRealmNotes.isEmpty()) {
            Log.v("Realm Check", "Creating Note Database");
            mRealmNotes.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    String noteTitle = "Welcome to Clay!";
                    String noteContent = "May this app be a blessing to you!\n" +
                            "You'll find some welcome notes with some helpful tips included in the app. Feel free to take a look now (tap \"Done\" in the top left and open the next note), or jump right in with your first note!";
                    byte[] contentBytes = noteContent.getBytes();
                    Note note = new Note(noteTitle, contentBytes);
                    realm.insert(note);
                }
            }, new Realm.Transaction.OnSuccess() {
                @Override
                public void onSuccess() {
                    Log.v("Realm Check", "Note Database Created");
                }
            }, new Realm.Transaction.OnError() {
                @Override
                public void onError(Throwable error) {
                    Log.v("Realm Check", "Failed to Create Note Database");
                }
            });
        } else {
            // TODO: display latest 20 notes
            Log.v("Realm Check", "Note Database Existed");
            RealmQuery<Note> query = mRealmNotes.where(Note.class);
        }
    }
}
