package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pedram on 1/7/2017.
 */

public class Review implements Parcelable {

    String id;
    String author;
    String content;

    public Review(String id, String author, String content) {
        this.id = id;
        this.author = author;
        this.content = content;
    }

    public Review() {
    }
    public Review(Parcel p){
        id = p.readString();
        author = p.readString();
        content = p.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(author);
        parcel.writeString(content);
    }

    public final Parcelable.Creator<Review> CREATOR = new ClassLoaderCreator<Review>() {
        @Override
        public Review createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new Review(parcel);
        }

        @Override
        public Review createFromParcel(Parcel parcel) {
            return new Review(parcel);
        }

        @Override
        public Review[] newArray(int i) {
            return new Review[0];
        }
    };
}
