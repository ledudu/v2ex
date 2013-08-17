package org.wzy.v2ex.ui.fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.wzy.v2ex.R;
import org.wzy.v2ex.adapter.MessageListAdapter;
import org.wzy.v2ex.bean.MessageBean;
import org.wzy.v2ex.ui.activity.MyActivity;
import org.wzy.v2ex.ui.activity.TopicActivity;
import org.wzy.v2ex.utils.BitmapCache;
import org.wzy.v2ex.utils.URL;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-7
 * Time: 下午11:35
 */
public class ContentFragment extends Fragment implements AdapterView.OnItemClickListener,
    PullToRefreshAttacher.OnRefreshListener{

    private String mUrl;
    private RequestQueue mRequestQueue;
    private BitmapCache mImageCache;
    private Context mContext;
    private List<MessageBean> mMessageBeanList;
    private MessageListAdapter mAdapter;
    private ListView mListView;
    private PullToRefreshAttacher mPullToRefreshAttacher;

    public ContentFragment() {
        super();
    }

    public ContentFragment(Context context) {
        super();
        mContext = context;
        mMessageBeanList = new ArrayList<MessageBean>();
        mRequestQueue = Volley.newRequestQueue(context);
        mImageCache = BitmapCache.getBitmapCache();
        Log.d("ContentFragment", mRequestQueue.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        if (TextUtils.equals(url, "latest")) {
            mUrl = URL.LATEST_URL;
        } else if (!TextUtils.isEmpty(url)){
            mUrl = String.format(URL.NODE_URL, url);
        } else {
            mUrl = "";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content, container, false);
        mListView = (ListView) view.findViewById(R.id.listview);
        mAdapter = new MessageListAdapter(mContext,
                mMessageBeanList,
                mRequestQueue,
                mImageCache,
                TextUtils.equals(mUrl, URL.LATEST_URL));
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
        Log.d("ContentFragment", mUrl);
        mPullToRefreshAttacher = ((MyActivity) getActivity()).getmPullToRefreshAttacher();
        mPullToRefreshAttacher.addRefreshableView(mListView, this);

        restGetContent(mUrl);

        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String topicId = mMessageBeanList.get(position).getId();
        Intent intent = new Intent(this.getActivity(), TopicActivity.class);
        intent.putExtra("topic_id", topicId);
        startActivity(intent);
    }

    @Override
    public void onRefreshStarted(View view) {
        restGetContent(mUrl);
    }

    private void restGetContent(String url) {
        if (TextUtils.isEmpty(url)) {
            return;
        }

        if (!mPullToRefreshAttacher.isRefreshing()) {
            mPullToRefreshAttacher.setRefreshing(true);
        }
        if (mRequestQueue != null) {
              mRequestQueue.add(new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
                  @Override
                  public void onResponse(org.json.JSONArray response) {
                      try {
                          Type type = new TypeToken<ArrayList<MessageBean>>() {}.getType();
                          mMessageBeanList.clear();
                          mMessageBeanList.addAll((ArrayList<MessageBean>) new Gson().fromJson(response.toString(), type));
                          Log.d("ContentFragment","size:" + mMessageBeanList.size());
                          mAdapter.notifyDataSetChanged();
                          mPullToRefreshAttacher.setRefreshComplete();
                      } catch (JsonSyntaxException e) {
                          e.printStackTrace();
                      } catch (JsonIOException e) {
                          e.printStackTrace();
                      }
                  }
              }, new Response.ErrorListener() {
                  @Override
                  public void onErrorResponse(VolleyError error) {
                      mPullToRefreshAttacher.setRefreshComplete();
                      error.printStackTrace();
                      if (TextUtils.isEmpty(error.getMessage())) {
                          Toast.makeText(mContext, "未知错误", Toast.LENGTH_LONG).show();
                      } else {
                        Toast.makeText(mContext, error.getMessage(), Toast.LENGTH_SHORT).show();
                      }
                  }
              }));
        }
    }


}
