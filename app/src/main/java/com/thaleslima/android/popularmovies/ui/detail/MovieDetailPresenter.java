package com.thaleslima.android.popularmovies.ui.detail;


import com.thaleslima.android.popularmovies.data.repository.MovieRepository;
import com.thaleslima.android.popularmovies.domain.Movie;

/**
 * Created by thales on 20/01/17.
 */

class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private MovieRepository mMovieRepository;
    private MovieDetailContract.View mView;
    private Movie mMovie;
    private boolean mIsFavorite;

    MovieDetailPresenter(MovieDetailContract.View view) {
        this.mView = view;
        mMovieRepository = new MovieRepository(view.getContext());
    }

    @Override
    public void loadMovie() {
        mMovie = mView.getMovieExtra();

        if (mMovie != null) {
            mView.showMovie(mMovie);
            checkFavorite();
        }
    }

    private void checkFavorite() {
        mIsFavorite = mMovieRepository.isFavorite(mMovie.id);

        if (mIsFavorite) {
            mView.setIcoFavoriteYes();
        } else {
            mView.setIcoFavoriteNo();
        }
    }

    @Override
    public void saveOrRemoveFavorite() {
        if (mIsFavorite) {
            removeFavorite();
            mView.setIcoFavoriteNo();
            mView.showSnackbarRemoveFavorite();
        } else {
            saveFavorite();
            mView.setIcoFavoriteYes();
            mView.showSnackbarSaveFavorite();
        }
    }

    @Override
    public void onPause() {
        if(mMovieRepository != null) {
            mMovieRepository.unsubscribe();
        }
    }

    private void saveFavorite() {
        mMovieRepository.insertFavorite(mMovie);
        mIsFavorite = true;
    }

    private void removeFavorite() {
        mMovieRepository.removeFavorite(mMovie.id);
        mIsFavorite = false;
    }
}
