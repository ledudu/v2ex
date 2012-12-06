package org.wzy.v2ex.interfaces;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.wzy.v2ex.GlobalContext;
import org.wzy.v2ex.support.AvatarBitmapDrawable;
import org.wzy.v2ex.support.AvatarBitmapWorkerTask;
import org.wzy.v2ex.support.MyAsyncTask;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.widget.ImageView;

public class AbstractActivity extends FragmentActivity {
    private Drawable transPic = new ColorDrawable(Color.TRANSPARENT);
    private Map<String, AvatarBitmapWorkerTask> avatarBitmapWorkerTaskHashMap = new ConcurrentHashMap<String, AvatarBitmapWorkerTask>();
    protected ICommander commander = new PicCommander();
    
    public ICommander getCommander() {
    	return commander;
    }
    
    private class PicCommander implements ICommander {
    	@Override
    	public void downloadAvatar(ImageView view, String urlKey, int position, boolean isFling) {
    		Bitmap bitmap = getBitmapFromCache(urlKey);
    		if (bitmap != null) {
    			if (!isSameUrl(urlKey, view)) {
    				view.setImageBitmap(bitmap);
    				view.setTag(urlKey);
    			}
    			cancelPotentialAvatarDownload(urlKey, view);
    			avatarBitmapWorkerTaskHashMap.remove(urlKey);
    		} else {
    			//AppLogger.i("download avatar, position:" + position);
    			view.setImageDrawable(transPic);
    			view.setTag("");
    			if (cancelPotentialAvatarDownload(urlKey, view) && !isFling) {
    				AvatarBitmapWorkerTask task = new AvatarBitmapWorkerTask(GlobalContext.getInstance().getAvatarCache(),
    						avatarBitmapWorkerTaskHashMap, view, urlKey, position, AbstractActivity.this);
    				AvatarBitmapDrawable downloadedDrawable = new AvatarBitmapDrawable(task);
    				view.setImageDrawable(downloadedDrawable);
    				task.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
    				avatarBitmapWorkerTaskHashMap.put(urlKey, task);
    			}
    		}
    		
    	}
    }
    
    private boolean isSameUrl(String url, ImageView imageView) {
    	String bitmapUrl = (String) imageView.getTag();
    	if ((!TextUtils.isEmpty(bitmapUrl)) && bitmapUrl.equals(url)) {
    		return true;
    	}
    	return false;
    }
    
    protected Bitmap getBitmapFromCache(String key) {
    	return GlobalContext.getInstance().getAvatarCache().get(key);
    }
    
    private boolean cancelPotentialAvatarDownload(String urlKey, ImageView imageView) {
    	if (avatarBitmapWorkerTaskHashMap != null) {
    		AvatarBitmapWorkerTask task = avatarBitmapWorkerTaskHashMap.get(urlKey);
    		if (task != null && (task.getStatus() == MyAsyncTask.Status.PENDING ||
    				task.getStatus() == MyAsyncTask.Status.RUNNING)) {
    			//AppLogger.i("there is task in map, url:" + urlKey);
    			return false;
    		}
    	}
    	AvatarBitmapWorkerTask avatarBitmapWorkerTask = getAvatarBitmapDownloaderTask(imageView);
    	if (avatarBitmapWorkerTask != null) {
    		String bitmapUrl = avatarBitmapWorkerTask.getUrl();
    		if (bitmapUrl == null || !(bitmapUrl.equals(urlKey))) {
    			avatarBitmapWorkerTask.cancel(true);    			
    		} else if (avatarBitmapWorkerTask.getStatus() == MyAsyncTask.Status.PENDING ||
    				avatarBitmapWorkerTask.getStatus() == MyAsyncTask.Status.RUNNING) {
    			//AppLogger.i("the image has the same task, url:" + urlKey);
    			return false;
    		}
    	}
    	return true;
    }
    
    private AvatarBitmapWorkerTask getAvatarBitmapDownloaderTask(ImageView imageView) {
    	if (imageView != null) {
    		Drawable drawable = imageView.getDrawable();
    		if (drawable instanceof AvatarBitmapDrawable) {
    			return ((AvatarBitmapDrawable)drawable).getBitmapDownloaderTask();
    		}
    	}
    	return null;
    }
}
