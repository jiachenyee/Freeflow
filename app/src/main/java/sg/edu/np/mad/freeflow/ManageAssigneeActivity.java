package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ManageAssigneeActivity extends AppCompatActivity {;

    ArrayList<String> assigneeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_assignee);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> userIdList = extras.getStringArrayList("workspaceUsers");
        assigneeList = extras.getStringArrayList("assigneeList");

        LinearLayout memberLinearLayout = findViewById(R.id.member_lineaer_layout);
        Button assignButton = findViewById(R.id.assign_button);

        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTaskActivity = new Intent();
                newTaskActivity.putExtra("assigneeIdList", assigneeList);
                setResult(RESULT_OK, newTaskActivity);
                finish();
            }
        });

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpAccentColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);
        decodeWorkSpaceUsers(userIdList, memberLinearLayout);
    }

    private void decodeWorkSpaceUsers(ArrayList<String> userIdList, LinearLayout layout){
        ArrayList<User> decodedUsers = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();


        for (String userId : userIdList){
            DocumentReference docRef = db.collection("users").document(userId);
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) { ;
                            User user = new User(document.getData(), userId);
                            decodedUsers.add(user);
                            System.out.println(decodedUsers.get(0).name);
                            System.out.println("Here" + decodedUsers.size());
                            if (decodedUsers.size() == userIdList.size()){
                                System.out.println("Final list size: " + decodedUsers.size());
                                createAssigneeCheckBox(decodedUsers, layout);
                            }
                        } else {
                            System.out.println("User not found");
                        }
                    } else {
                        System.out.println("error while getting User");
                    }
                }
            });
        }
    }

    private void createAssigneeCheckBox(ArrayList<User> decodedUsers, LinearLayout layout){
        for(int i = 0; i < decodedUsers.size(); i ++){
            // Create Checkbox Dynamically
            CheckBox checkBox = new CheckBox(this);
            checkBox.setId(i);
            checkBox.setText(decodedUsers.get(i).name);
            if (assigneeList.size() > 0 && assigneeList.contains(decodedUsers.get(checkBox.getId()).userID)){ checkBox.setChecked(true); }
            checkBox.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        assigneeList.add(decodedUsers.get(checkBox.getId()).userID);
                        System.out.println(assigneeList.size() + " before");
                        String msg = "You have " + (isChecked ? "checked" : "unchecked") + " checkbox " + decodedUsers.get(checkBox.getId()).userID;
                        Toast.makeText(ManageAssigneeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }else{
                        assigneeList.remove(decodedUsers.get(checkBox.getId()).userID);
                        String msg = assigneeList.size() + " after";
                        Toast.makeText(ManageAssigneeActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }

                }
            });
            // Add Checkbox to LinearLayout
            if (layout != null) {
                layout.addView(checkBox);
            }
        }
    }

    private void setUpAccentColor(int colorResource) {

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundResource(colorResource);

        findViewById(R.id.assign_button).setBackgroundResource(colorResource);
    }
}