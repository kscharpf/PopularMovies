package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Objects;

public class MovieFragment extends Fragment {

    private static final String TAG="MovieFragment";
    private static final String MOVIE_ITEM_ARG = "movie_item";
    @Nullable
    private MovieItem mMovieItem;

    @NonNull
    public static MovieFragment newInstance(MovieItem item) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MOVIE_ITEM_ARG, item);
        MovieFragment fragment = new MovieFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mMovieItem = Objects.requireNonNull(getArguments()).getParcelable(MOVIE_ITEM_ARG);
        Log.i(TAG, "onCreate retrieved: " + mMovieItem);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: " + mMovieItem);
        View v = inflater.inflate(R.layout.movie_activity_fragment, container, false);
        TextView titleTv = v.findViewById(R.id.movie_title_tv);
        titleTv.setText(Objects.requireNonNull(mMovieItem).getTitle());

        ImageView imageView = v.findViewById(R.id.movie_iv);
        Picasso.get()
                .load(mMovieItem.getBigUrl())
                .placeholder(R.drawable.ic_baseline_photo_24px)
                .error(R.drawable.ic_baseline_block_24px)
                .into(imageView);

        TextView moviePlotTv = v.findViewById(R.id.movie_plot_tv);
        moviePlotTv.setText(mMovieItem.getOverview());

        TextView movieRatingTv = v.findViewById(R.id.movie_rating_tv);
        movieRatingTv.setText(getString(R.string.movie_rating_label, mMovieItem.getVoteAverage()));

        TextView releaseDateTv = v.findViewById(R.id.movie_release_date_tv);
        releaseDateTv.setText(getString(R.string.release_date_label , mMovieItem.getReleaseDate()));

        return v;
    }
}
