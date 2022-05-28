package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class WorkspaceSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setting);

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUp();
    }

    private void setUp() {
        Bundle extras = this.getIntent().getExtras();

        LinearLayout workspaceActivityHeader = findViewById(R.id.header_view);
        workspaceActivityHeader.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);
    }
}