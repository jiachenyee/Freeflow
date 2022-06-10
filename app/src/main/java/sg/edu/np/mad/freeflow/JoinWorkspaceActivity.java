package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

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

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("workspaces")
                        .whereEqualTo("inviteCode", inviteCode)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QueryDocumentSnapshot workspaceSnapshot = null;

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                workspaceSnapshot = document;
                                break;
                            }

                            if (workspaceSnapshot == null) {
                                showErrorToast("Invalid invite code");
                            } else {
                                
                            }
                        } else {
                            showErrorToast("Error getting workspaces");
                        }
                    }
                });
            }
        });
    }

    private void showErrorToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private String stripURL(String inviteCode) {
        if (inviteCode.contains("npff.page.link/")) {
            String[] inviteCodeComponents = inviteCode.split("/");

            return inviteCodeComponents[inviteCodeComponents.length - 1];
        }
        return inviteCode;
    }
}