package com.example.android.popularmovies.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;

import com.example.android.popularmovies.services.UpdateMovieDBIntentService;

import java.net.URI;

/**
 * Created by Pedram on 1/8/2017.
 */

public class IntentUtils {

    private static final String YOUTUBE_QUERY_VIDEO = "v";

    public static Intent getYouTubeIntent(String key) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.appendPath("youtube.com");
        builder.appendPath("watch");
        builder.appendQueryParameter(YOUTUBE_QUERY_VIDEO, key);

        return new Intent(Intent.ACTION_VIEW,
                builder.build());
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    public static Intent getShareIntent(Activity activity, String name, String youtubeKey) {

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http");
        builder.appendPath("youtube.com");
        builder.appendPath("watch");
        builder.appendQueryParameter(YOUTUBE_QUERY_VIDEO, youtubeKey);

        Intent shareIntent = ShareCompat.IntentBuilder.from(activity)
                .setType("text/plain")
                .setText("Check "+name+" Out: " + builder.build().toString())
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return shareIntent;

    }

    public static Intent getPopularMovieUpdateIntent(Context context) {
        Intent intent = new Intent(context, UpdateMovieDBIntentService.class);
        intent.setAction(UpdateMovieDBIntentService.ACTION_FETCH_POPULAR);
        return intent;
    }

    public static Intent getHighVoteMovieUpdateIntent(Context context) {
        Intent intent = new Intent(context, UpdateMovieDBIntentService.class);
        intent.setAction(UpdateMovieDBIntentService.ACTION_FETCH_VOTE);
        return intent;
    }

}
