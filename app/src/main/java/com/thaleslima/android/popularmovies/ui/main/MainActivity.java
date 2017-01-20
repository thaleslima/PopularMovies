package com.thaleslima.android.popularmovies.ui.main;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thaleslima.android.popularmovies.R;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.ui.detail.MovieDetailActivity;
import com.thaleslima.android.popularmovies.ui.main.adapter.MoviesAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements MainContract.View, AdapterView.OnItemClickListener {

    private GridView mGridView;
    private MoviesAdapter mAdapter;
    private TextView mMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RelativeLayout mContainer;
    private MainContract.Presenter mPresenter;
    private List<Movie> mMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new MainPresenter(this);

        initToolbar();
        setFindViewById();
        initGridMovies();

        mPresenter.restoreInstanceState(savedInstanceState);
    }

    private void setFindViewById() {
        mMessageDisplay = (TextView) findViewById(R.id.tv_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
    }

    private void initToolbar() {
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mPresenter.onSaveInstanceState(outState);
    }

    private void initGridMovies() {
        mGridView = (GridView) findViewById(R.id.grid_movies);
        mContainer = (RelativeLayout) findViewById(R.id.activity_main);
        mGridView.setOnItemClickListener(this);
        mMovies = new ArrayList<>();
        mAdapter = new MoviesAdapter(this, mMovies);
        mGridView.setAdapter(mAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadMoviesData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    public void showSnackbarNoConnection() {
        Snackbar.make(mContainer, R.string.title_no_connection, Snackbar.LENGTH_LONG)
                .setAction(R.string.title_retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPresenter.loadMoviesData();
                    }
                }).show();
    }

    @Override
    public void showErrorMessage() {
        mMessageDisplay.setText(R.string.error_message);
        mMessageDisplay.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.GONE);
    }

    @Override
    public void showNoMoviesMessage() {
        mMessageDisplay.setText(R.string.no_movies_message);
        mMessageDisplay.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.GONE);
    }

    @Override
    public void setDataView(List<Movie> movies) {
        this.mMovies = movies;
        mAdapter.setMoviesData(movies);
    }

    @Override
    public void showDataView() {
        mMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showLoadingIndicator() {
        if (mMovies == null || mMovies.size() == 0) {
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mMovies != null && mMovies.size() > i) {
            MovieDetailActivity.navigate(this, mMovies.get(i));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        int typeSearch;

        if (id == R.id.action_favorites) {
            typeSearch = MainContract.Presenter.FAVORITES;
        } else if (id == R.id.action_top_rated) {
            typeSearch = MainContract.Presenter.TOP_RATED;
        } else {
            typeSearch = MainContract.Presenter.POPULAR;
        }

        mPresenter.setTypeSearch(typeSearch);
        mPresenter.loadMoviesData();

        return true;
    }
}
