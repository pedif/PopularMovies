package com.example.android.popularmovies.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.DetailActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.db.MovieColumns;
import com.example.android.popularmovies.data.db.MovieProvider;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.Constants;
import com.example.android.popularmovies.utilities.IntentUtils;
import com.example.android.popularmovies.utilities.MovieDBJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.views.MovieListAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment implements MovieListAdapter.InteractionListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = MovieListFragment.class.getSimpleName();
    private static final String STATE_LAYOUT_MANAGER = "manager_state";

    private static MovieListFragment instance;

    private RecyclerView moviesRecyclerView;
    private TextView mNoDataTextView;
    private MovieListAdapter mAdapter;
    private GridLayoutManager mLayoutManager;

    private static final int ID_MOVIE_LOADER = 123;
    private static final int ID_FAVORITE_LOADER = 456;

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
        mNoDataTextView =(TextView) rootView.findViewById(R.id.frag_movie_list_tv_no_data);

        mLayoutManager = new GridLayoutManager(getActivity(),
                getActivity().getResources().getInteger(R.integer.grid_column_count), GridLayoutManager.VERTICAL, false);
        moviesRecyclerView.setLayoutManager(mLayoutManager);

        //restore previous state
        if (savedInstanceState != null) {


            if (savedInstanceState.containsKey(STATE_LAYOUT_MANAGER)) {
                moviesRecyclerView.getLayoutManager()
                        .onRestoreInstanceState(savedInstanceState.getParcelable(STATE_LAYOUT_MANAGER));
            }

        }
            mAdapter = new MovieListAdapter(new ArrayList<Movie>(), MovieListFragment.this);
            moviesRecyclerView.setAdapter(mAdapter);


                /*
         * Ensures a loader is initialized and active. If the loader doesn't already exist, one is
         * created and (if the activity/fragment is currently started) starts the loader. Otherwise
         * the last created loader is re-used.
         */
        getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable(STATE_LAYOUT_MANAGER, moviesRecyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_popular:
                getActivity().startService(IntentUtils.getPopularMovieUpdateIntent(getActivity()));
                getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
                getActivity().getSupportLoaderManager().destroyLoader(ID_FAVORITE_LOADER);
                break;

            case R.id.action_highest_rate:
                getActivity().startService(IntentUtils.getHighVoteMovieUpdateIntent(getActivity()));
                getActivity().getSupportLoaderManager().initLoader(ID_MOVIE_LOADER, null, this);
                getActivity().getSupportLoaderManager().destroyLoader(ID_FAVORITE_LOADER);
                return true;

            case R.id.action_favorite:
                getActivity().getSupportLoaderManager().initLoader(ID_FAVORITE_LOADER, null, this);
                getActivity().getSupportLoaderManager().destroyLoader(ID_MOVIE_LOADER);
        }
        return false;
    }


    @Override
    public void onItemClickListener(long id) {

        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(Constants.EXTRA_MOVIE_ID, id);
        startActivity(intent);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        switch (id) {
            case ID_FAVORITE_LOADER:
                return new CursorLoader(getContext(), MovieProvider.FavoriteMovies.CONTENT_URI, MovieColumns.projection, null, null, null);

            case ID_MOVIE_LOADER:
                return new CursorLoader(getContext(), MovieProvider.Movies.CONTENT_URI, null, null, null, null);

        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Cursor data) {

        if (data == null || data.getCount() == 0) {
            mAdapter.setItems(new ArrayList<Movie>());
            mNoDataTextView.setVisibility(View.VISIBLE);
            return;
        }
        ArrayList<Movie> items = new ArrayList<>();
        data.moveToFirst();
        while (!data.isAfterLast()) {
            Movie movie = new Movie();
            movie.setId(data.getLong(MovieColumns.ID_INDEX));
            movie.setTitle(data.getString(MovieColumns.TITLE_INDEX));
            movie.setDate(data.getString(MovieColumns.DATE_INDEX));
            movie.setGenre(data.getString(MovieColumns.GENRE_INDEX));
            movie.setOverview(data.getString(MovieColumns.OVERVIEW_INDEX));
            movie.setPopularity(data.getDouble(MovieColumns.POPULARITY_INDEX));
            movie.setVote(data.getDouble(MovieColumns.VOTE_INDEX));
            movie.setPoster(data.getString(MovieColumns.POSTER_INDEX));
            items.add(movie);
            data.moveToNext();
        }

        mAdapter.setItems(items);
        mNoDataTextView.setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

}
