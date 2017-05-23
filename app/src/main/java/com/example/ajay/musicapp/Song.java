package com.example.ajay.musicapp;


import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Song implements Serializable {


    String title;
    String datas;
    String artistName;
    int duration;
    boolean isSelectedCB;

    public boolean getSelectedCB() {
        return isSelectedCB;
    }

    public void setVisibleCB(boolean SelectedCB) {
        isSelectedCB = SelectedCB;
    }



    public boolean isAdded() {
        return isAdded;
    }

    public void setAdded(boolean added) {
        isAdded = added;
    }

    int id;
    boolean isAdded;
    //Bitmap image;

    public long getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(long thumbnail) {
        this.thumbnail = thumbnail;
    }

    long thumbnail;



//  //  public void setImage(Bitmap image) {
//        this.image = image;
//    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getTitle() {
        return title;
    }

//    public Bitmap getImage() {
//        return image;
//    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDatas() {
        return datas;
    }

    public void setDatas(String data) {
        this.datas = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


 /*   @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }*/
}
