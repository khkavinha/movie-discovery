package com.example.kh870h.moviediscovery.MovieReviewsDisplay;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import com.example.kh870h.moviediscovery.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by kavin on 2/10/2018.
 */

public class MovieReviewsAsyncTask extends AsyncTaskLoader<ArrayList<MovieReviews>> {
    public static final String TAG = "Movie Data Loader";
    public ArrayList<MovieReviews> movieReviews;
    public String movieReviewsId;

    public MovieReviewsAsyncTask(Context context, String movieReviewsId) {
        super(context);
        this.movieReviewsId = movieReviewsId;
    }

    @Override
    public ArrayList<MovieReviews> loadInBackground() {
        Log.i(TAG, "LoadInBackground: START; Verifies movie reviews url is passing to MovieReviewsAsyncTask.java");
        URL movieReviewsBuildUrls = NetworkUtils.movieReviewsBuildUrl(movieReviewsId);

        if (movieReviewsBuildUrls == null) {
            return null;
        }
        try {
            movieReviews = getMovieReviewsJsonObject(NetworkUtils.getResponseFromHttpUrl(movieReviewsBuildUrls));
            return movieReviews;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static ArrayList<MovieReviews> getMovieReviewsJsonObject(String json) throws JSONException {
        final String mr_Results = "results";
        final String mr_id = "id";
        final String mr_author = "author";
        final String mr_content = "content";
        final String mr_url = "url";

        ArrayList<MovieReviews> movieReviews = new ArrayList<>();
        JSONArray results = new JSONObject(json).getJSONArray(mr_Results);

        for (int i = 0; i < results.length(); i++) {
            JSONObject result_movieReviews = results.getJSONObject(i);
            MovieReviews movieReviews1 = new MovieReviews();
            movieReviews1.setId(result_movieReviews.getString(mr_id));
            movieReviews1.setAuthor(result_movieReviews.getString(mr_author));
            movieReviews1.setContent(result_movieReviews.getString(mr_content));
            movieReviews1.setUrl(result_movieReviews.getString(mr_url));
            movieReviews.add(movieReviews1);
        }
        return movieReviews;
    }

}
