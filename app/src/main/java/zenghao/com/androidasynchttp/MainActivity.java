package zenghao.com.androidasynchttp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.loopj.android.http.AsyncHttpClient;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AsyncHttpClient client = new AsyncHttpClient();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
