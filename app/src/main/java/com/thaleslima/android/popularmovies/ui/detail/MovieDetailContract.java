package com.thaleslima.android.popularmovies.ui.detail;

import android.content.Context;

import com.thaleslima.android.popularmovies.domain.Movie;

/**
 * Created by thales on 20/01/17.
 */

interface MovieDetailContract {
    interface View {
        Movie getMovieExtra();

        void showMovie(Movie movie);

        Context getContext();

        void setIcoFavoriteNo();

        void setIcoFavoriteYes();

        void showSnackbarRemoveFavorite();

        void showSnackbarSaveFavorite();
    }

    interface Presenter {
        void loadMovie();

        void saveOrRemoveFavorite();

        void onPause();
    }
}
