package com.android.muilat.journally.data;


import java.util.Date;

public class EntryClass {

    private String id;
    private String user_id;
    private String title;
    private String description;
    private Date created_at;

    public EntryClass(){}

    public EntryClass(String title, String description, Date created_at) {
        this.title = title;
        this.description = description;
        this.created_at = created_at;
    }

    public EntryClass(String id, String title, String description, Date created_at) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.created_at = created_at;


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return user_id;
    }

    public void setUserId(String id) {
        this.id = user_id;
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
