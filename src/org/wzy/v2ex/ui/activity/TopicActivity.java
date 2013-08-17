package org.wzy.v2ex.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.wzy.v2ex.R;
import org.wzy.v2ex.adapter.TopicListAdapter;
import org.wzy.v2ex.bean.ReplyBean;
import org.wzy.v2ex.bean.TopicBean;
import org.wzy.v2ex.utils.BitmapCache;
import org.wzy.v2ex.utils.URL;
import org.wzy.v2ex.utils.Utils;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-10
 * Time: 下午12:31
 * To change this template use File | Settings | File Templates.
 */
public class TopicActivity extends Activity {
    private RequestQueue mRequestQueue;
    private ListView mListView;
    private List<TopicBean> mTopicList;
    private List<ReplyBean> mReplyList;
    private TopicListAdapter mAdapter;
    private View mHeaderView;
    private FrameLayout mProgressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        mListView = (ListView) findViewById(R.id.content);
        mProgressBarLayout = (FrameLayout) findViewById(R.id.progressbar_layout);
        mHeaderView = getLayoutInflater().inflate(R.layout.list_topic_header, mListView, false);
        mListView.addHeaderView(mHeaderView);

        mTopicList = new ArrayList<TopicBean>();
        mReplyList = new ArrayList<ReplyBean>();
        mRequestQueue = Volley.newRequestQueue(this);

        mAdapter = new TopicListAdapter(this, mReplyList, mRequestQueue, BitmapCache.getBitmapCache());
        mListView.setAdapter(mAdapter);

        Intent intent = getIntent();
        final String topicId = intent.getStringExtra("topic_id");
        final String topicUrl = String.format(URL.TOPIC_URL, Integer.parseInt(topicId));
        final String replyUrl = String.format(URL.REPLY_URL, Integer.parseInt(topicId));
        showProgressBar(true);
        getTopicContent(topicUrl);
        getReplyContent(replyUrl);
    }

    private void getTopicContent(String url) {
        JsonArrayRequest topicRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type type = new TypeToken<ArrayList<TopicBean>>() {}.getType();
                mTopicList.clear();
                mTopicList.addAll((ArrayList<TopicBean>) new Gson().fromJson(response.toString(), type));
                for (TopicBean bean : mTopicList)
                    Log.d("v2ex", "topic:" + bean);
                TopicBean topicBean = mTopicList.get(0);
                NetworkImageView imageView = (NetworkImageView) mHeaderView.findViewById(R.id.avatar);

                TextView title = (TextView) mHeaderView.findViewById(R.id.topic_title);
                TextView content = (TextView) mHeaderView.findViewById(R.id.topic_content);
                TextView author_name = (TextView) mHeaderView.findViewById(R.id.author_name);
                TextView time = (TextView) mHeaderView.findViewById(R.id.time);

                imageView.setDefaultImageResId(android.R.drawable.dialog_frame);
                imageView.setErrorImageResId(android.R.drawable.alert_light_frame);
                imageView.setImageUrl(topicBean.member.getAvatarNormal(), new ImageLoader(mRequestQueue, BitmapCache.getBitmapCache()));
                title.setText(topicBean.title);
                content.setText(topicBean.content);
                author_name.setText(topicBean.member.getUserName());
                time.setText(Utils.getTime(Long.parseLong(topicBean.created)));
                showProgressBar(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TopicActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                showProgressBar(false);
            }
        });
        mRequestQueue.add(topicRequest);
    }

    private void getReplyContent(String url) {
        JsonArrayRequest replyRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Type type = new TypeToken<ArrayList<ReplyBean>>() {}.getType();
                mReplyList.clear();
                mReplyList.addAll((ArrayList<ReplyBean>) new Gson().fromJson(response.toString(), type));
                for (ReplyBean bean : mReplyList)
                    Log.d("v2ex", "reply:" + bean);
                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(TopicActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mRequestQueue.add(replyRequest);
    }

    private void showProgressBar(boolean show) {
        mProgressBarLayout.setVisibility(show ? View.VISIBLE : View.GONE);
        mListView.setVisibility(show ? View.GONE : View.VISIBLE);
    }

}
