package org.wzy.v2ex;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;

public class TabListener<T extends Fragment> implements ActionBar.TabListener {
	private Fragment fragment;
	private final Activity activity;
	private final String tag;
	private final Class<T> clz;
	
	public TabListener(Activity activity, String tag, Class<T> clz) {
		this.activity = activity;
		this.tag = tag;
		this.clz = clz;
	}
	
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
		if (fragment == null) {
			fragment = Fragment.instantiate(activity, clz.getName());
			ft.add(android.R.id.content, fragment, tag);
		} else {
			ft.attach(fragment);
		}
	}
	
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
		
		if (fragment != null) {
			ft.detach(fragment);
		}
	}
	
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
		
	}
}