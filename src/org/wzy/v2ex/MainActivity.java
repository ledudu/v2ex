package org.wzy.v2ex;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.wzy.v2ex.interfaces.ICommander;
import org.wzy.v2ex.support.AvatarBitmapDrawable;
import org.wzy.v2ex.support.AvatarBitmapWorkerTask;
import org.wzy.v2ex.support.MyAsyncTask;
import org.wzy.v2ex.timeline.LatestTimeLine;
import org.wzy.v2ex.utils.AppLogger;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends FragmentActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * sections. We use a {@link android.support.v4.app.FragmentPagerAdapter} derivative, which will
     * keep every loaded fragment in memory. If this becomes too memory intensive, it may be best
     * to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_v2ex);
        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
     }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_v2ex, menu);
        return true;
    }
    
    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        	Fragment fragment = null;
        	switch (i) {
        	case 0:
        		fragment = new LatestTimeLine();
        		break;
        	case 1:
        	case 2:
        		fragment = new DummySectionFragment();
        		Bundle args = new Bundle();
                args.putInt(LatestTimeLine.ARG_SECTION_NUMBER, i + 1);
                fragment.setArguments(args);
                break;
        	}
        	return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: return getString(R.string.title_section1).toUpperCase();
                case 1: return getString(R.string.title_section2).toUpperCase();
                case 2: return getString(R.string.title_section3).toUpperCase();
            }
            return null;
        }
    }

    /**
     * A dummy fragment representing a section of the app, but that simply displays dummy text.
     */
    public static class DummySectionFragment extends Fragment {
        public DummySectionFragment() {
        }

        public static final String ARG_SECTION_NUMBER = "section_number";

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            TextView textView = new TextView(getActivity());
            textView.setGravity(Gravity.CENTER);
            Bundle args = getArguments();
            textView.setText(Integer.toString(args.getInt(ARG_SECTION_NUMBER)));
            return textView;
        }
    }
    
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
    						avatarBitmapWorkerTaskHashMap, view, urlKey, position, MainActivity.this);
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
