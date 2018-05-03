package com.example.kh870h.moviediscovery.SaveMovieData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.*;

import static com.example.kh870h.moviediscovery.SaveMovieData.FavoriteMoviesContract.FavoriteMoviesEntry.FAVORITE_MOVIE_DB_TABLE_NAME;

/**
 * Created by kavin on 1/28/2018.
 */

public class FavoriteMoviesDBHelper extends SQLiteOpenHelper {

    //name of the database
    public static final String MOVIES_DATABASE = "movieFavoriteDB.db";

    //database version
    private static final int DATABASE_VERSION_NUMBERS = 3;


    public FavoriteMoviesDBHelper(Context context) {
        super(context, MOVIES_DATABASE, null, DATABASE_VERSION_NUMBERS);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CRETE_MOVIES_FAVORITE =
                "CREATE TABLE " + FAVORITE_MOVIE_DB_TABLE_NAME + " (" +

                        FavoriteMoviesEntry.KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        FavoriteMoviesEntry.KEY_TITLE + " TEXT NOT NULL, " +
                        FavoriteMoviesEntry.KEY_RELEASE + " INTEGER NOT NULL, " +
                        FavoriteMoviesEntry.KEY_AVERAGE + " INTEGER NOT NULL, " +
                        FavoriteMoviesEntry.KEY_OVERVIEW + " TEXT NOT NULL, " +
                        FavoriteMoviesEntry.KEY_POSTER_PATH + " TEXT NOT NULL, " +
                        " UNIQUE (" + FavoriteMoviesEntry.KEY_OVERVIEW + ") ON CONFLICT REPLACE);";

        //execute the query by calling execSQL
        db.execSQL(SQL_CRETE_MOVIES_FAVORITE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FAVORITE_MOVIE_DB_TABLE_NAME);
        onCreate(db);
    }
}
