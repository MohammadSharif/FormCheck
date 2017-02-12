package haxtech.com.hackmerced2017;

/**
 * Created by aatifshah on 2/11/17.
 */

public class PostObject {
    public String body, userID, vidUrl, category;
    public double avgRating;

    PostObject(String b, String u, String v, String c, double a){
        body = b;
        userID = u;
        vidUrl = v;
        category = c;
        avgRating = a;
    }

}
