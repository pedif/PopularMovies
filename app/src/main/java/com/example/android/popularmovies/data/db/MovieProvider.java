package com.example.android.popularmovies.data.db;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

/**
 * Created by Pedram on 1/5/2017.
 */
@ContentProvider(authority = MovieProvider.AUTHORITY, database = MovieDatabase.class)
public class MovieProvider {

    public static final String AUTHORITY =
            "com.example.android.popularmovies.data.db.MovieProvider";

    static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    static final String FAVORITE_MOVIES_PATH = MovieDatabase.FAVORITE_MOVIES;
    static final String MOVIES_PATH = MovieDatabase.MOVIES;

    /**
     * Build a Uri based on parameters provided
     * @param paths
     * @return
     */
    private static Uri buildUri(String... paths) {
        Uri.Builder builder = BASE_CONTENT_URI.buildUpon();
        for (String path : paths)
            builder.appendPath(path);
        return builder.build();
    }

    @TableEndpoint(table = MovieDatabase.FAVORITE_MOVIES)
    public static class FavoriteMovies {
        @ContentUri(
                path = FAVORITE_MOVIES_PATH,
                type = "vnd.android.cursor.dir/popular_db",
                defaultSort = FavoriteMovieColumns.DATE + " DESC")
        public static final Uri CONTENT_URI = buildUri(FAVORITE_MOVIES_PATH);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = FAVORITE_MOVIES_PATH + "/#",
                type = "vnd.android.cursor.item/" + MovieDatabase.FAVORITE_MOVIES,
                whereColumn = FavoriteMovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(FAVORITE_MOVIES_PATH, String.valueOf(id));
        }

    }

    @TableEndpoint(table = MovieDatabase.MOVIES)
    public static class Movies {
        @ContentUri(
                path = MOVIES_PATH,
                type = "vnd.android.cursor.dir/popular_db",
                defaultSort = MovieColumns.DATE + " DESC")
        public static final Uri CONTENT_URI = buildUri(MOVIES_PATH);

        @InexactContentUri(
                name = "MOVIE_ID",
                path = MOVIES_PATH + "/#",
                type = "vnd.android.cursor.item/" + MovieDatabase.MOVIES,
                whereColumn = MovieColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id) {
            return buildUri(MOVIES_PATH, String.valueOf(id));
        }

    }

}
