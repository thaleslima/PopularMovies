package com.thaleslima.android.popularmovies.ui.detail.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.thaleslima.android.popularmovies.R;
import com.thaleslima.android.popularmovies.domain.Video;
import com.thaleslima.android.popularmovies.utilities.ResolveUrlUtil;

import java.util.ArrayList;
import java.util.List;

public  class VideoAdapter extends RecyclerView.Adapter<VideoAdapter.ViewHolder> {
    private List<Video> mDataSet = new ArrayList<>();
    private VideoAdapterItemClickListener listener;

    public interface VideoAdapterItemClickListener {
        void onVideoItemClick(Video video);
    }

    public VideoAdapter(VideoAdapterItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.populate(mDataSet.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mDataSet.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView mPosterMovie;
        private final View mView;
        private Video mVideo;

        ViewHolder(View view) {
            super(view);
            mView = view;
            mPosterMovie = (ImageView) view.findViewById(R.id.poster_video);

            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null && mVideo != null) {
                        listener.onVideoItemClick(mVideo);
                    }
                }
            });
        }

        void populate(Video data, int position) {
            mVideo = data;
            Picasso.with(mView.getContext()).load(ResolveUrlUtil.getCompleteVideoPosterUrl(data.key)).into(mPosterMovie);
            setPadding(position);
        }

        void setPadding(int position) {
            float paddingLeft = 0;
            if (position == 0) {
                paddingLeft = mView.getResources().getDimension(R.dimen.horizontal_padding);
            }

            float paddingRight = 0;
            if (position + 1 != getItemCount()) {
                paddingRight = mView.getResources().getDimension(R.dimen.horizontal_padding) / 2;
            }

            if (position == getItemCount() - 1) {
                paddingRight = mView.getResources().getDimension(R.dimen.horizontal_padding);
            }

            mView.setPadding((int) paddingLeft, 0, (int) paddingRight, 0);
        }
    }

    public void replaceData(List<Video> dataSet) {
        setList(dataSet);
        notifyDataSetChanged();
    }

    private void setList(List<Video> dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
    }
}
