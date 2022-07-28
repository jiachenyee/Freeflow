package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
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
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class WorkspaceSettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    RecyclerView settingsRecyclerView;
    Button leaveButton;
    Button manageButton;
    FirebaseFirestore db;
    ArrayList<String> admins;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace_setting);

        Bundle extras = this.getIntent().getExtras(); //extras stores the specific workspace INFO!
        //getting admin list of disabling of buttons
        admins = extras.getStringArrayList("admin");

        settingsRecyclerView = findViewById(R.id.settings_recycler_view);
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        //setting visibility of leave button based on admin status of current user
        leaveButton = findViewById(R.id.workspace_setting_leavebutton);
        leaveButton.setVisibility(admins.contains(mAuth.getCurrentUser().getUid()) ? View.VISIBLE : View.INVISIBLE);

        //setting visibility of manage button based on admin status of current user
        manageButton = findViewById(R.id.manage_button);
        manageButton.setVisibility(admins.contains(mAuth.getCurrentUser().getUid()) ? View.VISIBLE : View.INVISIBLE);

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        findViewById(R.id.workspace_setting_leavebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                db.collection("users").document(uid).update("workspaces",FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.collection("workspaces").document(extras.getString("workspaceID")).update("users", FieldValue.arrayUnion(uid)).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Intent returnHomeActivity = new Intent(WorkspaceSettingsActivity.this, MainActivity.class);
                                startActivity(returnHomeActivity);
                                showInfoToast("Successfully left workspace");

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showInfoToast("Error leaving workspace");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showInfoToast("Error leaving workspace");
                    }
                });
            }
        }); //end of leave button func

        findViewById(R.id.workspace_setting_deletebutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                db.collection("users").document(uid).update("workspaces",FieldValue.delete()).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        db.collection("workspaces").document(extras.getString("workspaceID")).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                                Intent returnHomeActivity = new Intent(WorkspaceSettingsActivity.this, MainActivity.class);
                                startActivity(returnHomeActivity);
                                showInfoToast("Successfully deleted workspace");

                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                showInfoToast("Error leaving workspace");
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showInfoToast("Error leaving workspace");
                    }
                });
            }
        }); //end of delete button func


        setUpRecyclerView(extras);
        setUpTitleBar(extras);
    }
    private void showInfoToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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