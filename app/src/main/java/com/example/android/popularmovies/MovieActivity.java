package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.Objects;

public class MovieActivity extends SingleFragmentActivity {

    private static final String MOVIE_ITEM_EXTRA ="com.example.android.popularmovies.movieitem";

    @NonNull
    public static Intent newIntent(Context context, MovieItem movieItem) {
        Intent intent = new Intent(context, MovieActivity.class);
        intent.putExtra(MOVIE_ITEM_EXTRA, movieItem);

        return intent;
    }

    @Override
    protected Fragment createFragment() {
        MovieItem movieItem = Objects.requireNonNull(getIntent().getExtras()).getParcelable(MOVIE_ITEM_EXTRA);
        return MovieFragment.newInstance(movieItem);
    }
}
