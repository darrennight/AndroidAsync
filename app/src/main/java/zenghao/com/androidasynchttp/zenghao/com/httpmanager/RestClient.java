package zenghao.com.androidasynchttp.zenghao.com.httpmanager;

import android.content.Context;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zenghao on 15/11/26.
 */
public class RestClient {

    private static AsyncHttpClient client = new AsyncHttpClient();

    public void login(Context context ,String name,String pwd,AsyncHttpResponseHandler responseHandler){
        client.get(context,"url",responseHandler);

        Map<String, String> map = new HashMap<String, String>();
        map.put("mkey", "mobile010Tv101Log212Info");
        map.put("ptype", "music");
        map.put("fid", "fid");
        RequestParams param = new RequestParams(map);

        client.post(context,"url",param,responseHandler);
    }

}
