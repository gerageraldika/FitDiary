package com.carpediemsolution.fitdiary.model;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Юлия on 07.02.2017.
 */

public class Weight {
    private String sweight;
    private Date mDate;
    private UUID mId;
    private String mNotes;
    private String calories;
    private String photoUri;

    public Weight() {this(UUID.randomUUID());}

    public Weight(UUID id){
        mId = id;
        mDate = new Date();
    }

    public UUID getId() {
    return mId;
    }

    public void setDate(Date date) {
         mDate = date;
    }

    public Date getDate() {
       return mDate;
   }

    public String getsWeight() {return sweight;}

    public void setWeight(String mweight) {sweight = mweight;}

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String mNotes) {
        this.mNotes = mNotes;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }
}
