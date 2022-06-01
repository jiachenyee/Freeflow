package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

public class WorkspaceInviteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_invite);

        Bundle extras = this.getIntent().getExtras();

        Bitmap icon = extras.getParcelable("workspaceIcon");
        int accentColor = extras.getInt("workspaceAccentColor");
        String workspaceName = extras.getString("workspaceName");
        String workspaceInviteCode = extras.getString("workspaceInviteCode");

        TextView joinWorkspaceTitleTextView = findViewById(R.id.join_workspace_title_text_view);
        TextView joinWorkspaceURLTextView = findViewById(R.id.join_workspace_url_text_view);
        ImageButton closeButton = findViewById(R.id.close_button);
        Button copyInviteLinkButton = findViewById(R.id.copy_invite_link_button);
        ImageView workspaceImage = findViewById(R.id.workspace_image);

        joinWorkspaceTitleTextView.setText("Join \"" + workspaceName + "\"");

        if (icon != null) {
            workspaceImage.setImageBitmap(icon);
        } else {
            workspaceImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.workspace_icon));
        }

        String joinURL = "npff.page.link/" + workspaceInviteCode;

        joinWorkspaceURLTextView.setText(joinURL);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        copyInviteLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Workspace Invite URL", joinURL);
                clipboard.setPrimaryClip(clip);
            }
        });
    }
}