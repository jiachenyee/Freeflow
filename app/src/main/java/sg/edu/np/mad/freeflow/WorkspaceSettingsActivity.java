package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class WorkspaceSettingsActivity extends AppCompatActivity {

    RecyclerView settingsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setting);

        Bundle extras = this.getIntent().getExtras();

        settingsRecyclerView = findViewById(R.id.settings_recycler_view);

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpRecyclerView();
        setUpTitleBar(extras);
    }

    private void setUpRecyclerView() {
        RecyclerView.Adapter mAdapter = new WorkspaceSettingsAdapter();

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        settingsRecyclerView.setLayoutManager(mLayoutManager);
        settingsRecyclerView.setItemAnimator(new DefaultItemAnimator());
        settingsRecyclerView.setAdapter(mAdapter);
    }

    private void setUpTitleBar(Bundle extras) {
        int color = Workspace.colors[extras.getInt("workspaceAccentColor",0)];

        LinearLayout workspaceActivityHeader = findViewById(R.id.header_view);
        workspaceActivityHeader.setBackgroundResource(color);
    }

    private void setUp(Bundle extras) {
        int color = Workspace.colors[extras.getInt("workspaceAccentColor",0)];

        setUpInviteButton(extras);

        CardView workspaceNameCard = findViewById(R.id.workspace_name_edit_text_card);
        workspaceNameCard.setCardBackgroundColor(ContextCompat.getColor(this, color));

        ImageView workspaceImage = findViewById(R.id.workspace_image);

        if (extras.getParcelable("workspaceIcon") == null) {
            workspaceImage.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.workspace_icon));
        } else {
            workspaceImage.setImageBitmap((Bitmap) extras.getParcelable("workspaceIcon"));
        }

        EditText workspaceNameEditText = findViewById(R.id.workspace_name_edit_text);
        workspaceNameEditText.setText(extras.getString("workspaceName"));
    }

    private void setUpInviteButton(Bundle extras) {
        Button inviteButton = findViewById(R.id.invite_button);
        inviteButton.setTextColor(getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workspaceInviteActivity = new Intent(WorkspaceSettingsActivity.this, WorkspaceInviteActivity.class);

                workspaceInviteActivity.putExtra("workspaceIcon", (Bitmap) extras.getParcelable("workspaceIcon"));
                workspaceInviteActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                workspaceInviteActivity.putExtra("workspaceName", extras.getString("workspaceName"));
                workspaceInviteActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));

                startActivity(workspaceInviteActivity);
            }
        });
    }
}