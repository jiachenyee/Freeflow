package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class WorkspaceInviteActivity extends AppCompatActivity {

    TextView joinWorkspaceTitleTextView;
    TextView joinWorkspaceURLTextView;
    ImageButton closeButton;
    Button copyInviteLinkButton;
    ImageView workspaceImage;
    TextView instructionsTextView;
    CardView joinWorkspaceURLCard;
    LinearLayout headerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_invite);

        Bundle extras = this.getIntent().getExtras();

        Bitmap icon = extras.getParcelable("workspaceIcon");
        int accentColor = Workspace.colors[extras.getInt("workspaceAccentColor")];
        String workspaceName = extras.getString("workspaceName");
        String workspaceInviteCode = extras.getString("workspaceInviteCode");

        joinWorkspaceTitleTextView = findViewById(R.id.join_workspace_title_text_view);
        joinWorkspaceURLTextView = findViewById(R.id.join_workspace_url_text_view);
        closeButton = findViewById(R.id.close_button);
        copyInviteLinkButton = findViewById(R.id.copy_invite_link_button);
        workspaceImage = findViewById(R.id.workspace_image);
        instructionsTextView = findViewById(R.id.join_workspace_description);
        joinWorkspaceURLCard = findViewById(R.id.join_workspace_url_card);
        headerView = findViewById(R.id.header_view);

        joinWorkspaceTitleTextView.setText("Join \"" + workspaceName + "\"");

        String joinURL = "npff.page.link/" + workspaceInviteCode;

        joinWorkspaceURLTextView.setText(joinURL);

        instructionsTextView.setText("1. Visit " + joinURL + " \n2. If you do not have Freeflow installed, install it from the Play Store \n3. Accept the invite \n4. You're all set!");

        setUpImage(icon);
        setUpHeader(accentColor);
        setUpCopyButton(joinURL);
    }

    private void setUpHeader(int accentColor) {
        copyInviteLinkButton.setBackgroundResource(accentColor);
        headerView.setBackgroundResource(accentColor);
        joinWorkspaceURLCard.setCardBackgroundColor(ContextCompat.getColor(this, accentColor));

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpImage(Bitmap icon) {
        if (icon != null) {
            workspaceImage.setImageBitmap(icon);
        } else {
            workspaceImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.workspace_icon));
        }
    }

    private void setUpCopyButton(String joinURL) {
        copyInviteLinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Workspace Invite URL", joinURL);
                clipboard.setPrimaryClip(clip);

                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, "Copied to Clipboard", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}