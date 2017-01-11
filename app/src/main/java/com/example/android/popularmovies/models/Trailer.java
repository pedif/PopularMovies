package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Pedram on 1/7/2017.
 */

public class Trailer implements Parcelable {

    String id;
    String key;
    String name;

    public Trailer(String id, String key, String name) {
        this.id = id;
        this.key = key;
        this.name = name;
    }

    public Trailer() {
    }

    public Trailer(Parcel p) {
        id = p.readString();
        key = p.readString();
        name = p.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(key);
        parcel.writeString(name);
    }

    public final Parcelable.Creator<Trailer> CREATOR = new ClassLoaderCreator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer createFromParcel(Parcel parcel) {
            return new Trailer(parcel);
        }

        @Override
        public Trailer[] newArray(int i) {
            return new Trailer[0];
        }
    };
}
