package com.example.clay;

import io.realm.RealmObject;

public class Note extends RealmObject {
    private String noteTitle;
    private CharSequence noteContent;

    public Note() {

    }

    public Note(String noteTitle, CharSequence noteContent) {
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public CharSequence getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(CharSequence noteContent) {
        this.noteContent = noteContent;
    }
}
