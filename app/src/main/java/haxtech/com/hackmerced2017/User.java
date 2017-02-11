package haxtech.com.hackmerced2017;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class User {
    public String uid;
    public String name;
    public String email;
    public ArrayList<String> postIDs;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public User(String uid, String name, String email) {
        this.name = name;
        this.email = email;
        postIDs = new ArrayList<>();
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("email", email);
        result.put("posts", postIDs);
        return result;
    }

    public void newPost(String postData){
        this.postIDs.add(postData);
    }
}
