package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class JoinWorkspaceActivity extends AppCompatActivity {

    ImageButton closeButton;

    FirebaseFirestore db;

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

        db = FirebaseFirestore.getInstance();

        setUpInitialState();
    }
    WorkspaceInviteFragment workspaceInviteFragment;

    private void setUpInitialState() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        workspaceInviteFragment = WorkspaceInviteFragment.newInstance(new WorkspaceInviteFragment.OnSubmitHandler() {
            @Override
            public void onSubmit(String code) {
                validateCode(code);
            }
        });

        ft.replace(R.id.join_workspace_fragment, workspaceInviteFragment);
        ft.commit();
    }

    private void setUpConfirmState(String name, String workspaceIcon, String workspaceID) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        ft.replace(R.id.join_workspace_fragment,
                WorkspaceConfirmationFragment.newInstance(name, workspaceIcon, new WorkspaceConfirmationFragment.OnActionHandler() {
                    @Override
                    public void onCancel() {
                        setUpInitialState();
                    }

                    @Override
                    public void onConfirm() {
                        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        db.collection("users").document(uid).update("workspaces", FieldValue.arrayUnion(workspaceID)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                db.collection("workspaces").document(workspaceID).update("users", FieldValue.arrayUnion(uid)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent = new Intent();
                                        intent.putExtra("workspaceID", workspaceID);

                                        setResult(200, intent);

                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        showErrorToast("Error joining workspace");
                                    }
                                });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showErrorToast("Error joining workspace");
                            }
                        });
                    }
                }));

        ft.commit();
    }

    private void validateCode(String code) {
        String inviteCode = stripURL(code);

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
                                String name = (String) workspaceSnapshot.get("name");
                                String workspaceIconURL = (String) workspaceSnapshot.get("workspaceIconURL");

                                setUpConfirmState(name, workspaceIconURL, workspaceSnapshot.getId());
                            }
                        } else {
                            showErrorToast("Error getting workspaces");
                        }
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