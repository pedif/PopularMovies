package com.example.android.popularmovies.data.db;


import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

/**
 * Created by Pedram on 1/5/2017.
 * Database Helper Class
 */

@Database(version = MovieDatabase.VERSION)
public final class MovieDatabase {

    private MovieDatabase() {
    }

    public static final int VERSION = 1;

    @Table(FavoriteMovieColumns.class)
    public static final String FAVORITE_MOVIES = "favorite_movies";
    @Table(MovieColumns.class)
    public static final String MOVIES = "movies";
}
