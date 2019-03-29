package com.example.android.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class PopularMoviesFragment extends Fragment {
    private RecyclerView mRecyclerView;
    private static final String TAG = "PopularMoviesFragment";
    private List<MovieItem> mItems = new ArrayList<>();


    private static class FetchItemsTask extends AsyncTask<Void, Void, List<MovieItem>> {
        private final String mSortCriterion;
        @NonNull
        private final WeakReference<PopularMoviesFragment> mFragment;

        FetchItemsTask(PopularMoviesFragment fragment, final String sortCriterion) {
            mFragment = new WeakReference<>(fragment);
            mSortCriterion = sortCriterion;
        }
        @NonNull
        @Override
        protected List<MovieItem> doInBackground(Void... voids) {
            return new MovieApi(mSortCriterion).fetchItems();
        }

        @Override
        protected void onPostExecute(List<MovieItem> movieItems) {
            PopularMoviesFragment fragment = mFragment.get();
            if(fragment != null) {
                fragment.updateItems(movieItems);
                fragment.setupAdapter();
            }
        }
    }

    private void updateItems(List<MovieItem> items) {
        mItems = items;
    }

    private class MovieHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView mImageView;
        private final TextView mTextView;
        private MovieItem mMovieItem;

        MovieHolder(@NonNull View itemView) {
            super(itemView);
            mImageView = itemView.findViewById(R.id.item_image_view);

            mTextView = itemView.findViewById(R.id.item_image_title);

            itemView.setOnClickListener(this);

        }

        void bindMovieItem(@NonNull MovieItem movieItem) {
            mMovieItem = movieItem;
            Picasso.get()
                    .load(movieItem.getUrl())
                    .placeholder(R.drawable.ic_baseline_photo_24px)
                    .error(R.drawable.ic_baseline_block_24px)
                    .into(mImageView);
            mTextView.setText(movieItem.getTitle());
        }

        @Override
        public void onClick(View v) {
            Intent intent = MovieActivity.newIntent(getActivity(), mMovieItem);
            startActivity(intent);

        }
    }

    private class MovieAdapter extends RecyclerView.Adapter<MovieHolder> {
        private final List<MovieItem> mMovieItems;

        MovieAdapter(List<MovieItem> items) {
            mMovieItems = items;
        }

        @NonNull
        @Override
        public MovieHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.movie_grid_item, viewGroup, false);

            return new MovieHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull MovieHolder movieHolder, int i) {
            MovieItem movieItem = mMovieItems.get(i);

            movieHolder.bindMovieItem(movieItem);
        }

        @Override
        public int getItemCount() {
            return mMovieItems.size();
        }
    }

    public static PopularMoviesFragment newInstance() {
        return new PopularMoviesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
        new FetchItemsTask(this, MovieApi.POPULARITY_SORT).execute();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.movies_activity_fragment, container, false);
        mRecyclerView = v.findViewById(R.id.movie_recycler_view);

        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        setupAdapter();

        return v;
    }


    private void setupAdapter() {
        if(isAdded()) {
            mRecyclerView.setAdapter(new MovieAdapter(mItems));
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_popular_movies, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_sort_by_popularity:
                new FetchItemsTask(this, MovieApi.POPULARITY_SORT).execute();
                return true;

            case R.id.action_sort_by_toprated:
                new FetchItemsTask(this, MovieApi.TOP_RATED_SORT).execute();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
