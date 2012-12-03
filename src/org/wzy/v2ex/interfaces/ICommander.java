package org.wzy.v2ex.interfaces;

import android.widget.ImageView;
import android.widget.ListView;

public interface ICommander {
	public void downloadAvatar(ImageView view, String url, int position, boolean isFling);
}
