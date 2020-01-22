package com.example.clay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private Toolbar mToolbarNote;
    private Button mSaveNote;
    private TextView mDate;
    private EditText mNoteTile;
    private EditText mNoteContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        mToolbarNote = findViewById(R.id.toolbar_note);
        setSupportActionBar(mToolbarNote);

        mSaveNote = findViewById(R.id.button_note_save);

        mDate = findViewById(R.id.text_view_date);
        String currentDate = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(new Date());
        mDate.setText(currentDate);

        mNoteTile = findViewById(R.id.edit_text_note_title);

        mNoteContent = findViewById(R.id.edit_text_note_content);
    }
}
