package com.ankuraggarwal.moviemania.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ankur on 19-Jul-16.
 */

public class FavoritesDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;

    static final String DB_NAME = "favorites.db";


    public FavoritesDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String CREATE_TABLE = "CREATE TABLE " + FavoritesContract.FavoriteMovies.TABLE_NAME + " (" +
                FavoritesContract.FavoriteMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavoritesContract.FavoriteMovies.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_TITLE + " TEXT NOT NULL, " +
                FavoritesContract.FavoriteMovies.COLUMN_IMAGE + " TEXT, " +
                FavoritesContract.FavoriteMovies.COLUMN_DESCRIPTION + " TEXT, " +
                FavoritesContract.FavoriteMovies.COLUMN_RATING + " INTEGER, " +
                FavoritesContract.FavoriteMovies.COLUMN_RELEASE_DATE + " TEXT);";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoritesContract.FavoriteMovies.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
