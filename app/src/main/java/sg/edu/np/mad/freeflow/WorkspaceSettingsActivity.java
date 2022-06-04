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

        setUpRecyclerView(extras);
        setUpTitleBar(extras);
    }

    private void setUpRecyclerView(Bundle extras) {
        RecyclerView.Adapter mAdapter = new WorkspaceSettingsAdapter(extras, this);

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
}