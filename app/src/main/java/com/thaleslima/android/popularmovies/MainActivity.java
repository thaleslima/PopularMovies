package com.thaleslima.android.popularmovies;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.utilities.NetworkUtils;
import com.thaleslima.android.popularmovies.utilities.OpenMovieJsonUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String BUNDLE_MOVIE_DATA = "movie-data";
    private static final int POPULAR = 1;
    private static final int TOP_RATED = 2;

    private GridView mGridView;
    private MoviesAdapter mAdapter;
    private List<Movie> mMovies;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;

    private int mCurrentSearch = POPULAR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGridView = (GridView) findViewById(R.id.grid_movies);
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        mGridView.setOnItemClickListener(this);

        if (savedInstanceState != null) {
            mMovies = savedInstanceState.getParcelableArrayList(BUNDLE_MOVIE_DATA);
        }

        initGridMovies();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(BUNDLE_MOVIE_DATA, (ArrayList<? extends Parcelable>) mMovies);
    }

    private void initGridMovies() {
        if (mMovies == null) {
            mMovies = new ArrayList<>();
        }

        mAdapter = new MoviesAdapter(this, mMovies);
        mGridView.setAdapter(mAdapter);

        if (mMovies.size() == 0) {
            loadMoviesData();
        }
    }

    private void loadMoviesData() {
        URL url;

        if (mCurrentSearch == POPULAR) {
            url = NetworkUtils.buildUrlPopular();
        } else {
            url = NetworkUtils.buildUrlTopRated();
        }

        new FetchMoviesTask().execute(url);
    }

    private void showErrorMessage() {
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
        mGridView.setVisibility(View.GONE);
    }

    private void setDataView(List<Movie> movies) {
        this.mMovies = movies;
        mAdapter.setMoviesData(movies);
    }

    private void showDataView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mGridView.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.GONE);
    }

    private void showLoadingIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        if (mMovies != null && mMovies.size() > i) {
            DetailActivity.navigate(this, mMovies.get(i));
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

        if (id == R.id.action_popular) {
            mCurrentSearch = POPULAR;
            loadMoviesData();
            return true;
        }

        if (id == R.id.action_top_rated) {
            mCurrentSearch = TOP_RATED;
            loadMoviesData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class FetchMoviesTask extends AsyncTask<URL, Void, List<Movie>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showLoadingIndicator();
        }

        @Override
        protected List<Movie> doInBackground(URL... url) {
            if (url.length == 0) {
                return null;
            }

            URL requestUrl = url[0];

            try {
                String response = NetworkUtils.getResponseFromHttpUrl(requestUrl);
                return OpenMovieJsonUtils.getMoviesFromJson(MainActivity.this, response);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Movie> movies) {
            hideLoadingIndicator();

            if (movies != null) {
                setDataView(movies);
                showDataView();
            } else {
                showErrorMessage();
            }
        }
    }
}
