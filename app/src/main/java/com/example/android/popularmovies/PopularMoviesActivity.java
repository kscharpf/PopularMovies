package com.example.android.popularmovies;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class PopularMoviesActivity extends SingleFragmentActivity {

    @NonNull
    @Override
    protected Fragment createFragment() {
        return PopularMoviesFragment.newInstance();
    }
}
