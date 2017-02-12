package haxtech.com.hackmerced2017;

/**
 * Created by aatifshah on 2/11/17.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class TabAdapter extends FragmentPagerAdapter {
    private final Context ctxt;
    private int pageCount=4;
    private ArrayList<PostObject>[] arrayLists;

    public TabAdapter(Context ctxt, FragmentManager mgr, ArrayList<PostObject>[] aL) {
        super(mgr);
        this.ctxt=ctxt;
        arrayLists = aL;
    }

    @Override
    public int getCount() {
        return(pageCount);
    }

    @Override
    public Fragment getItem(int position) {
        return TabFragment.newInstance(position, arrayLists);
    }

    @Override
    public String getPageTitle(int position) {
        return(TabFragment.getTitle(ctxt, position));
    }

    void setPageCount(int pageCount) {
        this.pageCount=pageCount;
    }
}