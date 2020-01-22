package com.example.clay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BibleActivity extends AppCompatActivity {

    private Toolbar mToolbarBible;

    private Realm mRealm;

    private RecyclerView mRecyclerView;
    private EditText mEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bible);

        mToolbarBible = findViewById(R.id.toolbar_bible);
        setSupportActionBar(mToolbarBible);

        mRecyclerView = findViewById(R.id.recycler_view_verse);
        mEditText = findViewById(R.id.edit_text_input_chapter);
        mEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String[] inputArray = Utils.getInputArray(mEditText.getText().toString());
                    if (!Utils.isValidInputBible(inputArray)) {
                        Toast.makeText(getApplicationContext(),
                                "Please enter a valid book + chapter", Toast.LENGTH_SHORT )
                                .show();
                    } else {
                        try {
                            Utils.Book book = null;
                            int bookNumber = 0;
                            int chapterNumber = 0;

                            if (inputArray.length == 2) {
                                book = Utils.bookMap.get(inputArray[0]);
                                bookNumber = book.getBookNumber();
                                chapterNumber = Integer.parseInt(inputArray[1]);
                            } else if (inputArray.length == 3) {
                                int leadingNumber = Integer.parseInt(inputArray[0]);
                                String name = inputArray[1];
                                String fullName = leadingNumber + " " + name;
                                book = Utils.bookMap.get(fullName);
                                bookNumber = book.getBookNumber();
                                chapterNumber = Integer.parseInt(inputArray[2]);
                            }

                            mRealm = Realm.getDefaultInstance();
                            RealmQuery<Verse> query = mRealm.where(Verse.class);
                            RealmResults<Verse> results = query.equalTo("book", bookNumber)
                                    .equalTo("chapter", chapterNumber).findAll();
                            List<Verse> verseList = new ArrayList<>();
                            for (int i = 0; i < results.size(); i++) {
                                verseList.add(results.get(i));
                            }
                            RecyclerViewConfig config = new RecyclerViewConfig();
                            config.setConfig(mRecyclerView, BibleActivity.this, verseList);
                            String displayedTitle = book.getBookName() + " " + chapterNumber;
                            mEditText.setText(displayedTitle);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    handled = true;
                }
                return handled;
            }
        });
    }
}
