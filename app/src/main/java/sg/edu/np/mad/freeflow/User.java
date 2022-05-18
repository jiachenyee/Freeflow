package sg.edu.np.mad.freeflow;

import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class User {
    public String name;
    public String profilePictureURL;

    public User(String name, String profilePictureURL) {
        this.name = name;
        this.profilePictureURL = profilePictureURL;
    }

    public User(FirebaseUser user) {
        this.name = user.getDisplayName();
        this.profilePictureURL = user.getPhotoUrl().toString();
    }

    public Map<String, Object> toHashMap() {
        Map<String, Object> encodedUser = new HashMap<>();
        encodedUser.put("name", name);
        encodedUser.put("profilePictureURL", profilePictureURL);

        return encodedUser;
    }
}
