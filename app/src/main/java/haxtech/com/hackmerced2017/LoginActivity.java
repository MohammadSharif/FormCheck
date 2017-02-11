package haxtech.com.hackmerced2017;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "LoginActivity";
    private static final int RC_SIGN_IN = 0;
    private FirebaseAuth auth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser() != null){
            // User has already signed in
            createUserIfDoesNotExist(auth.getCurrentUser());
            Log.d("AUTH", auth.getCurrentUser().getUid());
        } else {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().setProviders(
                    AuthUI.FACEBOOK_PROVIDER,
                    AuthUI.EMAIL_PROVIDER,
                    AuthUI.GOOGLE_PROVIDER
            ).build(), RC_SIGN_IN);
        }


        findViewById(R.id.log_out_button).setOnClickListener(this);
        findViewById(R.id.make_post_button).setOnClickListener(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.v(TAG,"dfsfd");
        if(requestCode == RC_SIGN_IN){
            if(resultCode == RESULT_OK){
                // user logged in
                FirebaseUser firebaseUser = auth.getCurrentUser();
                createUserIfDoesNotExist(firebaseUser);
                Log.d("AUTH", firebaseUser.getUid());

                Intent main_intent = new Intent(this, MainActivity.class);
                //TODO: pass user to main activity
                startActivity(main_intent);
            } else {
                //user not authenticated
                Log.d("AUTH", "NOT AUTHENTICATED");
            }
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.log_out_button){
            AuthUI.getInstance().signOut(this).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("AUTH", "USER HAS LOGGED OUT");
                    auth.signOut();
                    finish();

                }
            });
        } else if(v.getId() == R.id.make_post_button) {
            Intent intent = new Intent(this, CreatePostActivity.class);
            startActivity(intent);
        }
    }

    private void createUserIfDoesNotExist(FirebaseUser user) {
        final FirebaseUser finalUser = user;
        DatabaseReference usersRef = mDatabase.child("users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.hasChild(finalUser.getUid())) {
                    Log.d("AUTH", "USER NOT FOUND, CREATING USER NOW");
                    String userID = finalUser.getUid();
                    User userObj = new User(userID, finalUser.getDisplayName(), finalUser.getEmail());
                    Map<String, Object> userValues = userObj.toMap();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("/users/" + userID, userValues);
                    mDatabase.updateChildren(childUpdates);
                }
            }
            @Override
            public void onCancelled(DatabaseError firebaseError) { }
        });
    }

}
