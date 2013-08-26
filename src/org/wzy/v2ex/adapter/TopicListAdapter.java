package org.wzy.v2ex.adapter;

import android.content.Context;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import org.wzy.v2ex.R;
import org.wzy.v2ex.bean.ReplyBean;
import org.wzy.v2ex.utils.Utils;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-10
 * Time: 下午6:27
 * To change this template use File | Settings | File Templates.
 */
public class TopicListAdapter extends BaseAdapter{

    private List<ReplyBean> mReplyList;
    private RequestQueue mRequestQueue;
    private Context mContext;
    private ImageLoader.ImageCache mImageCache;

    public TopicListAdapter(Context context, List<ReplyBean> replyBeans,
                            RequestQueue requestQueue, ImageLoader.ImageCache imageCache) {
        mContext = context;
        mRequestQueue = requestQueue;
        mReplyList = replyBeans;
        mImageCache = imageCache;
    }

    @Override
    public int getCount() {
        return mReplyList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_topic_item, parent, false);
            hodler = new ViewHodler();
            hodler.avatar = (NetworkImageView) convertView.findViewById(R.id.avatar);
            hodler.content = (TextView) convertView.findViewById(R.id.content);
            hodler.author_name = (TextView) convertView.findViewById(R.id.author_name);
            hodler.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }

        if (hodler != null) {
            hodler.avatar.setDefaultImageResId(android.R.drawable.dialog_frame);
            hodler.avatar.setErrorImageResId(android.R.drawable.alert_light_frame);
            hodler.avatar.setImageUrl(mReplyList.get(position).member.getAvatarNormal(),
                    new ImageLoader(mRequestQueue, mImageCache));
            hodler.content.setText(mReplyList.get(position).content);
            hodler.author_name.setText(mReplyList.get(position).member.getUserName());
            hodler.time.setText(Utils.getTime(Long.parseLong(mReplyList.get(position).created)));

            setLinkText(hodler.content);
        }
        return convertView;
    }

    private void setLinkText(TextView textView) {
        Linkify.addLinks(textView, Linkify.WEB_URLS);
    }

    private class ViewHodler {
        NetworkImageView avatar;
        TextView content;
        TextView author_name;
        TextView time;
    }
}
