package com.carpediemsolution.fitdiary.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Юлия on 07.02.2017.
 */

public class Weight {
    private String weight;
    private Date date;
    private UUID id;
    private String notes;
    private String calories;
    private String photoUri;

    public Weight() {
        this(UUID.randomUUID());
    }

    public Weight(UUID id) {
        this.id = id;
        date = new Date();
    }

    public UUID getId() {
        return id;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    public String getsWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
