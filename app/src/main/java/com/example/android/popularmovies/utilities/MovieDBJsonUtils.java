package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Pedram on 12/11/2016.
 */
public class MovieDBJsonUtils {


    //Json Tags
    private static final String TAG_MOVIE_LIST = "results";
    private static final String TAG_MOVIE_ID = "id";
    private static final String TAG_MOVIE_TITLE = "title";
    private static final String TAG_MOVIE_POPULARITY = "popularity";
    private static final String TAG_MOVIE_VOTE = "vote_average";
    private static final String TAG_MOVIE_POSTER="poster_path";
    private static final String TAG_MOVIE_DATE="release_date";

    /**
     * Returns a list containing all movie objects from the json Object
     * @param result representing a jsonObject string value
     * @return
     * @throws JSONException
     */
    public static List<Movie> getMovieListFromJson(String result) throws JSONException {

        JSONObject json = new JSONObject(result);
        List<Movie> movies = new ArrayList<>();
        JSONArray jsonArray = json.getJSONArray(TAG_MOVIE_LIST);
        JSONObject item;
        Movie movie;
        for (int i = 0; i < jsonArray.length(); i++) {
            item = jsonArray.getJSONObject(i);
            movie = new Movie();
            movie.setId(item.getLong(TAG_MOVIE_ID));
            movie.setTitle(item.getString(TAG_MOVIE_TITLE));
            movie.setPopularity(item.getDouble(TAG_MOVIE_POPULARITY));
            movie.setVote(item.getDouble(TAG_MOVIE_VOTE));
            movie.setPoster(item.getString(TAG_MOVIE_POSTER));
            movie.setDate(item.getString(TAG_MOVIE_DATE));
            movies.add(movie);
        }
        return movies;
    }

}
