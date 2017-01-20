package com.thaleslima.android.popularmovies.utilities;

import java.util.Locale;

/**
 * Created by thales on 08/01/17.
 */

public final class ResolveUrlUtil {
    private ResolveUrlUtil() {

    }

    public static String getCompletePosterUrl(String posterPath) {
        return Constants.POSTER_BASE_URL + posterPath;
    }

    public static String getCompleteVideoPosterUrl(String videoPath) {
        return String.format(Locale.getDefault(), Constants.POSTER_VIDEO_BASE_URL, videoPath);
    }

    public static String getCompleteVideoUrl(String key) {
        return Constants.YOUTUBE_BASE_URL + key;
    }
}
