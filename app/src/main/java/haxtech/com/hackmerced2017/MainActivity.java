package haxtech.com.hackmerced2017;

import android.content.Intent;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TabAdapter adapter;
    private TabLayout tabs;
    private FloatingActionButton record_button;
    FrameLayout layout_main;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager=(ViewPager)findViewById(R.id.pager);
        record_button = (FloatingActionButton) findViewById(R.id.fab_button);

        adapter=new TabAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs=(TabLayout)findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        adapter.setPageCount(10);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        layout_main = (FrameLayout) findViewById(R.id.main);

        layout_main.getForeground().setAlpha(0);

        record_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent meals_intent = new Intent(v.getContext(), RecordActivity.class);
                startActivity(meals_intent);

//                LayoutInflater layoutInflater = (LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
//                View popupView = layoutInflater.inflate(R.layout.popover_recorder, null);
//                final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                layout_main.getForeground().setAlpha(200);
//                popupWindow.showAtLocation(, Gravity.CENTER, 50, -30);


            }
        });
    }

}
