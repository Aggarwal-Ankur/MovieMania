package com.ankuraggarwal.moviemania.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Ankur on 18-Jul-16.
 */

public class FavoritesContract {
    //Create the content Authority
    public static final String AUTHORITY = "com.aggarwalankur.moviemania.provider";

    //Create the content URI
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);


    public static final class FavoriteMovies implements BaseColumns{

        public static final Uri CONTENT_URI =  Uri.withAppendedPath(FavoritesContract.CONTENT_URI, "movies");

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + AUTHORITY + "/" + "favorite_movie";
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + AUTHORITY + "/" + "favorite_movie";

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_IMAGE = "image";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
    }

}
