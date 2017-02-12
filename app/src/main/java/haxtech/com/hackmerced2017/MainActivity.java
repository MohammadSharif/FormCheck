package haxtech.com.hackmerced2017;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TabAdapter adapter;
    private TabLayout tabs;
    private ArrayList<PostObject>[] posts;
    private FloatingActionButton record_button;
    FrameLayout layout_main;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupFeed();

        record_button = (FloatingActionButton) findViewById(R.id.fab_button);

        layout_main = (FrameLayout) findViewById(R.id.main);

        layout_main.getForeground().setAlpha(0);

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent meals_intent = new Intent(v.getContext(), RecordActivity.class);
//                startActivity(meals_intent);

                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = layoutInflater.inflate(R.layout.popover_recorder, null);
                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                layout_main.getForeground().setAlpha(200);
                popupWindow.showAtLocation(v , Gravity.CENTER, 50, -30);

            }
        });

    }

    private void setupFeed() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("posts");
        ref.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Get map of users in datasnapshot
                    setupTabs(collectPosts((Map<String, Object>) dataSnapshot.getValue()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            });
    }

    private void setupTabs(ArrayList<PostObject>[] arrayLists) {
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        adapter = new TabAdapter(this, getSupportFragmentManager(), arrayLists);
        pager.setAdapter(adapter);

        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setTabGravity(TabLayout.GRAVITY_FILL);
        tabs.setupWithViewPager(pager);
        adapter.setPageCount(4);
        tabs.setTabMode(TabLayout.MODE_FIXED);


    }

    private ArrayList<PostObject>[] collectPosts(Map<String, Object> p) {
        ArrayList<PostObject>[] posts = new ArrayList[4];
        for(int i = 0; i < 4; i++){
            posts[i] = new ArrayList<>();
        }
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : p.entrySet()) {
            Map post = (Map) entry.getValue();
            Log.v(TAG,(String)post.get("userID"));
            switch((String)post.get("category")){
                case "Chest":
                    posts[0].add(parseEntry(post));
                    continue;
                case "Back":
                    posts[1].add(parseEntry(post));
                    continue;
                case "Legs":
                    posts[2].add(parseEntry(post));
                    continue;
                case "Full Body":
                    posts[3].add(parseEntry(post));
                    continue;
                default:
                    Log.v(TAG, (String)post.get("category"));
                    continue;
            }
        }
        return posts;
    }

    private PostObject parseEntry(Map post) {
        return new PostObject(
                (String)post.get("body"),
                (String)post.get("userID"),
                (String)post.get("vidUrl"),
                (String)post.get("category"),
                (Long)post.get("avgRating"));
    }

    public void test(String a){
        Log.v(TAG, a);
    }

}

