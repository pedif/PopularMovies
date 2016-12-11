package com.example.android.popularmovies.fragments;


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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.Constants;
import com.example.android.popularmovies.utilities.MovieDBJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.views.MovieListAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import static com.example.android.popularmovies.utilities.Constants.MOVIE_PIC_BASE_PATH;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    private static MovieDetailsFragment instance;
    private static long movieId;

    private ImageView mPosterImageView;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private TextView mRateTextView;
    private TextView mOverviewTextView;

    public static MovieDetailsFragment getInstance(long id) {
        if (instance == null) {
            instance = new MovieDetailsFragment();
        }
        movieId = id;
        return instance;
    }

    public MovieDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        mPosterImageView = (ImageView) rootView.findViewById(R.id.frag_detail_movie_img);
        mTitleTextView = (TextView) rootView.findViewById(R.id.frag_detail_movie_tv_title);
        mDateTextView = (TextView) rootView.findViewById(R.id.frag_detail_movie_tv_date);
        mRateTextView = (TextView) rootView.findViewById(R.id.frag_detail_movie_tv_rate);
        mOverviewTextView = (TextView) rootView.findViewById(R.id.frag_detail_movie_tv_overview);
        new GetMovieDataTask().execute();
        return rootView;
    }

    private class GetMovieDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... integers) {

            try {
                return NetworkUtils.getMovieData(movieId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.v(TAG, s);
            try {
                Movie movie = MovieDBJsonUtils.getMovieFullInfoFromJson(s);
                Picasso.with(getActivity()).load(MOVIE_PIC_BASE_PATH+movie.getPoster()).into(mPosterImageView);
                mTitleTextView.setText(movie.getTitle());
                mDateTextView.setText(movie.getDate());
                mRateTextView.setText(movie.getVote()+"/10");
                mOverviewTextView.setText(movie.getOverview());
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
