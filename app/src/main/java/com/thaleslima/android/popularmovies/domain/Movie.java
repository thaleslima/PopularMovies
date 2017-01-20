package com.thaleslima.android.popularmovies.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by thales on 08/01/17.
 */

public class Movie implements Parcelable {
    public String posterPath;
    public String originalTitle;
    public String overview;
    public String voteAverage;
    public String releaseDate;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.posterPath);
        dest.writeString(this.originalTitle);
        dest.writeString(this.overview);
        dest.writeString(this.voteAverage);
        dest.writeString(this.releaseDate);
    }

    public Movie() {
    }

    protected Movie(Parcel in) {
        this.posterPath = in.readString();
        this.originalTitle = in.readString();
        this.overview = in.readString();
        this.voteAverage = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
