package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class JoinWorkspaceActivity extends AppCompatActivity {

    ImageButton closeButton;
    Button joinButton;
    EditText workspaceInviteCodeEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_workspace);

        closeButton = findViewById(R.id.close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        joinButton = findViewById(R.id.join_workspace_button);

        workspaceInviteCodeEditText = findViewById(R.id.workspace_invite_code_edit_text);

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inviteCode = stripURL(workspaceInviteCodeEditText.getText().toString());
                
            }
        });
    }

    private String stripURL(String inviteCode) {
        if (inviteCode.contains("npff.page.link/")) {
            String[] inviteCodeComponents = inviteCode.split("/");

            return inviteCodeComponents[inviteCodeComponents.length - 1];
        }
        return inviteCode;
    }
}