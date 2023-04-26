package com.lmj.opencar;

import android.app.Application;
import android.util.Log;

import com.kakao.sdk.common.KakaoSdk;

public class GlobalApplication extends Application {

    private  static GlobalApplication instance;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        Log.d("Test",Join_MenuActivity.APP_KEY);
        KakaoSdk.init(this,Join_MenuActivity.APP_KEY);
    }
}
