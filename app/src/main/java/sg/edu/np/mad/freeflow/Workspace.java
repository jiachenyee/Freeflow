package sg.edu.np.mad.freeflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

public class Workspace {
    public String name;

    public Uri workspaceIconURI;

    public int accentColor;
    public String inviteCode;

    public ArrayList<String> users;
    public ArrayList<String> admins;

    public Bitmap workspaceIcon;
    public OnImageLoadHandler onImageLoadHandler;

    Workspace(Map<String, Object> workspaceData) {
        this.name = (String) workspaceData.get("name");
        this.workspaceIconURI = Uri.parse((String) workspaceData.get("workspaceIconURL"));
        this.accentColor = (int) (long) workspaceData.get("accentColor");
        this.inviteCode = (String) workspaceData.get("inviteCode");

        this.users = (ArrayList<String>) workspaceData.get("users");
        this.admins = (ArrayList<String>) workspaceData.get("admin");

        loadImage();
    }

    private void loadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(workspaceIconURI.toString());

                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    workspaceIcon = bmp;

                    if (onImageLoadHandler != null) {
                        onImageLoadHandler.onImageLoad();
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public Workspace setImageLoadHandler(OnImageLoadHandler onImageLoadHandler) {
        this.onImageLoadHandler = onImageLoadHandler;
        return this;
    }

    public interface OnImageLoadHandler {
        void onImageLoad();
    }
}
