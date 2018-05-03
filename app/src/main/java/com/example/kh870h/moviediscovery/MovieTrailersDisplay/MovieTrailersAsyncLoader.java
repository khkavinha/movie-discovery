package com.example.kh870h.moviediscovery.MovieTrailersDisplay;

import android.content.Context;
import android.util.Log;
import android.support.v4.content.AsyncTaskLoader;

import com.example.kh870h.moviediscovery.NetworkUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MovieTrailersAsyncLoader extends AsyncTaskLoader<ArrayList<MovieTrailers>> {

    private static final String TAG = "Movie Data Loader";
    public ArrayList<MovieTrailers> movieTrailers;
    private String movieTrailersId;

    public MovieTrailersAsyncLoader(Context context, String movieTrailersId) {
        super(context);
        this.movieTrailersId = movieTrailersId;
    }

    @Override
    public ArrayList<MovieTrailers> loadInBackground() {
        Log.i(TAG, "LoadInBackground: START; Verifies movie trailers url is passing to MovieTrailersAsyncLoader.java");
        URL movieTrailersBuildUrls = NetworkUtils.movieTrailersBuildUrl(movieTrailersId);

        if (movieTrailersBuildUrls == null) {
            return null;
        }

        try {
            movieTrailers = getMovieTrailersJsonObject(NetworkUtils.getResponseFromHttpUrl(movieTrailersBuildUrls));
            return movieTrailers;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }

    public static ArrayList<MovieTrailers> getMovieTrailersJsonObject(String json) throws JSONException {
        final String mt_Results = "results";
        final String mt_id = "id";
        final String mt_iso_639_1 = "iso_639_1";
        final String mt_iso_3166_1 = "iso_3166_1";
        final String mt_key = "key";
        final String mt_name = "name";
        final String mt_site = "site";
        final String mt_type = "type";

        ArrayList<MovieTrailers> movieTrailers = new ArrayList<>();
        JSONArray results = new JSONObject(json).getJSONArray(mt_Results);


        for (int i = 0; i < results.length(); i++) {
            JSONObject result_movieTrailers = results.getJSONObject(i);
            MovieTrailers movieTrailers1 = new MovieTrailers();

            movieTrailers1.setId(result_movieTrailers.getString(mt_id));
            movieTrailers1.setIso_639_1(result_movieTrailers.getString(mt_iso_639_1));
            movieTrailers1.setIso_3166_1(result_movieTrailers.getString(mt_iso_3166_1));
            movieTrailers1.setKey(result_movieTrailers.getString(mt_key));
            movieTrailers1.setName(result_movieTrailers.getString(mt_name));
            movieTrailers1.setSite(result_movieTrailers.getString(mt_site));
            movieTrailers1.setType(result_movieTrailers.getString(mt_type));
            movieTrailers.add(movieTrailers1);
        }
        return movieTrailers;
    }
}


