package com.thaleslima.android.popularmovies;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.utilities.NetworkUtils;

public class DetailActivity extends AppCompatActivity {
    private static final String EXTRA_MOVIE_DATA = "movie-data";

    private TextView mOriginalTitle;
    private TextView mOverview;
    private TextView mVoteAverage;
    private TextView mReleaseDate;
    private ImageView mPoster;

    public static void navigate(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_DATA, movie);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mOriginalTitle = (TextView) findViewById(R.id.tv_title_movie);
        mOverview = (TextView) findViewById(R.id.tv_overview_movie);
        mVoteAverage = (TextView) findViewById(R.id.tv_vote_average_movie);
        mReleaseDate = (TextView) findViewById(R.id.tv_release_date_movie);
        mPoster = (ImageView) findViewById(R.id.poster_movie);

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MOVIE_DATA)) {
            Movie movie = getIntent().getParcelableExtra(EXTRA_MOVIE_DATA);
            showMovie(movie);
        }
    }

    private void showMovie(Movie movie) {
        if (movie != null) {
            mOriginalTitle.setText(movie.originalTitle);
            mOverview.setText(movie.overview);
            mVoteAverage.setText(getString(R.string.vote_average, movie.voteAverage));
            mReleaseDate.setText(movie.releaseDate);

            Picasso.with(this).load(NetworkUtils.POSTER_BASE_URL + movie.posterPath).into(mPoster);
        }
    }
}
