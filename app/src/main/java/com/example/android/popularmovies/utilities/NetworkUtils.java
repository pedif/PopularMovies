package com.example.android.popularmovies.utilities;

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
    private static final String MOVIE_API_URL = "https://api.themoviedb.org/3/movie/";
    private static final String MOVIE_POPULAR_URL = "popular";
    private static final String MOVIE_TOP_URL = "top_rated";
    private static final String QUERY_API_KEY = "api_key";


    /**
     * Builds an URL upon movie list url with added query params
     *
     * @param childUrl dynamic url appended to base api url
     * @return
     */
    private static URL buildUrl(String childUrl) {
        Uri buildUri = Uri.parse(MOVIE_API_URL + childUrl).buildUpon()
                .appendQueryParameter(QUERY_API_KEY, BuildConfig.MOVIE_API_KEY).build();
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


}

