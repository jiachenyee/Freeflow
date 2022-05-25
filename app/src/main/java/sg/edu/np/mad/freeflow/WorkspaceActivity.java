package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
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

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workspaceSettingsActivity = new Intent(WorkspaceActivity.this, WorkspaceSettingsActivity.class);

                startActivity(workspaceSettingsActivity);
            }
        });
    }

    private void setUpImageView(Bundle extras) {
        Bitmap image = extras.getParcelable("workspaceIcon");

        workspaceImageView.setImageBitmap(image);

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