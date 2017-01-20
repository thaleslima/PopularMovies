package com.thaleslima.android.popularmovies.data.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.thaleslima.android.popularmovies.BuildConfig;
import com.thaleslima.android.popularmovies.data.local.MovieContract;
import com.thaleslima.android.popularmovies.data.remote.MovieApi;
import com.thaleslima.android.popularmovies.data.remote.MovieClient;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.domain.Review;
import com.thaleslima.android.popularmovies.domain.Video;
import com.thaleslima.android.popularmovies.domain.response.MovieResponse;
import com.thaleslima.android.popularmovies.domain.response.ReviewResponse;
import com.thaleslima.android.popularmovies.domain.response.VideoResponse;
import com.thaleslima.android.popularmovies.utilities.DefaultSubscriber;
import com.thaleslima.android.popularmovies.utilities.ObservableUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.functions.Func1;

/**
 * Created by thales on 18/01/17.
 */

public class MovieRepository {
    private static final String THE_MOVIE_DB_API_KEY = BuildConfig.MOVIE_API_KEY;
    private MovieApi mMovieApi;
    private Context mContext;

    @NonNull
    private List<Subscription> mSubscriptions;

    public MovieRepository(Context context) {
        this.mMovieApi = MovieClient.getClient();
        this.mContext = context;

        mSubscriptions = new ArrayList<>();
    }

    public void getPopularMovies(DefaultSubscriber<List<Movie>> subscriber) {
        Func1<MovieResponse, List<Movie>> movieResponseToMovies = new Func1<MovieResponse, List<Movie>>() {
            @Override
            public List<Movie> call(MovieResponse movieResponse) {
                return movieResponse.results;
            }
        };

        Observable<List<Movie>> observable = mMovieApi.getPopularMovies(THE_MOVIE_DB_API_KEY).map(movieResponseToMovies);
        Subscription subscription = ObservableUtil.buildObservable(observable).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }

    public void getTopRatedMovies(DefaultSubscriber<List<Movie>> subscriber) {
        Func1<MovieResponse, List<Movie>> movieResponseToMovies = new Func1<MovieResponse, List<Movie>>() {
            @Override
            public List<Movie> call(MovieResponse movieResponse) {
                return movieResponse.results;
            }
        };

        Observable<List<Movie>> observable = mMovieApi.getTopRatedMovies(THE_MOVIE_DB_API_KEY).map(movieResponseToMovies);
        Subscription subscription = ObservableUtil.buildObservable(observable).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }

    public void getVideosById(long id, DefaultSubscriber<List<Video>> subscriber) {
        Func1<VideoResponse, List<Video>> videoResponseToVideo = new Func1<VideoResponse, List<Video>>() {
            @Override
            public List<Video> call(VideoResponse videoResponse) {
                return videoResponse.results;
            }
        };

        Observable<List<Video>> observable = mMovieApi.getVideosById(id, THE_MOVIE_DB_API_KEY).map(videoResponseToVideo);
        Subscription subscription = ObservableUtil.buildObservable(observable).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }

    public void getReviewsById(long id, DefaultSubscriber<List<Review>> subscriber) {
        Func1<ReviewResponse, List<Review>> reviewResponseToVideo = new Func1<ReviewResponse, List<Review>>() {
            @Override
            public List<Review> call(ReviewResponse reviewResponse) {
                return reviewResponse.results;
            }
        };

        Observable<List<Review>> observable = mMovieApi.getReviewsById(id, THE_MOVIE_DB_API_KEY).map(reviewResponseToVideo);
        Subscription subscription = ObservableUtil.buildObservable(observable).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }

    public void getFavoritesMovies(DefaultSubscriber<List<Movie>> subscriber) {
        Func1<Cursor, List<Movie>> cursorToMovies = new Func1<Cursor, List<Movie>>() {
            @Override
            public List<Movie> call(Cursor cursor) {
                List<Movie> movies = new ArrayList<>();

                if (cursor != null && cursor.moveToFirst()) {
                    int idIndex = cursor.getColumnIndex(MovieContract.MovieEntry._ID);
                    int posterPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_POSTER_PATH);
                    int originalTitleIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE);
                    int overviewIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_OVERVIEW);
                    int voteAverageIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_AVERAGE);
                    int releaseDateIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_RELEASE_DATE);
                    int backdropPathIndex = cursor.getColumnIndex(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH);
                    do {
                        Movie movie = new Movie();
                        movie.id = cursor.getInt(idIndex);
                        movie.posterPath = cursor.getString(posterPathIndex);
                        movie.originalTitle = cursor.getString(originalTitleIndex);
                        movie.overview = cursor.getString(overviewIndex);
                        movie.voteAverage = cursor.getString(voteAverageIndex);
                        movie.releaseDate = cursor.getString(releaseDateIndex);
                        movie.backdropPath = cursor.getString(backdropPathIndex);

                        movies.add(movie);
                    } while (cursor.moveToNext());
                }

                if (cursor != null) {
                    cursor.close();
                }

                return movies;
            }
        };

        ObservableUtil<Cursor> observableUtil = new ObservableUtil<Cursor>() {
            @Override
            protected Cursor buildObject() throws Exception {
                Uri uri = MovieContract.MovieEntry.CONTENT_URI;
                return mContext.getContentResolver().query(
                        uri, null, null, null, null);
            }
        };

        Observable<List<Movie>> observable = observableUtil.buildObservable().map(cursorToMovies);
        Subscription subscription = ObservableUtil.buildObservable(observable).subscribe(subscriber);
        mSubscriptions.add(subscription);
    }

    public void insertFavorite(Movie movie) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.MovieEntry._ID, movie.id);
        contentValues.put(MovieContract.MovieEntry.COLUMN_AVERAGE, movie.voteAverage);
        contentValues.put(MovieContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.originalTitle);
        contentValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movie.overview);
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_PATH, movie.posterPath);
        contentValues.put(MovieContract.MovieEntry.COLUMN_RELEASE_DATE, movie.releaseDate);
        contentValues.put(MovieContract.MovieEntry.COLUMN_BACKDROP_PATH, movie.backdropPath);

        Uri uri = MovieContract.MovieEntry.CONTENT_URI;

        mContext.getContentResolver().insert(uri, contentValues);
    }

    public void removeFavorite(long id) {
        String stringId = Long.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();
        mContext.getContentResolver().delete(uri, null, null);
    }

    public boolean isFavorite(long id) {
        String stringId = Long.toString(id);
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(stringId).build();

        Cursor cursor = mContext.getContentResolver().query(
                uri, null, null, null, null);

        boolean isFavorite = cursor != null && cursor.getCount() > 0;

        if (cursor != null) {
            cursor.close();
        }

        return isFavorite;
    }

    public void unsubscribe() {
        for (Subscription mSubscription : mSubscriptions) {
            mSubscription.unsubscribe();
        }
    }
}
