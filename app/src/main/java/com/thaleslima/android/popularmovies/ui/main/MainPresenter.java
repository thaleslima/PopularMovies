package com.thaleslima.android.popularmovies.ui.main;

import android.os.Bundle;

import com.thaleslima.android.popularmovies.data.repository.MovieRepository;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.utilities.DefaultSubscriber;
import com.thaleslima.android.popularmovies.utilities.NetworkUtil;

import java.util.List;

/**
 * Created by thales on 20/01/17.
 */

class MainPresenter implements MainContract.Presenter {
    private static final String BUNDLE_CURRENT_SEARCH = "movie-search";

    private MovieRepository mMovieRepository;
    private MainContract.View mView;
    private int mCurrentSearch = POPULAR;

    MainPresenter(MainContract.View view) {
        mView = view;
        mMovieRepository = new MovieRepository(view.getContext());
    }

    @Override
    public void loadMoviesData() {
        switch (mCurrentSearch) {
            case POPULAR:
                getPopularMovies();
                break;
            case TOP_RATED:
                getTopRatedMovies();
                break;
            default:
                getFavoritesMovies();
        }
    }

    @Override
    public void setTypeSearch(int typeSearch) {
        mCurrentSearch = typeSearch;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(BUNDLE_CURRENT_SEARCH, mCurrentSearch);
    }

    @Override
    public void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(BUNDLE_CURRENT_SEARCH)) {
            mCurrentSearch = savedInstanceState.getInt(BUNDLE_CURRENT_SEARCH);
        }
    }

    @Override
    public void onPause() {
        if (mMovieRepository != null) {
            mMovieRepository.unsubscribe();
        }
    }

    private void getPopularMovies() {
        if (NetworkUtil.isNetworkAvailable(mView.getContext())) {
            mView.showLoadingIndicator();
            mMovieRepository.getPopularMovies(new Subscriber());
        } else {
            mView.showSnackbarNoConnection();
        }
    }

    private void getTopRatedMovies() {
        if (NetworkUtil.isNetworkAvailable(mView.getContext())) {
            mView.showLoadingIndicator();
            mMovieRepository.getTopRatedMovies(new Subscriber());
        } else {
            mView.showSnackbarNoConnection();
        }
    }

    private void getFavoritesMovies() {
        mView.showLoadingIndicator();
        mMovieRepository.getFavoritesMovies(new Subscriber());
    }

    private class Subscriber extends DefaultSubscriber<List<Movie>> {
        @Override
        public void onError(Throwable e) {
            mView.showErrorMessage();
            mView.hideLoadingIndicator();
        }

        @Override
        public void onNext(List<Movie> movies) {
            mView.hideLoadingIndicator();

            if (movies.size() > 0) {
                mView.setDataView(movies);
                mView.showDataView();
            } else {
                mView.showNoMoviesMessage();
            }
        }
    }
}
