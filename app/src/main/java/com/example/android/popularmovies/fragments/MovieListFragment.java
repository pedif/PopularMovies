package com.example.android.popularmovies.fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.utilities.NetworkUtils;

import java.io.IOException;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static MovieListFragment instance;


    public static MovieListFragment getInstance() {
        if (instance == null) {
            instance = new MovieListFragment();
        }
        return instance;
    }

    public MovieListFragment() {
        setHasOptionsMenu(true);
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.activity_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
            case R.id.action_highest_rate:
                new GetMovieListTask().execute(item.getItemId());
                return true;
        }
        return false;
    }

    private class GetMovieListTask extends AsyncTask<Integer, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Integer... integers) {

            try {

                switch (integers[0]) {
                    case R.id.action_popular:
                        return NetworkUtils.getPopularMovieList();
                    case R.id.action_highest_rate:
                        return NetworkUtils.getHighestRatedMovieList();
                }

            } catch (IOException e) {
                Log.v(TAG, e.getMessage());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG,s);
        }
    }
}
