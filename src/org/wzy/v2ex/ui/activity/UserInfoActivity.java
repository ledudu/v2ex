package org.wzy.v2ex.ui.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.wzy.v2ex.R;
import org.wzy.v2ex.bean.UserBean;
import org.wzy.v2ex.utils.AppLogger;
import org.wzy.v2ex.utils.BitmapCache;
import org.wzy.v2ex.utils.URL;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-26
 * Time: 下午10:23
 */
public class UserInfoActivity extends Activity {

    private RequestQueue requestQueue;
    private NetworkImageView avatarImage;
    private TextView userName;
    private TextView tagLine;
    private TextView location;
    private TextView bio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);

        avatarImage = (NetworkImageView) findViewById(R.id.avatar);
        userName = (TextView) findViewById(R.id.user_name);
        tagLine = (TextView) findViewById(R.id.tagline);
        location = (TextView) findViewById(R.id.location);
        bio = (TextView) findViewById(R.id.bio);

        requestQueue = Volley.newRequestQueue(this);
        getUser(getIntent().getStringExtra("username"));
    }

    private void getUser(String userName) {
        String url = String.format(URL.USER_INFO_URL, userName);
        AppLogger.d("getUser, url:" + url);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject result) {
                AppLogger.d("getUser, json:" + result.toString());
                UserBean userBean = new Gson().fromJson(result.toString(), UserBean.class);
                setUser(userBean);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(UserInfoActivity.this, volleyError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private void setUser(UserBean userBean) {
        avatarImage.setDefaultImageResId(android.R.drawable.dialog_frame);
        avatarImage.setErrorImageResId(android.R.drawable.alert_light_frame);
        avatarImage.setImageUrl(userBean.avatar_large, new ImageLoader(requestQueue, BitmapCache.getBitmapCache()));
        userName.setText(userBean.username);
        tagLine.setText(userBean.tagline);
        location.setText(userBean.location);
        bio.setText(userBean.bio);
    }
}
