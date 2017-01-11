package com.example.android.popularmovies.utilities;

import com.example.android.popularmovies.models.Movie;
import com.example.android.popularmovies.models.Review;
import com.example.android.popularmovies.models.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.example.android.popularmovies.utilities.Constants.MOVIE_PIC_BASE_PATH;

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
    private static final String TAG_MOVIE_POSTER = "poster_path";
    private static final String TAG_MOVIE_DATE = "release_date";
    private static final String TAG_MOVIE_OVERVIEW = "overview";


    private static final String TAG_TRAILER_LIST = "results";
    private static final String TAG_TRAILER_ID = "id";
    private static final String TAG_TRAILER_KEY = "key";
    private static final String TAG_TRAILER_NAME = "name";


    private static final String TAG_REVIEW_LIST = "results";
    private static final String TAG_REVIEW_ID = "id";
    private static final String TAG_REVIEW_AUTHOR = "author";
    private static final String TAG_REVIEW_CONTENT = "content";


    /**
     * Returns a list containing all movie objects from the json Object
     *
     * @param data representing a jsonObject string value
     * @return
     * @throws JSONException
     */
    public static List<Movie> getMovieListFromJson(String data) throws JSONException {

        JSONObject json = new JSONObject(data);
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
            movie.setPoster(MOVIE_PIC_BASE_PATH + item.getString(TAG_MOVIE_POSTER));
            movie.setDate(item.getString(TAG_MOVIE_DATE));
            movies.add(movie);
        }
        return movies;
    }

    /**
     * Returns a list containing all movie objects from the json Object
     *
     * @param data representing a jsonObject string value
     * @return
     * @throws JSONException
     */
    public static Movie getMovieFullInfoFromJson(String data) throws JSONException {

        JSONObject json = new JSONObject(data);

        Movie movie = new Movie();
        movie.setId(json.getLong(TAG_MOVIE_ID));
        movie.setTitle(json.getString(TAG_MOVIE_TITLE));
        movie.setPopularity(json.getDouble(TAG_MOVIE_POPULARITY));
        movie.setVote(json.getDouble(TAG_MOVIE_VOTE));
        movie.setPoster(MOVIE_PIC_BASE_PATH + json.getString(TAG_MOVIE_POSTER));
        movie.setDate(json.getString(TAG_MOVIE_DATE));
        movie.setOverview(json.getString(TAG_MOVIE_OVERVIEW));
        return movie;
    }


    public static ArrayList<Trailer> getTrailerListFromJson(String data) throws JSONException {

        JSONObject json = new JSONObject(data);
        JSONArray jsonArray = json.getJSONArray(TAG_TRAILER_LIST);
        JSONObject item;
        Trailer trailer;
        ArrayList<Trailer> trailers = (jsonArray.length() > 0) ? new ArrayList<Trailer>() : null;
        for (int i = 0; i < jsonArray.length(); i++) {
            item = jsonArray.getJSONObject(i);
            trailer = new Trailer();
            trailer.setId(item.getString(TAG_TRAILER_ID));
            trailer.setName(item.getString(TAG_TRAILER_NAME));
            trailer.setKey(item.getString(TAG_TRAILER_KEY));
            trailers.add(trailer);
        }
        return trailers;
    }

    public static ArrayList<Review> getReviewListFromJson(String data) throws JSONException {

        JSONObject json = new JSONObject(data);
        JSONArray jsonArray = json.getJSONArray(TAG_REVIEW_LIST);
        JSONObject item;
        Review review;
        ArrayList<Review> reviews = (jsonArray.length() > 0) ? new ArrayList<Review>() : null;
        for (int i = 0; i < jsonArray.length(); i++) {
            item = jsonArray.getJSONObject(i);
            review = new Review();
            review.setId(item.getString(TAG_REVIEW_ID));
            review.setAuthor(item.getString(TAG_REVIEW_AUTHOR));
            review.setContent(item.getString(TAG_REVIEW_CONTENT));
            reviews.add(review);
        }
        return reviews;
    }
}
