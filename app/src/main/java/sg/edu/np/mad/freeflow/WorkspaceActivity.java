package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class WorkspaceActivity extends AppCompatActivity {

    ImageView workspaceImageView;
    TextView workspaceNameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        workspaceImageView = findViewById(R.id.workspace_image);

        Bundle extras = this.getIntent().getExtras();

        setUpImageView(extras);
        setUpWorkspaceTitle(extras);

        ImageView settingsImageView = findViewById(R.id.settings_image_view);

        ConstraintLayout workspaceActivityHeader = findViewById(R.id.workspace_activity_header);
        workspaceActivityHeader.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workspaceSettingsActivity = new Intent(WorkspaceActivity.this, WorkspaceSettingsActivity.class);

                workspaceSettingsActivity.putExtra("workspaceIcon", (Bitmap) extras.getParcelable("workspaceIcon"));
                workspaceSettingsActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                workspaceSettingsActivity.putExtra("workspaceName", extras.getString("workspaceName"));
                workspaceSettingsActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));
                workspaceSettingsActivity.putExtra("workspaceUsers", extras.getStringArray("workspaceUsers"));
                workspaceSettingsActivity.putExtra("workspaceAdmins", extras.getStringArray("workspaceAdmins"));

                startActivity(workspaceSettingsActivity);
            }
        });
    }

    private void setUpImageView(Bundle extras) {
        Bitmap image = extras.getParcelable("workspaceIcon");

        if (image != null) {
            workspaceImageView.setImageBitmap(image);
        } else {
            workspaceImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.workspace_icon));
        }

        workspaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpWorkspaceTitle(Bundle extras) {
        workspaceNameTextView = findViewById(R.id.workspace_name_show);
        String workspaceName = extras.getString("workspaceName");

        workspaceNameTextView.setText(workspaceName);
    }

    private void setUpWorkspaceAccentColor(Bundle extras) {
        String workspaceName = extras.getString("workspaceName");

        workspaceNameTextView.setText(workspaceName);
    }
}