package com.example.kh870h.moviediscovery.SaveMovieData;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kavin on 1/29/2018.
 */

public class FavoriteMoviesContract {
    //using authority so the code knows which Content Provider to access
    public static final String AUTHORITY = "com.example.kh870h.moviediscovery";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_FAVORITE_MOVIE_DB = "results";

    public static final class FavoriteMoviesEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIE_DB).build();

        //tables name and table column names
        public static final String FAVORITE_MOVIE_DB_TABLE_NAME = "results";
        public static final String KEY_ID = "id";
        public static final String KEY_TITLE = "title";
        public static final String KEY_RELEASE = "release_date";
        public static final String KEY_AVERAGE = "vote_average";
        public static final String KEY_OVERVIEW = "overview";
        public static final String KEY_POSTER_PATH = "poster_path";

    }
}
