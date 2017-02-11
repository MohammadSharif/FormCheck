package haxtech.com.hackmerced2017;

/**
 * Created by aatifshah on 2/11/17.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabAdapter extends FragmentPagerAdapter {
    private final Context ctxt;
    private int pageCount=10;

    public TabAdapter(Context ctxt, FragmentManager mgr) {
        super(mgr);

        this.ctxt=ctxt;
    }

    @Override
    public int getCount() {
        return(pageCount);
    }

    @Override
    public Fragment getItem(int position) {
        return(TabFragment.newInstance(position));
    }

    @Override
    public String getPageTitle(int position) {
        return(TabFragment.getTitle(ctxt, position));
    }

    void setPageCount(int pageCount) {
        this.pageCount=pageCount;
    }
}