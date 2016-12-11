package com.example.android.popularmovies.fragments;


import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MovieDBJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.views.MovieListAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements MovieListAdapter.InteractionListener {

    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static MovieListFragment instance;

    private RecyclerView moviesRecyclerView;
    private MovieListAdapter mAdapter;

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
        View rootView = inflater.inflate(R.layout.fragment_movie_list, container, false);

        moviesRecyclerView = (RecyclerView) rootView.findViewById(R.id.frag_movie_list_rv_movies);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(layoutManager);

        return rootView;
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

    @Override
    public void onItemClickListener() {

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
            Log.v(TAG, s);
            try {
                List<Movie> data = MovieDBJsonUtils.getMovieListFromJson(s);
                Log.v(TAG, String.valueOf(data.size()));
                mAdapter = new MovieListAdapter(data, MovieListFragment.this);
                moviesRecyclerView.setAdapter(mAdapter);
            } catch (JSONException e) {
                Log.v(TAG, e.getMessage());
            }
        }
    }
}
