package com.example.android.popularmovies;

import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.android.popularmovies.fragments.MovieDetailsFragment;
import com.example.android.popularmovies.fragments.MovieListFragment;
import com.example.android.popularmovies.utilities.Constants;

import static com.example.android.popularmovies.utilities.Constants.EXTRA_MOVIE_ID;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().hasExtra(EXTRA_MOVIE_ID)) {
            getSupportFragmentManager().beginTransaction().replace(R.id.frag_container,
                    MovieDetailsFragment.getInstance(getIntent().getLongExtra(EXTRA_MOVIE_ID, 0))).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            NavUtils.navigateUpFromSameTask(this);
        return super.onOptionsItemSelected(item);
    }
}
