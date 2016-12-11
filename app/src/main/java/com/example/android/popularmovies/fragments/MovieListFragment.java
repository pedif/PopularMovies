package com.example.android.popularmovies.fragments;


import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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

import com.example.android.popularmovies.DetailActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.Constants;
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
    private static final String STATE_LAYOUT_MANAGER = "manager_state";
    private static MovieListFragment instance;

    private RecyclerView moviesRecyclerView;
    private MovieListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

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

        //TODO restoring state is not working....
        if(moviesRecyclerView.getLayoutManager()==null) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2, GridLayoutManager.VERTICAL, false);
            moviesRecyclerView.setLayoutManager(mLayoutManager);
        }else if(savedInstanceState!=null){
            Parcelable managerState = savedInstanceState.getParcelable(STATE_LAYOUT_MANAGER);
            if(managerState!=null)
             moviesRecyclerView.getLayoutManager().onRestoreInstanceState(managerState);
        }

        return rootView;
    }

    //TODO Not working
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STATE_LAYOUT_MANAGER,moviesRecyclerView.getLayoutManager().onSaveInstanceState());
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
    public void onItemClickListener(long id) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.EXTRA_MOVIE_ID,id);
        startActivity(intent);
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
