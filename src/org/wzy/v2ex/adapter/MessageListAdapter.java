package org.wzy.v2ex.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.wzy.v2ex.R;
import org.wzy.v2ex.bean.MessageBean;
import org.wzy.v2ex.utils.Utils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-10
 * Time: 上午10:37
 * To change this template use File | Settings | File Templates.
 */
public class MessageListAdapter extends BaseAdapter {

    private List<MessageBean> mMessageList;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private ImageLoader.ImageCache mImageCache;
    private boolean mIsHotest;

    public MessageListAdapter(Context context, List<MessageBean> messageBeanList,
                              RequestQueue requestQueue, ImageLoader.ImageCache imageCache,
                              boolean isHotest) {
        mContext = context;
        mMessageList = messageBeanList;
        mRequestQueue = requestQueue;
        mImageCache = imageCache;
        mIsHotest = isHotest;
    }

    @Override
    public int getCount() {
        return mMessageList.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        return super.getItemViewType(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.list_message_item, parent, false);
            holder = new ViewHolder();
            holder.imageView = (NetworkImageView) convertView.findViewById(R.id.avatar);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            holder.replyCount = (TextView) convertView.findViewById(R.id.reply_count);
            holder.author = (TextView) convertView.findViewById(R.id.author);
            holder.node = (TextView) convertView.findViewById(R.id.node);
            holder.time =  (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (holder != null) {
            holder.imageView.setDefaultImageResId(android.R.drawable.dialog_frame);
            holder.imageView.setErrorImageResId(android.R.drawable.alert_light_frame);
            holder.imageView.setImageUrl(mMessageList.get(position).getMember().getAvatarNormal(),
                    new ImageLoader(mRequestQueue, mImageCache));

            holder.title.setText(mMessageList.get(position).getTitle());
            holder.replyCount.setText(mMessageList.get(position).getReplies());
            holder.time.setText(Utils.getTime(Long.parseLong(mMessageList.get(position).getCreated())));
            holder.author.setText(mMessageList.get(position).getMember().getUserName());
            if (mIsHotest)
                holder.node.setText(mMessageList.get(position).getNode().getTitle());
        }
        return convertView;
    }

    private class ViewHolder {
        NetworkImageView imageView;
        TextView title;
        TextView replyCount;
        TextView author;
        TextView node;
        TextView time;
    }
}
