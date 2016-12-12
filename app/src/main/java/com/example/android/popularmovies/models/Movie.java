package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pedram on 12/11/2016.
 */
public class Movie implements Parcelable {
    long id;
    String title;
    double vote;
    double popularity;
    String genre;
    String date;
    String poster;
    String overview;

    public Movie() {

    }

    public Movie(Parcel p) {
        id = p.readLong();
        title = p.readString();
        vote = p.readDouble();
        popularity = p.readDouble();
        genre = p.readString();
        date = p.readString();
        poster = p.readString();
        overview = p.readString();
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

    public double getVote() {
        return vote;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getOverview() {
        return overview;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(title);
        parcel.writeDouble(vote);
        parcel.writeDouble(popularity);
        parcel.writeString(genre);
        parcel.writeString(date);
        parcel.writeString(poster);
        parcel.writeString(overview);
    }

    public final Parcelable.Creator<Movie> CREATOR = new ClassLoaderCreator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new Movie(parcel);
        }

        @Override
        public Movie createFromParcel(Parcel parcel) {
            return new Movie(parcel);
        }

        @Override
        public Movie[] newArray(int i) {
            return new Movie[0];
        }
    };
}
