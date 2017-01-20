package com.thaleslima.android.popularmovies.ui.detail.fragment;

import com.thaleslima.android.popularmovies.data.repository.MovieRepository;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.domain.Review;
import com.thaleslima.android.popularmovies.domain.Video;
import com.thaleslima.android.popularmovies.utilities.DefaultSubscriber;
import com.thaleslima.android.popularmovies.utilities.ResolveUrlUtil;

import java.util.List;

/**
 * Created by thales on 20/01/17.
 */

class MovieDetailPresenter implements MovieDetailContract.Presenter {
    private MovieRepository mMovieRepository;
    private MovieDetailContract.View mView;
    private Video mVideoToShare;

    MovieDetailPresenter(MovieDetailContract.View view) {
        this.mView = view;
        mMovieRepository = new MovieRepository(view.getContext());
    }

    @Override
    public void loadMovie() {
        Movie movie = mView.getMovieExtra();
        if (movie != null) {
            mView.showMenu(movie);
            mMovieRepository.getVideosById(movie.id, new SubscriberVideo());
            mMovieRepository.getReviewsById(movie.id, new SubscriberReview());
        }
    }

    @Override
    public void shareVideo() {
        if (mVideoToShare != null) {
            mView.shareText(ResolveUrlUtil.getCompleteVideoUrl(mVideoToShare.key));
        }
    }

    @Override
    public void onPause() {
        if (mMovieRepository != null) {
            mMovieRepository.unsubscribe();
        }
    }

    private class SubscriberVideo extends DefaultSubscriber<List<Video>> {
        @Override
        public void onNext(List<Video> videos) {
            mView.setDataVideos(videos);

            if (videos != null && videos.size() > 0) {
                mVideoToShare = videos.get(0);
                mView.showMenuShare();
            }
        }
    }

    private class SubscriberReview extends DefaultSubscriber<List<Review>> {
        @Override
        public void onNext(List<Review> reviews) {
            mView.setDataReviews(reviews);
        }
    }
}
