package haxtech.com.hackmerced2017;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class TabFragment extends Fragment {
    private static final String KEY_POSITION="position";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] ts = new String[]{"00","100","120"};

    static TabFragment newInstance(int position) {
        TabFragment frag=new TabFragment();
        Bundle args=new Bundle();

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);

        return(frag);
    }

    static String getTitle(Context ctxt, int position) {
        return(String.format(ctxt.getString(R.string.hint), position + 1));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_tab, container, false);
        int position=getArguments().getInt(KEY_POSITION, -1);

        mRecyclerView = (RecyclerView) result.findViewById(R.id.my_recycler_view);
        mLayoutManager = new LinearLayoutManager(result.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CardAdapter(ts);
        mRecyclerView.setAdapter(mAdapter);
        return(result);
    }
}