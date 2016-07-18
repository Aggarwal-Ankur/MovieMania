package com.ankuraggarwal.moviemania.provider;

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

    }

}
