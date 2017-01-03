package com.ankuraggarwal.moviemania.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by Ankur on 12/29/2016.
 */

@Singleton
@Module
public class ContextModule {
    private final Context mContext;


    public ContextModule(Context context) {
        this.mContext = context;
    }

    @Provides
    public Context provideContext(){
        return mContext;
    }
}
