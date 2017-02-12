package haxtech.com.hackmerced2017;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class UserViewActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView textRatingValue, usernameText, excerciseText;
    private Button buttonSubmit;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private String[] mDataset = {""};

    private String body, name, userID, videoUrl, category;
    private Double avgRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        initLocal(savedInstanceState);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(mDataset);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initLocal(Bundle savedInstanceState) {
        body = (String) getIntent().getExtras().get("body");
        name = (String) getIntent().getExtras().get("name");
        userID = (String) getIntent().getExtras().get("userID");
        videoUrl = (String) getIntent().getExtras().get("videoUrl");
        category = (String) getIntent().getExtras().get("category");
        avgRating = (Double) getIntent().getExtras().get("avgRating");

        usernameText = (TextView) findViewById(R.id.DetailView_Username);
        excerciseText = (TextView) findViewById(R.id.DetailView_ExerciseName) ;
        usernameText.setText(name);
        excerciseText.setText(body);
    }

    public void addListenerOnRatingBar() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        textRatingValue = (TextView) findViewById(R.id.txtRatingValue);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                textRatingValue.setText(String.valueOf(rating));
            }
        });
    }

    public void addListenerOnButton() {
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
//        buttonSubmit = (Button) findViewById(R.id.buttonSubmit);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(UserViewActivity.this, String.valueOf(ratingBar.getRating()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}