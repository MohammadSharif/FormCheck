package haxtech.com.hackmerced2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private TabAdapter adapter;
    private TabLayout tabs;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager pager=(ViewPager)findViewById(R.id.pager);

        adapter=new TabAdapter(this, getSupportFragmentManager());
        pager.setAdapter(adapter);

        tabs=(TabLayout)findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        adapter.setPageCount(10);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

}
