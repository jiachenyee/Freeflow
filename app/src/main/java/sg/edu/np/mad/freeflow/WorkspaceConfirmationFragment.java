package sg.edu.np.mad.freeflow;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class WorkspaceConfirmationFragment extends Fragment {

    Button cancelButton;
    Button confirmButton;
    ImageView workspaceImageView;
    TextView workspaceTitleTextView;

    Bitmap workspaceIcon;

    private String workspaceName;
    private String workspaceIconURI;
    private OnActionHandler onActionHandler;

    public WorkspaceConfirmationFragment() {
        // Required empty public constructor
    }

    public static WorkspaceConfirmationFragment newInstance(String workspaceName, String workspaceIconURI, OnActionHandler onActionHandler) {
        WorkspaceConfirmationFragment fragment = new WorkspaceConfirmationFragment();

        fragment.onActionHandler = onActionHandler;
        fragment.workspaceIconURI = workspaceIconURI;
        fragment.workspaceName = workspaceName;

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_workspace_confirmation, container, false);

        confirmButton = v.findViewById(R.id.confirm_button);
        workspaceImageView = v.findViewById(R.id.workspace_image);
        workspaceTitleTextView = v.findViewById(R.id.workspace_title_text_view);
        cancelButton = v.findViewById(R.id.cancel_button);

        workspaceTitleTextView.setText("Join \"" + workspaceName + "\"");

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onActionHandler != null) {
                    onActionHandler.onCancel();
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onActionHandler != null) {
                    onActionHandler.onConfirm();
                }
            }
        });

        loadImage();

        return v;
    }

    private void loadImage() {

        if (workspaceIconURI == null) { return; }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(workspaceIconURI);

                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    workspaceIcon = bmp;

                    workspaceImageView.setImageBitmap(workspaceIcon);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface OnActionHandler {
        void onCancel();
        void onConfirm();
    }
}