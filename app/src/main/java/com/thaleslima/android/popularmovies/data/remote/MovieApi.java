package com.thaleslima.android.popularmovies.data.remote;

import com.thaleslima.android.popularmovies.domain.response.MovieResponse;
import com.thaleslima.android.popularmovies.domain.response.ReviewResponse;
import com.thaleslima.android.popularmovies.domain.response.VideoResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by thales on 18/01/17.
 */

public interface MovieApi {
    @GET("popular")
    Observable<MovieResponse> getPopularMovies(@Query("api_key") String key);

    @GET("top_rated")
    Observable<MovieResponse> getTopRatedMovies(@Query("api_key") String key);

    @GET("{id}/videos")
    Observable<VideoResponse> getVideosById(@Path("id") long id, @Query("api_key") String key);

    @GET("{id}/reviews")
    Observable<ReviewResponse> getReviewsById( @Path("id") long id, @Query("api_key") String key);
}
