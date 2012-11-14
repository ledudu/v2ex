package org.wzy.v2ex;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.wzy.http.HttpMethod;
import org.wzy.http.HttpUtility;
import org.wzy.support.MyAsyncTask;
import org.wzy.v2exbean.MessageBean;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

public class LatestFragment extends Fragment {
	
	LatestTopicsDataAdapter v2exAdapter;
	TimeLineGetNewMsgListTask newTask;
	ArrayList<MessageBean> mMessages = null;
	protected MenuItem refreshView;
	protected ImageView iv;
	private volatile boolean enableRefreshTime = true;
	
	public LatestFragment() {
    }
	
	public static final String ARG_SECTION_NUMBER = "section_number";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	mMessages = new ArrayList<MessageBean>();
    	LayoutInflater inflator = getActivity().getLayoutInflater();    	
    	iv = (ImageView)inflater.inflate(R.layout.refresh_action_view, null);
    	View view = inflator.inflate(R.layout.listview_layout, container, false);
    	ListView list = (ListView) view.findViewById(R.id.v2ex_listView);
    	
		
    	v2exAdapter = new LatestTopicsDataAdapter(this, mMessages);
    	
    	list.setAdapter(v2exAdapter);
    	
    	list.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {

				case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
					Log.i("wzy1", "SCROLL_STATE_IDLE");
					if (!enableRefreshTime) {
						Log.i("wzy1", "in SCROLL_STATE_IDLE");
						enableRefreshTime = true;
						getAdapter().notifyDataSetChanged();
					}

					break;

				case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
					Log.i("wzy1", "SCROLL_STATE_FLING");
					enableRefreshTime = false;
					break;

				case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
					Log.i("wzy1", "SCROLL_STATE_TOUCH_SCROLL");
					enableRefreshTime = true;
					break;

				}
			}
			
			@Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
		});
    	
    	newTask = new TimeLineGetNewMsgListTask();
    	newTask.execute();
    	
    	setHasOptionsMenu(true);
    	return view;
    }
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    	Log.i("wzy", "onCreateOptionsMenu");
    	super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.latest_fragment_menu, menu);
        refreshView = menu.findItem(R.id.menu_refresh);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			Log.i("wzy", "refresh");
			if (newTask == null || newTask.getStatus() == MyAsyncTask.Status.FINISHED) {
				newTask = new TimeLineGetNewMsgListTask();
				newTask.execute();
			}
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
    public TimeLineGetNewMsgListTask getTask() {
    	return newTask;
    }
    
    private ArrayList<MessageBean> parseJson(String json) {
    	ArrayList<MessageBean> messages = null;
    	try {
    		Gson gson = new Gson();    		 
    		Type type = new TypeToken<ArrayList<MessageBean>>() {}.getType();
    		messages = gson.fromJson(json, type);
    		if (mMessages != null && messages != null) {
    			mMessages.clear();
    			mMessages.addAll(messages);
    		}
    		for (MessageBean message : mMessages) {
    			Log.i("wzy", message.getTitle() + ", " + message.getMember().getUserName());
    		}
    	} catch (JsonSyntaxException e) {
    		Log.w("wzy", e.getMessage());
    	}
    	return mMessages;
    }

    private String loadJson() {
    	String url = "http://www.v2ex.com/api/topics/latest.json";
    	String json = HttpUtility.getInstance().executeNormalTask(HttpMethod.Get, url);
    	/*String json = null;
    	try {
    		InputStream in = getActivity().getAssets().open("latest.json");
    		int size = in.available();
    		
    		byte []buffer = new byte[size];
    		in.read(buffer);
    		in.close();
    		
    		json = new String(buffer);
    		Log.i("wzy", "loadJson:\n" + json);
    	} catch (IOException e) {
    		throw new RuntimeException(e);
    	}*/
    	return json;
    }
    
    protected void refresh() {
    	if (refreshView != null) {
	    	if (iv != null) {
	    		iv.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.refresh));
	    		refreshView.setActionView(iv);
	    	} else {
	    		Log.w("wzy", "ImageView parse error");
	    	}
    	}
    }
    
    protected void completeRefresh() {
    	if (refreshView != null && refreshView.getActionView() != null) {
    		refreshView.getActionView().clearAnimation();
    		refreshView.setActionView(null);
    	}
    }
    
    public class TimeLineGetNewMsgListTask extends MyAsyncTask<Object, ArrayList<MessageBean>, ArrayList<MessageBean>> {

        @Override
        protected void onPreExecute() {
        	refresh();
        }
        
    	 @Override
         protected ArrayList<MessageBean> doInBackground(Object... params) {
    		Log.i("wzy", "doInBackground");
    		parseJson(loadJson());    		
    		return mMessages;
         }
    	 
    	 @Override
    	 protected void onPostExecute(ArrayList<MessageBean> newvalue) {
    		 Log.i("wzy", "onPostExecute:" + newvalue.toString());
    		 v2exAdapter.notifyDataSetChanged();
    		 completeRefresh();
    	 }
    	 
    };
    
    private LatestTopicsDataAdapter getAdapter() {
    	return v2exAdapter;
    }
    
    public boolean isListViewFling() {
    	return !enableRefreshTime;
    }
    
}