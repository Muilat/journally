package com.android.muilat.journally.data;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "journals")
public class EntryClass {

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private String description;
    private Date created_at;

    @Ignore
    public EntryClass(String title, String description, Date created_at) {
        this.title = title;
        this.description = description;
        this.created_at = created_at;
    }

    public EntryClass(long id, String title, String description, Date created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_at = created_at;

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
