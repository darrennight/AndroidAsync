package zenghao.com.androidasynchttp;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by zenghao on 15/11/25.
 */
public class MyApplication extends Application {

    private static MyApplication mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        Fresco.initialize(this);
    }

public static MyApplication getInstance(){
    return mInstance;
}
}
