package org.wzy.v2ex.ui.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import org.wzy.v2ex.R;
import org.wzy.v2ex.ui.fragment.ContentFragment;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();

    private String[] mNodes;
    private String[] mNodeNames;
    private DrawerLayout mNodeSlide;
    private ListView mNodeList;
    private ActionBarDrawerToggle mNodeSlideToggler;
    private CharSequence mTitle;
    private CharSequence mSelectTitle;

    private PullToRefreshAttacher mPullToRefreshAttacher;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        //init pulltorefresh attacher
        PullToRefreshAttacher.Options options = new PullToRefreshAttacher.Options();
        options.headerInAnimation = R.anim.pulldown_fade_in;
        options.headerOutAnimation = R.anim.pulldown_fade_out;
        options.refreshScrollDistance = 0.3f;
        options.headerLayout = R.layout.actionbar_pull_refresh_header;
        mPullToRefreshAttacher = PullToRefreshAttacher.get(this, options);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mTitle = mSelectTitle = getTitle();

        mNodes = getResources().getStringArray(R.array.nodes);
        mNodeNames = getResources().getStringArray(R.array.nodes_name);
        mNodeSlide = (DrawerLayout) findViewById(R.id.sliding_node);
        mNodeList = (ListView) findViewById(R.id.list_node);

        mNodeList.setAdapter(new ArrayAdapter<String>(
                this,
                R.layout.list_node_item,
                mNodes));
        mNodeList.setOnItemClickListener(new NodeListClickListener());

        mNodeSlideToggler = new ActionBarDrawerToggle(
                this,
                mNodeSlide,
                R.drawable.ic_drawer,
                R.string.app_name,
                R.string.app_name
                ) {

            @Override
            public void onDrawerClosed(View drawerView) {
                getActionBar().setTitle(mSelectTitle);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle(mTitle);
            }
        };
        mNodeSlide.setDrawerListener(mNodeSlideToggler);
        if (savedInstanceState == null)
            selectItem(0);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mNodeSlideToggler.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mNodeSlideToggler.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mNodeSlideToggler.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mSelectTitle = title;
        getActionBar().setTitle(title);
    }

    private class NodeListClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ContentFragment(this);
        Bundle bundle = new Bundle();

        if (position < mNodeNames.length) {
            bundle.putString("url", mNodeNames[position]);
        } else {
            bundle.putString("url", "");
        }
        fragment.setArguments(bundle);

        fragmentManager.beginTransaction()
                       .replace(R.id.content_frame, fragment)
                       .commit();
        Log.d(TAG, "selectItem position:" + position + ", fragment:" + fragment);

        setTitle(mNodes[position]);
        mNodeList.setItemChecked(position, true);
        mNodeSlide.closeDrawer(mNodeList);
    }

    public PullToRefreshAttacher getmPullToRefreshAttacher() {
        return mPullToRefreshAttacher;
    }
}
