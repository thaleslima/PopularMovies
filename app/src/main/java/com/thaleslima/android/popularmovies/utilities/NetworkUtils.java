package com.thaleslima.android.popularmovies.utilities;

import android.net.Uri;
import android.util.Log;

import com.thaleslima.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by thales on 08/01/17.
 */

public class NetworkUtils {
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w185/";

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String STATIC_MOVIE_URL = "http://api.themoviedb.org/3/movie/";

    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String API_KEY = "api_key";

    private static final String THE_MOVIE_DB_API_KEY = BuildConfig.MOVIE_API_KEY;

    private static final String MOVIE_POPULAR_URL = STATIC_MOVIE_URL + POPULAR;
    private static final String MOVIE_TOP_RATED_URL = STATIC_MOVIE_URL + TOP_RATED;

    public static URL buildUrlPopular() {
        Uri builtUri = Uri.parse(MOVIE_POPULAR_URL).buildUpon()
                .appendQueryParameter(API_KEY, THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static URL buildUrlTopRated() {
        Uri builtUri = Uri.parse(MOVIE_TOP_RATED_URL).buildUpon()
                .appendQueryParameter(API_KEY, THE_MOVIE_DB_API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
