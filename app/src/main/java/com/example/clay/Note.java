package com.example.clay;

import java.util.Date;

import io.realm.RealmObject;

public class Note extends RealmObject {
    private String title;
    private byte[] content;
    private Date dateCreated;
    private Date dateUpdated;
    private String location;
    private String speaker;
    private String sermonImpact;
    private String notebook;
    private String rangesOfVerses;
    private String scripture;
    private String noteId;

    public Note() {
        this.dateCreated = new Date();
        this.rangesOfVerses = "";
        this.scripture = "";
    }

    public Note(String title, byte[] content) {
        this.title = title;
        this.content = content;
        this.dateCreated = new Date();
    }

    public String getTitle() {
        return title;
    }

    public byte[] getContent() {
        return content;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public Date getDateUpdated() {
        return dateUpdated;
    }

    public String getLocation() {
        return location;
    }

    public String getSpeaker() {
        return speaker;
    }

    public String getSermonImpact() {
        return sermonImpact;
    }

    public String getNotebook() {
        return notebook;
    }

    public String getRangesOfVerses() {
        return rangesOfVerses;
    }

    public String getScripture() {
        return scripture;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public void setSermonImpact(String sermonImpact) {
        this.sermonImpact = sermonImpact;
    }

    public void setNotebook(String notebook) {
        this.notebook = notebook;
    }

    public void updateRangesOfVerses(int position) {
        this.rangesOfVerses = rangesOfVerses + position + " ";
    }

    public void setScripture(String scripture) {
        this.scripture = scripture;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }
}
