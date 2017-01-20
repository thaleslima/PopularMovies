package com.thaleslima.android.popularmovies.ui.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thaleslima.android.popularmovies.R;
import com.thaleslima.android.popularmovies.domain.Review;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private List<Review> mDataSet = new ArrayList<>();

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.populate(mDataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView mAuthorView;
        private final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mAuthorView = (TextView) view.findViewById(R.id.tv_author_review);
            mContentView = (TextView) view.findViewById(R.id.tv_content_review);
        }

        void populate(Review review) {
            mAuthorView.setText(review.author);
            mContentView.setText(review.content);
        }
    }

    public void replaceData(List<Review> dataSet) {
        setList(dataSet);
        notifyDataSetChanged();
    }

    private void setList(List<Review> dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
    }
}
