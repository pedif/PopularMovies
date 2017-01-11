package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Pedram on 12/11/2016.
 */
public class NetworkUtils {

    //Tag used for logging
    private static final String TAG = NetworkUtils.class.getSimpleName();

    //Specific urls used for accessing different data from the movie DB API
    private static final String MOVIE_API_URL = "https://api.themoviedb.org/3/movie";
    private static final String MOVIE_POPULAR_URL = "popular";
    private static final String MOVIE_TOP_URL = "top_rated";
    private static final String MOVIE_TRAILER = "videos";
    private static final String MOVIE_REVIEW = "reviews";
    private static final String QUERY_API_KEY = "api_key";

    /**
     * Checks network connectivity
     *
     * @param context
     * @return
     */
    public static boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }

    /**
     * Builds an URL upon movie list url with added query params
     *
     * @param paths list of dynamic paths to be appended to  base uri
     * @return
     */
    private static URL buildUrl(String... paths) {

        Uri.Builder buildUri = Uri.parse(MOVIE_API_URL).buildUpon();
        for (String path : paths) {
            buildUri.appendPath(path);
        }
        buildUri.appendQueryParameter(QUERY_API_KEY, BuildConfig.MOVIE_API_KEY).build();

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Wrong URl");
        }
        return url;
    }

    /**
     * Executes a http request of the given url and returns the result
     *
     * @param url target http url
     * @return
     * @throws IOException
     */
    private static final String getResponseFromHttpUrl(URL url) throws IOException {

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = con.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            con.disconnect();
        }
    }

    /**
     * returns a string containing movies sorted by their popularity
     *
     * @return
     * @see NetworkUtils#buildUrl
     * @see NetworkUtils#getResponseFromHttpUrl(URL)
     */
    public static String getPopularMovieList() throws IOException {

        return getResponseFromHttpUrl(buildUrl(MOVIE_POPULAR_URL));
    }

    /**
     * returns a string containing movies sorted by their average rate
     *
     * @return
     * @see NetworkUtils#buildUrl
     * @see NetworkUtils#getResponseFromHttpUrl(URL)
     */
    public static String getHighestRatedMovieList() throws IOException {

        return getResponseFromHttpUrl(buildUrl(MOVIE_TOP_URL));

    }

    /**
     * returns a string containing full information of a movie
     *
     * @param id Movie ID
     * @return
     * @see NetworkUtils#buildUrl
     * @see NetworkUtils#getResponseFromHttpUrl(URL)
     */
    public static String getMovieData(long id) throws IOException {

        return getResponseFromHttpUrl(buildUrl(String.valueOf(id)));

    }

    /**
     * returns a string containing trailers of a movie
     *
     * @param id Movie ID
     * @return
     * @see NetworkUtils#buildUrl
     * @see NetworkUtils#getResponseFromHttpUrl(URL)
     */
    public static String getMovieTrailerList(long id) throws IOException {

        return getResponseFromHttpUrl(buildUrl(String.valueOf(id), MOVIE_TRAILER));

    }

    /**
     * returns a string containing trailers of a movie
     *
     * @param id Movie ID
     * @return
     * @see NetworkUtils#buildUrl
     * @see NetworkUtils#getResponseFromHttpUrl(URL)
     */
    public static String getMovieReviewList(long id) throws IOException {

        return getResponseFromHttpUrl(buildUrl(String.valueOf(id), MOVIE_REVIEW));

    }

}

