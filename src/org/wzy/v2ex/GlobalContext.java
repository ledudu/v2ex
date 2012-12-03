package org.wzy.v2ex;

import android.app.ActivityManager;
import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;


public class GlobalContext extends Application {
	private static GlobalContext globalContext = null;
	private LruCache<String, Bitmap> avatarCache = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;
		createAvatarCache();
	}
	
	public static GlobalContext getInstance() {
		return globalContext;
	}
	
	public LruCache<String, Bitmap> getAvatarCache() {
		if (avatarCache == null) {
			createAvatarCache();
		}
		return avatarCache;
	}
	
	private void createAvatarCache() {
		int memSize = ((ActivityManager) getSystemService(
				ACTIVITY_SERVICE)).getMemoryClass();
		int cacheSize = 1024 * 1024 * memSize / 5;
		
		avatarCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap value) {
				return value.getByteCount();
			}
		};
	}
}
