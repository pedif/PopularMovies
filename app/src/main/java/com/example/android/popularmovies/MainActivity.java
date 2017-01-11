package com.example.android.popularmovies;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmovies.data.db.FavoriteMovieColumns;
import com.example.android.popularmovies.data.db.MovieProvider;
import com.example.android.popularmovies.fragments.MovieListFragment;
import com.example.android.popularmovies.services.UpdateMovieDBIntentService;
import com.example.android.popularmovies.utilities.IntentUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container, MovieListFragment.getInstance()).commit();

        startService(IntentUtils.getPopularMovieUpdateIntent(this));
    }


}
