package com.example.clay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NoteActivity extends AppCompatActivity {

    private Realm mRealm;

    private Toolbar mToolbarNote;

    private Button mButtonSaveNote;
    private TextView mDate;
    private EditText mNoteTile;
    private EditText mNoteContent;

    private Note mNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("notes.realm")
                .build();
        mRealm = Realm.getInstance(realmConfig);

        mToolbarNote = findViewById(R.id.toolbar_note);
        setSupportActionBar(mToolbarNote);

        mButtonSaveNote = findViewById(R.id.button_note_save);
        mButtonSaveNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRealm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {

                        realm.insertOrUpdate(mNote);
                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                        Log.v("Note", "SAVED");
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                        Log.v("Note", "FAILED");
                    }
                });
            }
        });

        mNote = new Note();

        mDate = findViewById(R.id.text_view_date);
        String currentDate = new SimpleDateFormat("EEE, MMM dd, yyyy", Locale.getDefault()).format(new Date());
        mDate.setText(currentDate);

        mNoteTile = findViewById(R.id.edit_text_note_title);
        mNoteTile.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    String title = mNoteTile.getText().toString();
                    mNote.setTitle(title);
                    Log.v("Note", "Title added");
                    handled = true;
                }
                return handled;
            }
        });

        mNoteContent = findViewById(R.id.edit_text_note_content);
        mNoteContent.setSingleLine(false);
        mNoteContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // TODO: use TextWatcher to listen for deletion
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String s = editable.toString();
                if (s.length() == 0) {
                    return;
                }
                int end = s.length() - 1;  // index of the last character
                if (Character.toString(s.charAt(end)).equals("\n")) { // current line finished
                    int begin = s.substring(0, end).lastIndexOf("\n");
                    String potentialInsertQuery;
                    if (begin == -1) {
                        potentialInsertQuery = s;
                    } else {
                        potentialInsertQuery = s.substring(begin + 1, end);
                    }
                    String[] inputArray = Utils.getInputArray(potentialInsertQuery);
                    if (Utils.isValidInputVerse(inputArray)) {
                        // Insert verses
                        String versesToInsert = searchVerses(Utils.getSearchKeyWords(inputArray));
                        editable.append("\n" + versesToInsert + "\n\n");
                        // Add the range of inserted verses
                        int insertionStart = end + 1;
                        int insertionEnd = editable.toString().trim().length();
                        mNote.updateRangesOfVerses(insertionStart);
                        mNote.updateRangesOfVerses(insertionEnd);
                        Log.v("Note", "Ranges: " + mNote.getRangesOfVerses());
                    }
                }
            }
        });
        mNoteContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (actionId == KeyEvent.KEYCODE_ENTER)) {
                    String content = mNoteContent.getText().toString();
                    mNote.setContent(content.getBytes());
                    Log.v("Note", "Content added: " + content);
                    handled = true;
                }
                if (actionId == EditorInfo.IME_MASK_ACTION) {
                    String content = mNoteContent.getText().toString();
                    mNote.setContent(content.getBytes());
                    Log.v("Note", "Content added: " + content);
                    handled = true;
                }
                if (keyEvent.getAction() == KeyEvent.KEYCODE_DEL || actionId == KeyEvent.KEYCODE_DEL) {
                    String currentContent = textView.getText().toString();
                    String rangeString = mNote.getRangesOfVerses();
                    String[] rangeArrayString = rangeString.trim().split(" ");
                    int[] rangeArray = new int[rangeArrayString.length];
                    for (int i = 0; i < rangeArrayString.length; i++) {
                        rangeArray[i] = Integer.parseInt(rangeArrayString[i]);
                    }

                    int selectionStart = textView.getSelectionStart();
                    int selectionEnd = textView.getSelectionEnd();
                    Log.v("Note", "Cursor position: " + selectionStart + " " + selectionEnd);
                    if (cursorInVerses(selectionStart, rangeArray) || cursorInVerses(selectionEnd, rangeArray)) {
                        // Disable deletion
                        textView.setText(currentContent);
                    }
                }
                return handled;
            }
        });
    }

    private String searchVerses(String[] inputArray) {
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("bible.realm")
                .build();
        mRealm = Realm.getInstance(realmConfig);
        RealmQuery<Verse> query = mRealm.where(Verse.class);
        RealmResults<Verse> results;

        Utils.Book book = Utils.bookMap.get(inputArray[0]);;
        int bookNumber = book.getBookNumber();
        int chapterNumber = Integer.parseInt(inputArray[1]);

        if (inputArray.length == 3) {
            int verseNumber = Integer.parseInt(inputArray[2]);
             results = query.equalTo("book", bookNumber)
                    .equalTo("chapter", chapterNumber)
                    .equalTo("verse", verseNumber)
                    .findAll();
        } else {
            int startingVerse = Integer.parseInt(inputArray[2]);
            int endingVerse = Integer.parseInt(inputArray[3]);
            results = query.equalTo("book", bookNumber)
                    .equalTo("chapter", chapterNumber)
                    .between("verse", startingVerse, endingVerse)
                    .findAll();
        }

        List<Verse> verseList = new ArrayList<>();
        verseList.addAll(results);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < verseList.size(); i++) {
            stringBuilder.append(verseList.get(i).getVerse());
            stringBuilder.append(" ");
            stringBuilder.append(verseList.get(i).getContent());
            stringBuilder.append("\n");
        }

        return new String(stringBuilder);
    }

    private boolean cursorInVerses(int position, int[] rangeArray) {
        int low = 0;
        int high = rangeArray.length - 1;

        while (low < high - 1) {
            int mid = low + (high - low) / 2;
            if (rangeArray[mid] <= position) {
                low = mid + 1;
            } else {
                high = mid;
            }
        }

        int target;
        if (rangeArray[low] < position) {
            target = high;
        } else {
            target = low;
        }
        if (target % 2 == 0) {  // insertionStart
            return false;
        } else {  // insertionEnd
            return true;
        }
    }
}
