package org.wzy.v2ex;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.wzy.v2exbean.MessageBean;

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
			Log.i("wzy", "getCount:" + mMessages.size());
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
		Log.i("wzy", "getView: positon:" + position + ", convertView:"
				+ convertView + ", parent:" + parent);
		if (convertView == null) {
			convertView = inflater.inflate(
					R.layout.listview_item_latest_layout, parent, false);
		}

		ImageView avatar = (ImageView) convertView.findViewById(R.id.avatar);
		//avatar.setBackgroundResource(R.drawable.avatar);
		if (!((LatestFragment)mFragment).isListViewFling()) {
			Log.i("wzy1", "setImageDrawable");
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
			/*Date date = new Date(Long.parseLong(messageTime) * 1000);
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			//String createTime = calendar.getDisplayName(Calendar.MINUTE,
			//		Calendar.SHORT, Locale.CHINA);
			//time.setText("12:34");
			StringBuilder sb = new StringBuilder();
			sb.append(calendar.get(Calendar.YEAR));
			sb.append(" ");
			sb.append(calendar.get(Calendar.MONTH));
			sb.append(" ");
			sb.append(calendar.get(Calendar.DAY_OF_MONTH));
			sb.append(" ");
			appendTwoDigits(sb, calendar.get(Calendar.HOUR_OF_DAY));
			sb.append(":");
			appendTwoDigits(sb, calendar.get(Calendar.MINUTE));
			sb.append(":");
			appendTwoDigits(sb, calendar.get(Calendar.SECOND));
			
			time.setText(sb.toString());*/
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
		Log.i("wzy", "delta:" + delta + ", month:" + MONTH + ", n:" + delta/MONTH);
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
	
	private void appendTwoDigits(StringBuilder sb, int n) {
		if (n < 10) {
			sb.append('0');
		}
		sb.append(n);
	}
	
	public static class ViewHolder {
		ImageView avatar;
		TextView username;
		TextView content;
		TextView time;
	}

}
