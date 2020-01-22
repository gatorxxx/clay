package com.example.clay;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Verse extends RealmObject {

    @PrimaryKey
    private String id;
    private int book;
    private int chapter;
    private int verse;
    private String content;

    public Verse() {

    }

    public Verse(String id, int book, int chapter, int verse, String content) {
        this.id = id;
        this.book = book;
        this.chapter = chapter;
        this.verse = verse;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public int getBook() {
        return book;
    }

    public int getChapter() {
        return chapter;
    }

    public int getVerse() {
        return verse;
    }

    public String getContent() {
        return content;
    }
}
