package com.thaleslima.android.popularmovies.utilities;

import android.content.Context;

import com.thaleslima.android.popularmovies.domain.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by thales on 08/01/17.
 */

public class OpenMovieJsonUtils {

    public static List<Movie> getMoviesFromJson(Context context, String moviesJson)
            throws JSONException {

        final String OWM_LIST_MOVIES = "results";
        final String OWM_TITLE = "original_title";
        final String OWM_POSTER_PATH = "poster_path";
        final String OWM_OVERVIEW = "overview";
        final String OWM_VOTE_AVERAGE = "vote_average";
        final String OWM_RELEASE_DATE = "release_date";
        final String OWM_MESSAGE = "status_message";

        JSONObject movieJson = new JSONObject(moviesJson);

        if (movieJson.has(OWM_MESSAGE)) {
            return null;
        }

        JSONArray moviesArray = movieJson.getJSONArray(OWM_LIST_MOVIES);
        List<Movie> movies = new ArrayList<>();

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movieObj = moviesArray.getJSONObject(i);

            Movie movie = new Movie();
            movie.originalTitle = movieObj.getString(OWM_TITLE);
            movie.posterPath = movieObj.getString(OWM_POSTER_PATH);
            movie.overview = movieObj.getString(OWM_OVERVIEW);
            movie.voteAverage = movieObj.getString(OWM_VOTE_AVERAGE);
            movie.releaseDate = movieObj.getString(OWM_RELEASE_DATE);

            movies.add(movie);
        }
        return movies;
    }
}
