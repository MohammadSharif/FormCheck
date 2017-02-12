package haxtech.com.hackmerced2017;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class TabFragment extends Fragment {
    private static final String TAG = "TabFragment";
    private static final String KEY_POSITION="position";
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    static ArrayList<PostObject>[] posts;

    static TabFragment newInstance(int position, ArrayList<PostObject>[] aL) {
        TabFragment frag=new TabFragment();
        Bundle args=new Bundle();
        posts = aL;

        args.putInt(KEY_POSITION, position);
        frag.setArguments(args);


        return(frag);
    }

    static String getTitle(Context ctxt, int position) {
        switch (position){
            case 0:
                return "chest";
            case 1:
                return "body";
            case 2:
                return "legs";
            case 3:
                return "fullBody";
            default:
                return "wtf";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View result=inflater.inflate(R.layout.fragment_tab, container, false);
        int position=getArguments().getInt(KEY_POSITION, -1);

        mRecyclerView = (RecyclerView) result.findViewById(R.id.my_recycler_view);
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        Log.v(TAG, "check");
                    }
                }));
        mLayoutManager = new LinearLayoutManager(result.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        Log.v(TAG,String.valueOf(position));
        mAdapter = new CardAdapter(getCategoryPosts(position));
        mRecyclerView.setAdapter(mAdapter);
        return(result);
    }

    private ArrayList<PostObject> getCategoryPosts(int position) {
        switch (position){
            case 0:
                return posts[0];
            case 1:
                return posts[1];
            case 2:
                return posts[2];
            case 3:
                return posts[3];
            default:
                return posts[0];
        }
    }

    public void test(String a){
        Log.v(TAG, a);
    }
}