package com.thaleslima.android.popularmovies.ui.detail.fragment;

import android.content.Context;

import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.domain.Review;
import com.thaleslima.android.popularmovies.domain.Video;

import java.util.List;

/**
 * Created by thales on 20/01/17.
 */

interface MovieDetailContract {
    interface View {
        Movie getMovieExtra();

        void showMenu(Movie movie);

        void setDataReviews(List<Review> reviews);

        void setDataVideos(List<Video> videos);

        void showMenuShare();

        Context getContext();

        void shareText(String completeVideoUrl);
    }

    interface Presenter {
        void loadMovie();

        void shareVideo();

        void onPause();
    }
}
