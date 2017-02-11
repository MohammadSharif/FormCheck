package haxtech.com.hackmerced2017;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Comment {
    public String uid;
    public String userID;
    public String body;

    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    }

    public Comment(String userID, String body) {
        this.userID = userID;
        this.body = body;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userID", userID);
        result.put("body", body);
        return result;
    }
}
