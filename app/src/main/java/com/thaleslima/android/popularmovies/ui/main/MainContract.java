package com.thaleslima.android.popularmovies.ui.main;

import android.content.Context;
import android.os.Bundle;

import com.thaleslima.android.popularmovies.domain.Movie;

import java.util.List;

/**
 * Created by thales on 20/01/17.
 */

interface MainContract {
    interface View {
        void showSnackbarNoConnection();

        void showErrorMessage();

        void showNoMoviesMessage();

        void setDataView(List<Movie> movies);

        void showDataView();

        void showLoadingIndicator();

        void hideLoadingIndicator();

        Context getContext();
    }

    interface Presenter {
        int POPULAR = 1;
        int TOP_RATED = 2;
        int FAVORITES = 3;

        void loadMoviesData();

        void setTypeSearch(int typeSearch);

        void onSaveInstanceState(Bundle outState);

        void restoreInstanceState(Bundle savedInstanceState);

        void onPause();
    }
}
