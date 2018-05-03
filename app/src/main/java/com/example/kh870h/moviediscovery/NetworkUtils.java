package com.example.kh870h.moviediscovery;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import static com.example.kh870h.moviediscovery.BuildConfig.THE_MOVIE_DB;

/**
 * Created by kh870h on 1/11/2018.
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();
    private final static ArrayList<MovieItem> movieItems = new ArrayList<>();
    public static String movieTrailers_URL = "http://api.themoviedb.org/3/movie/";

    public NetworkUtils(ArrayList<MovieItem> movieItems) {
    }

    //http://api.themoviedb.org/3/movie/346364/videos?api_key=

    public static URL movieTrailersBuildUrl(String id) {
        Uri movieTrailerUrl = Uri.parse(movieTrailers_URL).buildUpon()
                .appendPath(id)
                .appendPath("videos")
                .appendQueryParameter("api_key", THE_MOVIE_DB)
                .build();

        URL url = null;
        try {
            url = new URL(movieTrailerUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    //http://api.themoviedb.org/3/movie/346364/reviews?api_key=

    public static URL movieReviewsBuildUrl(String id) {
        Uri movieReviewsUrl = Uri.parse(movieTrailers_URL).buildUpon()
                .appendPath(id)
                .appendPath("reviews")
                .appendQueryParameter("api_key", THE_MOVIE_DB)
                .build();

        URL url = null;
        try {
            url = new URL(movieReviewsUrl.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        Log.v(TAG, "Built URI " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
