package com.thaleslima.android.popularmovies.data.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.thaleslima.android.popularmovies.data.local.MovieContract.MovieEntry;

/**
 * Created by thales on 18/01/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_TABLE =
                "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                        MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                        MovieEntry.COLUMN_POSTER_PATH + " TEXT, " +
                        MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, " +
                        MovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                        MovieEntry.COLUMN_AVERAGE + " TEXT, " +
                        MovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                        MovieEntry.COLUMN_BACKDROP_PATH + " TEXT" +
                        " )";

        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }
}
