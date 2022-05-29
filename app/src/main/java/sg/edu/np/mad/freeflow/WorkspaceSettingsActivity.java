package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class WorkspaceSettingsActivity extends AppCompatActivity {

    Button inviteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setting);

        Bundle extras = this.getIntent().getExtras();

        inviteButton = findViewById(R.id.invite_button);

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUp(extras);
    }

    private void setUp(Bundle extras) {

        LinearLayout workspaceActivityHeader = findViewById(R.id.header_view);
        workspaceActivityHeader.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        setUpInviteButton(extras);
    }

    private void setUpInviteButton(Bundle extras) {
        inviteButton.setTextColor(getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workspaceInviteActivity = new Intent(WorkspaceSettingsActivity.this, WorkspaceInviteActivity.class);

                startActivity(workspaceInviteActivity);
            }
        });
    }
}