package com.thaleslima.android.popularmovies.ui.main.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thaleslima.android.popularmovies.R;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.utilities.ResolveUrlUtil;

import java.util.List;

/**
 * Created by thales on 08/01/17.
 */

public class MoviesAdapter extends ArrayAdapter<Movie> {
    public MoviesAdapter(Context context, List<Movie> movies) {
        super(context, 0, movies);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.movie_list_item, parent, false);
        }

        ImageView movieImage = (ImageView) convertView.findViewById(R.id.movie_image);
        if (movie != null) {
            Picasso.with(getContext())
                    .load(ResolveUrlUtil.getCompletePosterUrl(movie.posterPath))
                    .into(movieImage);
        }

        return convertView;
    }

    public void setMoviesData(List<Movie> movies) {
        clear();
        addAll(movies);
        notifyDataSetChanged();
    }
}
