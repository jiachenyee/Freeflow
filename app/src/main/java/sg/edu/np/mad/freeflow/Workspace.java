package sg.edu.np.mad.freeflow;

import android.graphics.Bitmap;
import android.net.Uri;

import java.util.ArrayList;
import java.util.Map;

public class Workspace {
    public String name;

    public Uri workspaceIconURI;

    public int accentColor;
    public String inviteCode;

    public ArrayList<String> users;
    public ArrayList<String> admins;

    Workspace(Map<String, Object> workspaceData) {
        this.name = (String) workspaceData.get("name");
        this.workspaceIconURI = Uri.parse((String) workspaceData.get("workspaceIconURL"));
        this.accentColor = (int) (long) workspaceData.get("accentColor");
        this.inviteCode = (String) workspaceData.get("inviteCode");

        this.users = (ArrayList<String>) workspaceData.get("users");
        this.admins = (ArrayList<String>) workspaceData.get("admin");
    }
}
