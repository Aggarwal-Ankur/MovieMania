package com.ankuraggarwal.moviemania.modules;

/**
 * Created by Ankur on 1/2/2017.
 */

import android.content.Context;

import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Singleton
@Module(includes = ContextModule.class)
public class PicassoModule {
    @Provides
    public Picasso providePicasso(Context context) {
        return new Picasso.Builder(context)
                .build();
    }
}
