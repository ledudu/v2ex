package org.wzy.v2ex.utils;

import android.graphics.Bitmap;
import android.util.LruCache;
import com.android.volley.toolbox.ImageLoader;

/**
 * Created with IntelliJ IDEA.
 * User: zeyiwu
 * Date: 13-8-8
 * Time: 下午3:01
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private static BitmapCache mBitmapCache;
    private LruCache<Integer, Bitmap> mCache;

    private BitmapCache() {
        int maxSize = 10 * 1024 * 1024;
        mCache = new LruCache<Integer, Bitmap>(maxSize) {
            @Override
            protected int sizeOf(Integer key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };
    }

    public static BitmapCache getBitmapCache() {
        if (mBitmapCache == null) {
            mBitmapCache = new BitmapCache();
        }
        return mBitmapCache;
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url.hashCode());
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        int hashCode = url.hashCode();
        mCache.put(hashCode, bitmap);
    }
}
