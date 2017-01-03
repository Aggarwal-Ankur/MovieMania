package com.ankuraggarwal.moviemania;

import android.content.Context;

import com.ankuraggarwal.moviemania.modules.ContextModule;
import com.ankuraggarwal.moviemania.modules.PicassoModule;
import com.squareup.picasso.Picasso;

import dagger.Component;

/**
 * Created by Ankur on 12/29/2016.
 */

@Component(modules = {ContextModule.class, PicassoModule.class})
public interface MovieManiaApplicationComponent {
    void inject(Context context);

    Context provideContext();

    Picasso providePicasso();
}
