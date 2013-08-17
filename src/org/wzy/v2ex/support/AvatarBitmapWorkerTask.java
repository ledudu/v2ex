package org.wzy.v2ex.support;

import java.lang.ref.WeakReference;
import java.util.Map;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.LruCache;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import org.wzy.v2ex.R;


public class AvatarBitmapWorkerTask extends MyAsyncTask<String, Void, Bitmap> {
	private LruCache<String, Bitmap> lruCache;
	private String url = "";
	private final WeakReference<ImageView> view;
	private Map<String, AvatarBitmapWorkerTask> taskMap;
	private int position;
	
	private Activity activity;
	
	public String getUrl() {
		return url;
	}
	
	public AvatarBitmapWorkerTask(LruCache<String, Bitmap> lruCache,
									Map<String, AvatarBitmapWorkerTask> taskMap,
									ImageView view, String url, int position, Activity activity) {
		this.lruCache = lruCache;
		this.url = url;
		this.taskMap = taskMap;
		this.view = new WeakReference<ImageView>(view);
		this.position = position;
		this.activity = activity;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (view != null) {
			ImageView imageView = view.get();
			if (imageView != null) {
				imageView.setTag(url);
			}
		}
	}
	
	@Override
	protected Bitmap doInBackground(String... url) {
		if (!isCancelled()) {
			return ImageTool.getSmallAvatarWithRoundedCorner(this.url);	
		}
		return null;
	}
	
	@Override
	protected void onCancelled(Bitmap bitmap) {
		if (taskMap != null && taskMap.get(url) != null) {
			taskMap.remove(url);
		}
		super.onCancelled(bitmap);
	}
	
	@Override
	protected void onPostExecute(Bitmap bitmap) {
		if (bitmap != null) {
			if (view != null && view.get() != null) {
				ImageView imageView = view.get();
				AvatarBitmapWorkerTask avatarBitmapWorkerTask = getAvatarBitmapWorkerTask(imageView);
				if (this == avatarBitmapWorkerTask) {
					//AppLogger.i("put to lrucache, url:" + FileManager.getFileFromUrl(this.url, FileLocationMethod.avatar_small) + ", position:" + position);
					playImageViewAninmation(view.get(), bitmap);
					lruCache.put(url, bitmap);
				}
			}
		}
		
		if (taskMap != null && taskMap.get(url) != null) {
			taskMap.remove(url);
		}
	}
	
	private AvatarBitmapWorkerTask getAvatarBitmapWorkerTask(ImageView imageView) {
		if (imageView != null) {
			Drawable drawable = imageView.getDrawable();
			if (drawable instanceof AvatarBitmapDrawable) {
				return ((AvatarBitmapDrawable)drawable).getBitmapDownloaderTask();
			}
		}
		return null;
	}
	
	private void playImageViewAninmation(final ImageView view, final Bitmap bitmap) {
		final Animation anim_out = AnimationUtils.loadAnimation(activity, R.anim.timeline_pic_fade_out);
		final Animation anim_in = AnimationUtils.loadAnimation(activity, R.anim.timeline_pic_fade_in);
		
		anim_out.setAnimationListener(new Animation.AnimationListener() {
			
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAnimationEnd(Animation arg0) {
				view.setImageBitmap(bitmap);
				anim_in.setAnimationListener(new Animation.AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						
					}
				});
				view.startAnimation(anim_in);
			}
		});
		
		view.startAnimation(anim_out);
	}
}
