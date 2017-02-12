package haxtech.com.hackmerced2017;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RecordActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private static final String TAG = "RecordActivity";
    private Button button2;
    private VideoView mVideoView;
    private ImageView playButton;
    private Button submitButton;
    private EditText recording_name;
    private Spinner categorySpinner;

    private Uri savedVideoData;
    private Uri downloadUri;

    final int REQUEST_VIDEO_CAPTURE = 1;

    private FirebaseAuth auth;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        dispatchTakeVideoIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        mVideoView = (VideoView) findViewById(R.id.recorded_video);
        playButton = (ImageView) findViewById(R.id.recorded_video_play_button);
        submitButton = (Button) findViewById(R.id.recording_button_submit);
        recording_name = (EditText) findViewById(R.id.recording_name);
        categorySpinner = (Spinner) findViewById(R.id.categorySpinner);
        categorySpinner.setOnItemSelectedListener(this);
        List<String> categories = new ArrayList<String>();
        categories.add("Chest");
        categories.add("Back");
        categories.add("Legs");
        categories.add("Full Body");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        //dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(dataAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        auth = FirebaseAuth.getInstance();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReferenceFromUrl("gs://hackmerced2017.appspot.com");

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new UploadAndPostTask().execute();
            }
        });
    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        takeVideoIntent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 10);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Log.v(TAG, "here");
            this.savedVideoData = intent.getData();
            playButton.setVisibility(View.GONE);
            mVideoView.setVideoURI(savedVideoData);
            mVideoView.seekTo(100);
            mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mVideoView.start();
                    mp.setLooping(true);
                }
            });
        } else {
            finish();
        }
    }

    public void uploadVideo(Uri data) {
        StorageReference filesRef = storageRef.child("videos/"+data.getLastPathSegment());
        // Get the data from an ImageView as bytes
        UploadTask uploadTask = filesRef.putFile(data);

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                System.out.println("Upload is " + progress + "% done");
            }
        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onPaused(UploadTask.TaskSnapshot taskSnapshot) {
                System.out.println("Upload is paused");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Log.e("UPLOAD", "FAILED");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                StorageMetadata metadata = taskSnapshot.getMetadata();
                saveDownloadUriAndTriggerWrite(metadata);
                Log.v("UPLOAD", "SUCCESS");
            }
        });
    }

    private void saveDownloadUriAndTriggerWrite(StorageMetadata metadata) {
        this.downloadUri = metadata.getDownloadUrl();
        String selectedCategory = this.categorySpinner.getSelectedItem().toString();
        String downloadUriStr = this.downloadUri.toString();
        Log.v("RecordActivity", this.downloadUri.toString());
        writeNewPost(this.recording_name.getText().toString(), downloadUriStr, selectedCategory);
    }

    private void writeNewPost(String body, String vidUrl, String category) {
        FirebaseUser curUser = auth.getCurrentUser();
        String key = mDatabase.child("posts").push().getKey();
        String userID = curUser.getUid();
        Post post = new Post(userID, body, vidUrl, category);
        Map<String, Object> postValues = post.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/posts/" + key, postValues);
        DatabaseReference listOfPostsRef = mDatabase.child("users").child(userID).child("posts");
        listOfPostsRef.push().setValue(key);
        mDatabase.updateChildren(childUpdates);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.v(TAG, parent.getItemAtPosition(position).toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private class UploadAndPostTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                uploadVideo(savedVideoData);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
