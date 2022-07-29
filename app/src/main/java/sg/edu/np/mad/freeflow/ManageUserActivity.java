package sg.edu.np.mad.freeflow;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageUserActivity extends AppCompatActivity {

    ImageButton closeButton;
    Button adminButton;
    Button kickButton;
    EditText NameEditText;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_user);


        closeButton = findViewById(R.id.close_button);
        adminButton = findViewById(R.id.admin_button);
        kickButton = findViewById(R.id.kick_button);
        NameEditText = findViewById(R.id.category_name_edit_text);

        Bundle extras = this.getIntent().getExtras();

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adminButton.setOnClickListener(new View.OnClickListener() { //button to elevate user as admin
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                //DocumentReference userRef = db.collection("users").document(userID);
                //getting text from input
                String Name = NameEditText.getText().toString();

                //finding user with email that matches input, then getting uid from that user document

                // Exit early as data validation
                if (Name.length() == 0) { return; }

                db.collectionGroup("users").whereEqualTo("email", Name).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                List<DocumentSnapshot> doclist = task.getResult().getDocuments();
                                DocumentSnapshot User = doclist.get(0);
                                uid = User.getString("uid");//getting uid of name

                            }
                        });
                String workspaceID = extras.getString("workspaceID"); //getting workspaceID
                DocumentReference Ref = db.collection("workspaces").document(workspaceID);
                // Add admin to workspace
                Ref.update("admin", FieldValue.arrayUnion(uid));

                Toast.makeText(getApplicationContext(),
                                "User elevated to Admin",
                                Toast.LENGTH_LONG) //toast to show user elevated to Admin
                        .show();
                finish();
            }
        });

        kickButton.setOnClickListener(new View.OnClickListener() { //button to remove user
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                //DocumentReference userRef = db.collection("users").document(userID);
                String Name = NameEditText.getText().toString();
                //finding user with the email and getting uid

                // Exit early as data validation
                if (Name.length() == 0) { return; }

                db.collectionGroup("users").whereEqualTo("email", Name).get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()){
                                List<DocumentSnapshot> doclist = task.getResult().getDocuments();
                                DocumentSnapshot User = doclist.get(0);
                                uid = User.getString("uid");//getting uid of name

                            }
                        });
                String workspaceID = extras.getString("workspaceID");
                DocumentReference Ref = db.collection("workspaces").document(workspaceID);
                // remove from user from workspace
                Ref.update("users", FieldValue.arrayRemove(uid));

            }
        });




        LinearLayout headerView = findViewById(R.id.header_view);
        headerView.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

    }
}