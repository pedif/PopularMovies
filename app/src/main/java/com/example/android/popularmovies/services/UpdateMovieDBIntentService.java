package com.example.android.popularmovies.services;

import android.app.IntentService;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.Context;
import android.content.OperationApplicationException;
import android.os.RemoteException;
import android.util.Log;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.db.FavoriteMovieColumns;
import com.example.android.popularmovies.data.db.MovieColumns;
import com.example.android.popularmovies.data.db.MovieProvider;
import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.utilities.MovieDBJsonUtils;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 */
public class UpdateMovieDBIntentService extends IntentService {

    public static final String TAG = UpdateMovieDBIntentService.class.getName();
    public static final String ACTION_FETCH_POPULAR = "com.example.android.popularmovies.services.action.fetch.popular";
    public static final String ACTION_FETCH_VOTE = "com.example.android.popularmovies.services.action.fetch.vote";


    public UpdateMovieDBIntentService() {
        super("UpdateMovieDBIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                List<Movie> movies = new ArrayList<>();
                String json = "";
                final String action = intent.getAction();
                if (ACTION_FETCH_POPULAR.equals(action)) {

                    json = NetworkUtils.getPopularMovieList();

                } else if (ACTION_FETCH_VOTE.equals(action)) {
                    json = NetworkUtils.getHighestRatedMovieList();
                }

                if (json.length() == 0)
                    return;
                movies = MovieDBJsonUtils.getMovieListFromJson(json);


                ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>(movies.size());
                ArrayList<Long> ids = new ArrayList<>();
                for (Movie movie : movies) {
                    ids.add(movie.getId());
                    ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(MovieProvider.Movies.CONTENT_URI);
                    builder.withValue(FavoriteMovieColumns._ID, movie.getId());
                    builder.withValue(FavoriteMovieColumns.TITLE, movie.getTitle());
                    builder.withValue(FavoriteMovieColumns.VOTE, movie.getVote());
                    builder.withValue(FavoriteMovieColumns.POPULARITY, movie.getPopularity());
                    builder.withValue(FavoriteMovieColumns.GENRE, movie.getGenre());
                    builder.withValue(FavoriteMovieColumns.DATE, movie.getDate());
                    builder.withValue(FavoriteMovieColumns.OVERVIEW, movie.getOverview());
                    builder.withValue(FavoriteMovieColumns.POSTER, movie.getPoster());
                    batchOperations.add(builder.build());
                }


                try {
                    getContentResolver().delete(MovieProvider.Movies.CONTENT_URI,"1>0",null);
                    getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
                } catch (RemoteException | OperationApplicationException e) {
                    Log.e(TAG, "Error applying batch insert", e);
                }
            } catch (IOException | JSONException e) {

                e.printStackTrace();
            }
        }
    }


}
