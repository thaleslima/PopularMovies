package com.thaleslima.android.popularmovies.ui.detail.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NavUtils;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.thaleslima.android.popularmovies.R;
import com.thaleslima.android.popularmovies.domain.Movie;
import com.thaleslima.android.popularmovies.domain.Review;
import com.thaleslima.android.popularmovies.domain.Video;
import com.thaleslima.android.popularmovies.ui.detail.adapter.ReviewAdapter;
import com.thaleslima.android.popularmovies.ui.detail.adapter.VideoAdapter;
import com.thaleslima.android.popularmovies.utilities.ResolveUrlUtil;

import java.util.List;

public class MovieDetailFragment extends Fragment implements MovieDetailContract.View, VideoAdapter.VideoAdapterItemClickListener {
    private static final String EXTRA_MOVIE_DATA = "movie-data";

    private TextView mOverview;
    private TextView mVoteAverage;
    private TextView mTitleOriginal;
    private TextView mReleaseDate;
    private TextView mVideosView;
    private TextView mReviewsView;
    private ImageView mPoster;

    private VideoAdapter mVideoAdapter;
    private ReviewAdapter mReviewAdapter;
    private MenuItem mMenuActionShare;

    private MovieDetailContract.Presenter mPresenter;


    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(EXTRA_MOVIE_DATA, movie);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        setFindViewById(view);
        initRecyclerReviews(view);
        initRecyclerVideos(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter = new MovieDetailPresenter(this);
        mPresenter.loadMovie();
    }

    @Override
    public void onPause() {
        super.onPause();
        mPresenter.onPause();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this.getActivity());
                break;
            case R.id.action_share:
                mPresenter.shareVideo();
                break;
        }

        return true;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);

        mMenuActionShare = menu.findItem(R.id.action_share);
        mMenuActionShare.setVisible(false);
    }

    private void setFindViewById(View view) {
        mTitleOriginal = (TextView) view.findViewById(R.id.tv_original_title);
        mVoteAverage = (TextView) view.findViewById(R.id.tv_vote_average_movie);
        mReleaseDate = (TextView) view.findViewById(R.id.tv_release_date_movie);
        mOverview = (TextView) view.findViewById(R.id.tv_overview_movie);
        mVideosView = (TextView) view.findViewById(R.id.tv_videos);
        mReviewsView = (TextView) view.findViewById(R.id.tv_reviews);
        mPoster = (ImageView) view.findViewById(R.id.poster_movie);
    }

    private void initRecyclerReviews(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.review_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(this.getActivity(),
                DividerItemDecoration.VERTICAL));

        mReviewAdapter = new ReviewAdapter();
        recyclerView.setAdapter(mReviewAdapter);
    }

    private void initRecyclerVideos(View view) {
        final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.videos_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        mVideoAdapter = new VideoAdapter(this);
        recyclerView.setAdapter(mVideoAdapter);
    }

    @Override
    public Movie getMovieExtra() {
        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey(EXTRA_MOVIE_DATA)) {
            return bundle.getParcelable(EXTRA_MOVIE_DATA);
        }

        return null;
    }

    @Override
    public void showMenu(Movie movie) {
        mTitleOriginal.setText(movie.originalTitle);
        mVoteAverage.setText(getString(R.string.vote_average, movie.voteAverage));
        mReleaseDate.setText(movie.releaseDate);
        mOverview.setText(movie.overview);

        Picasso.with(this.getContext()).load(ResolveUrlUtil.getCompletePosterUrl(movie.posterPath)).into(mPoster);
    }

    @Override
    public void setDataReviews(List<Review> reviews) {
        mReviewAdapter.replaceData(reviews);
        mReviewsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDataVideos(List<Video> videos) {
        mVideoAdapter.replaceData(videos);
        mVideosView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showMenuShare() {
        mMenuActionShare.setVisible(true);
    }

    @Override
    public void shareText(String textToShare) {
        String mimeType = "text/plain";
        ShareCompat.IntentBuilder
                .from(this.getActivity())
                .setType(mimeType)
                .setChooserTitle(R.string.app_name)
                .setText(textToShare)
                .startChooser();
    }

    @Override
    public void onVideoItemClick(Video video) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(ResolveUrlUtil.getCompleteVideoUrl(video.key))));
    }
}
