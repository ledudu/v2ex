package org.wzy.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.wzy.bean.MessageBean;
import org.wzy.v2ex.R;
import org.wzy.v2ex.R.drawable;
import org.wzy.v2ex.R.id;
import org.wzy.v2ex.R.layout;
import org.wzy.v2ex.R.string;

import timeline.LatestTimeLineFragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LatestTopicsDataAdapter extends BaseAdapter {
	
	private ArrayList<MessageBean> mMessages;
	protected LayoutInflater inflater;
	private Fragment mFragment;
	
	public LatestTopicsDataAdapter(Fragment fragment, ArrayList<MessageBean> messages) {
		mMessages = messages;
		inflater = fragment.getActivity().getLayoutInflater();
		mFragment = fragment;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if (mMessages != null) {			
			return mMessages.size();			
		}
		return 0;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub		
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.listview_item_latest_layout, parent, false);
		}

		ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
		//avatar.setBackgroundResource(R.drawable.avatar);
		if (!((LatestTimeLineFragment)mFragment).isListViewFling()) {			
			avatar.setImageDrawable(parent.getResources().getDrawable(R.drawable.ic_launcher));
		}
		TextView username = (TextView) convertView.findViewById(R.id.username);
		if (mMessages != null) {
			String name = mMessages.get(position).getMember().getUserName();			
			username.setText(name);
		}
		
		TextView titleView = (TextView) convertView.findViewById(R.id.title);
		if (mMessages != null) {
			String title = mMessages.get(position).getTitle();			
			titleView.setText(title);
		}
		
		TextView content = (TextView) convertView.findViewById(R.id.node);
		if (mMessages != null) {
			String messageContent = mMessages.get(position).getNode().getName();			
			content.setText(messageContent);
		}
	
		TextView time = (TextView) convertView.findViewById(R.id.time);
		if (mMessages != null) {
			String messageTime = mMessages.get(position).getCreated();
			time.setText(formatString(parent.getContext(), Long.parseLong(messageTime)));
		}

		return convertView;
	}
	
	private final long YEAR = 365 * 24 * 60 * 60;
	private final long MONTH = 30 * 24 * 60 * 60;
	private final long DAY = 24 * 60 * 60;
	private final long HOUR = 60 * 60;
	private final long MINUTE = 60;
	private final long SECOND = 1;
	
	private String formatString(Context context, final long time) {
		String formatTime;		
		long delta = System.currentTimeMillis() / 1000 - time;		
		if (delta > YEAR) {
			formatTime = String.format(context.getString(R.string.year), delta/YEAR);
		} else if (delta > MONTH) {
			formatTime = String.format(context.getString(R.string.month), delta/MONTH);
		} else if (delta > DAY) {
			formatTime = String.format(context.getString(R.string.day), delta/DAY);
		} else if (delta > HOUR) {
			formatTime = String.format(context.getString(R.string.hour), delta/HOUR);
		} else if (delta > MINUTE) {
			formatTime = String.format(context.getString(R.string.minute), delta/MINUTE);
		} else if (delta >SECOND) {
			formatTime = String.format(context.getString(R.string.second), delta/SECOND);
		} else {
			formatTime = context.getString(R.string.current);
		}
		return formatTime;
	}
	
	public static class ViewHolder {
		ImageView avatar;
		TextView username;
		TextView content;
		TextView time;
	}

}
