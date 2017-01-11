package com.example.android.popularmovies.fragments;


import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.RemoteException;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.db.FavoriteMovieColumns;
import com.example.android.popularmovies.data.db.MovieProvider;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;
import com.example.android.popularmovies.utilities.IntentUtils;
import com.example.android.popularmovies.utilities.MovieDBJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.views.MovieDetailRecyclerAdapter;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieDetailsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = MovieDetailsFragment.class.getSimpleName();
    private static final String Movie_Data = "movie_data";
    private static final String TRAILERS_DATA = "trailer_data";
    private static final String REVIEWS_DATA = "trailer_data";
    private static final String IS_FAVORITE_DATA = "favorite_data";

    private static long movieId;
    RecyclerView mDetailsRecyclerView;
    MovieDetailRecyclerAdapter mAdapter;
    RecyclerView mExtraRecyclerView;
    MovieDetailRecyclerAdapter mExtraAdapter;
    ImageView mFavoriteImageView;
    TextView mTitleTextView;


    private boolean mTwoPane;
    private Movie mMovie;
    private ArrayList<Trailer> mTrailers;
    private ArrayList<Review> mReviews;
    private boolean mIsFavorite;

    public static MovieDetailsFragment getInstance(long id) {

        movieId = id;
        return new  MovieDetailsFragment();
    }

    public MovieDetailsFragment() {
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        mFavoriteImageView = (ImageView) rootView.findViewById(R.id.detail_img_favorite);
        mTitleTextView = (TextView) rootView.findViewById(R.id.frag_detail_movie_tv_title);

        mDetailsRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_rv);
        mDetailsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mExtraRecyclerView = (RecyclerView) rootView.findViewById(R.id.detail_rv2);

        //If there are two recycler views we are in two pane mode
        if (mExtraRecyclerView != null) {
            mTwoPane = true;
            mExtraRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else
            mTwoPane = false;

        mFavoriteImageView.setOnClickListener(this);
        rootView.findViewById(R.id.detail_img_share).setOnClickListener(this);

        if (savedInstanceState != null) {

            if (savedInstanceState.containsKey(IS_FAVORITE_DATA))
                mIsFavorite = savedInstanceState.getBoolean(IS_FAVORITE_DATA);

            if (savedInstanceState.containsKey(Movie_Data)) {
                mMovie = savedInstanceState.getParcelable(Movie_Data);
                mTitleTextView.setText(mMovie.getTitle());
            }

            if (savedInstanceState.containsKey(TRAILERS_DATA))
                mTrailers = savedInstanceState.getParcelableArrayList(TRAILERS_DATA);

            if (savedInstanceState.containsKey(REVIEWS_DATA))
                mReviews = savedInstanceState.getParcelableArrayList(REVIEWS_DATA);


        } else {
            //Query the db to see if there are already available data to be displayed
            Cursor cursor = getActivity().getContentResolver().query(MovieProvider.FavoriteMovies.withId(movieId), null,
                    null, null, null);
            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                mMovie = new Movie();
                mMovie.setId(movieId);
                mMovie.setTitle(cursor.getString(FavoriteMovieColumns.TITLE_INDEX));
                mMovie.setOverview(cursor.getString(FavoriteMovieColumns.OVERVIEW_INDEX));
                mMovie.setPoster(cursor.getString(FavoriteMovieColumns.POSTER_INDEX));
                mMovie.setPopularity(cursor.getDouble(FavoriteMovieColumns.POPULARITY_INDEX));
                mMovie.setVote(cursor.getDouble(FavoriteMovieColumns.VOTE_INDEX));
                mMovie.setDate(cursor.getString(FavoriteMovieColumns.DATE_INDEX));
                mFavoriteImageView.setImageResource(R.drawable.ic_favorite_activated);
                mIsFavorite = true;
                cursor.close();
            } else
                mIsFavorite = false;
            new GetMovieDataTask().execute();
        }

        return rootView;
    }

    @Override
    public void onClick(View view) {
        if(mMovie==null)
            return;
        switch (view.getId()) {
            case R.id.detail_img_favorite:
                setFavoriteMovie();
                break;
            case R.id.detail_img_share:
                getActivity().startActivity(
                        IntentUtils.getShareIntent(getActivity(), mMovie.getTitle(), mTrailers.get(0).getKey()));
                break;
        }
    }

    /**
     * Adds/Removes the movie from favorite movie list
     */
    private void setFavoriteMovie() {
        String msg = "";
        if (mIsFavorite) {
            int result = getActivity().getContentResolver().delete(MovieProvider.FavoriteMovies.withId(movieId), null, null);
            Log.e(TAG, String.valueOf(result));
            mFavoriteImageView.setImageResource(R.drawable.ic_favorite);
            msg = mMovie.getTitle() + " " + getString(R.string.message_deleted);
            mIsFavorite = false;
        } else {
            ContentValues initialValues = new ContentValues();
            initialValues.put(FavoriteMovieColumns._ID, mMovie.getId());
            initialValues.put(FavoriteMovieColumns.TITLE, mMovie.getTitle());
            initialValues.put(FavoriteMovieColumns.VOTE, mMovie.getVote());
            initialValues.put(FavoriteMovieColumns.POPULARITY, mMovie.getPopularity());
            initialValues.put(FavoriteMovieColumns.GENRE, mMovie.getGenre());
            initialValues.put(FavoriteMovieColumns.DATE, mMovie.getDate());
            initialValues.put(FavoriteMovieColumns.OVERVIEW, mMovie.getOverview());
            initialValues.put(FavoriteMovieColumns.POSTER, mMovie.getPoster());


            Uri uri = getActivity().getContentResolver().insert(MovieProvider.FavoriteMovies.CONTENT_URI, initialValues);
            if (uri != null) {
                Log.e(TAG, uri.toString());
                mFavoriteImageView.setImageResource(R.drawable.ic_favorite_activated);
                mIsFavorite = true;
                msg = mMovie.getTitle() + " " + getString(R.string.message_added);

            }
        }
        Snackbar snackbar = Snackbar
                .make(mFavoriteImageView, msg, Snackbar.LENGTH_LONG);

        snackbar.show();

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(IS_FAVORITE_DATA, mIsFavorite);
        outState.putParcelable(Movie_Data, mMovie);
        outState.putParcelableArrayList(TRAILERS_DATA, mTrailers);
        outState.putParcelableArrayList(REVIEWS_DATA, mReviews);
        super.onSaveInstanceState(outState);
    }

    private class GetMovieDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String[] doInBackground(Void... integers) {

            String[] result = new String[3];
            try {
                if (mMovie == null || mMovie.getId() != movieId)
                    result[0] = NetworkUtils.getMovieData(movieId);
                if (mTrailers == null)
                    result[1] = NetworkUtils.getMovieTrailerList(movieId);
                if (mReviews == null)
                    result[2] = NetworkUtils.getMovieReviewList(movieId);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }


        @Override
        protected void onPostExecute(String[] s) {
            super.onPostExecute(s);

            try {
                if ((mMovie == null || mMovie.getId() != movieId) && s[0] != null)
                    mMovie = MovieDBJsonUtils.getMovieFullInfoFromJson(s[0]);
                if (mTrailers == null && s[1] != null)
                    mTrailers = MovieDBJsonUtils.getTrailerListFromJson(s[1]);
                if (mReviews == null && s[2] != null)
                    mReviews = MovieDBJsonUtils.getReviewListFromJson(s[2]);

                if(mMovie!=null)
                mTitleTextView.setText(mMovie.getTitle());
                else
                mTitleTextView.setText(R.string.message_no_data);

                if (mTwoPane) {
                    mExtraAdapter = new MovieDetailRecyclerAdapter(getContext(), null,
                            mTrailers, mReviews);
                    mExtraRecyclerView.setAdapter(mExtraAdapter);
                    mAdapter = new MovieDetailRecyclerAdapter(getContext(), mMovie,
                            null, null);
                    mDetailsRecyclerView.setAdapter(mAdapter);
                } else {
                    mAdapter = new MovieDetailRecyclerAdapter(getContext(), mMovie,
                            mTrailers, mReviews);
                    mDetailsRecyclerView.setAdapter(mAdapter);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
