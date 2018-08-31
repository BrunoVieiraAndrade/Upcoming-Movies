package com.arctouch.codechallenge.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.base.BaseActivity;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.ui.home.HomeActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
    }
}
