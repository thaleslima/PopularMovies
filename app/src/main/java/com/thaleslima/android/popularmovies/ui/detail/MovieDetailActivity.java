package com.thaleslima.android.popularmovies.ui.detail;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thaleslima.android.popularmovies.R;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.ui.detail.fragment.MovieDetailFragment;
import com.thaleslima.android.popularmovies.utilities.ResolveUrlUtil;

public class MovieDetailActivity extends AppCompatActivity implements MovieDetailContract.View {
    private static final String EXTRA_MOVIE_DATA = "movie-data";

    private FloatingActionButton mFloatingActionButton;
    private ImageView mBackdropView;
    private FrameLayout mContainer;

    private MovieDetailContract.Presenter mPresenter;

    public static void navigate(Activity activity, Movie movie) {
        Intent intent = new Intent(activity, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE_DATA, movie);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initToolbar();
        setFindViewById();
        initFabFavorite();
        initialize();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setFindViewById() {
        mBackdropView = (ImageView) findViewById(R.id.iv_backdrop_movie);
        mContainer = (FrameLayout) findViewById(R.id.container);
    }

    private void initialize() {
        mPresenter = new MovieDetailPresenter(this);
        mPresenter.loadMovie();
    }

    @Override
    public Movie getMovieExtra() {
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_MOVIE_DATA)) {
            return getIntent().getParcelableExtra(EXTRA_MOVIE_DATA);
        }

        return null;
    }

    @Override
    public void showMovie(Movie movie) {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            setTitle(movie.originalTitle);
        }

        Picasso.with(this)
                .load(ResolveUrlUtil.getCompletePosterUrl(movie.backdropPath))
                .into(mBackdropView);

        initFragment(MovieDetailFragment.newInstance(movie));
    }

    @Override
    public Context getContext() {
        return this;
    }

    private void initFabFavorite() {
        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.saveOrRemoveFavorite();
            }
        });
    }

    @Override
    public void setIcoFavoriteYes() {
        mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_white_24dp));
    }

    @Override
    public void showSnackbarRemoveFavorite() {
        Snackbar.make(mContainer, R.string.title_remove_favorite, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void showSnackbarSaveFavorite() {
        Snackbar.make(mContainer, R.string.title_save_favorite, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void setIcoFavoriteNo() {
        mFloatingActionButton.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_favorite_border_white_24dp));
    }

    private void initFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.commit();
    }
}
