package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ManageAssigneeActivity extends AppCompatActivity {;

    ArrayList<String> assigneeList = new ArrayList<String>();       //list of assignee
    ArrayList<String> removedAssignees = new ArrayList<String>();   //List of removed assignees
    ArrayList<String> userIdList;                                   //List of userId
    Button assignButton;                                            //Button to assign users

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_assignee);

        Bundle extras = getIntent().getExtras();
        userIdList = extras.getStringArrayList("workspaceUsers");
        assigneeList = extras.getStringArrayList("assigneeList");

        LinearLayout memberLinearLayout = findViewById(R.id.member_lineaer_layout);
        assignButton = findViewById(R.id.assign_button);

        //Action to perform on click
        assignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent to pass back to previous activity
                Intent newTaskActivity = new Intent();
                //Pass the list of assignee ID
                newTaskActivity.putExtra("assigneeIdList", assigneeList);
                //Pass the the list of assignee id removed from task
                newTaskActivity.putExtra("assigneesRemoved", removedAssignees);
                setResult(RESULT_OK, newTaskActivity);
                finish();
            }
        });
        //Set action for the close button
        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //Set up the accent color
        setUpAccentColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);
        //Convert Userid to user objects
        decodeWorkSpaceUsers(userIdList, memberLinearLayout);
    }

    //Method to convert user Id to user objects
    private void decodeWorkSpaceUsers(ArrayList<String> userIdList, LinearLayout layout){
        //Instantiate an empty list of user objects
        ArrayList<User> decodedUsers = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        //Run through list of user id and convert to user objects from firebase
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
                            if (decodedUsers.size() == userIdList.size()){
                                //Once all converted set up the view
                                setUpLayoutInflator(decodedUsers, layout);
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
    //Method to set up the view for the manage assignee activity
    private void setUpLayoutInflator(ArrayList<User> decodedUsers, LinearLayout layout){
        //Inflate viewholder to parent view
        for (int i = 0; i < decodedUsers.size(); i ++){
            LayoutInflater inflater = getLayoutInflater();
            View mylayout = inflater.inflate(R.layout.assignee_custom_view_holder, layout, false);
            //Set all relevant widgets in the view
            ImageView userImage = mylayout.findViewById(R.id.profile_image_view);
            TextView userName = mylayout.findViewById(R.id.assignee_text_view);
            CheckBox assigneeCheckBox = mylayout.findViewById(R.id.assignee_checkbox);
            //User picasso to load url to image
            Picasso.with(this).load(decodedUsers.get(i).profilePictureURL).into(userImage);
            userName.setText(decodedUsers.get(i).name);
            assigneeCheckBox.setId(i);
            //Check if there are people assigned to this task already
            if (assigneeList.size() > 0 && assigneeList.contains(decodedUsers.get(assigneeCheckBox.getId()).userID)){ assigneeCheckBox.setChecked(true); }
            //Action to perform when check box is checked and vice versa
            assigneeCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        //Add assignee to assignee list, add assignee to removed list
                        if (removedAssignees.size() > 0 && removedAssignees.contains(decodedUsers.get(assigneeCheckBox.getId()).userID)){
                            removedAssignees.remove(decodedUsers.get(assigneeCheckBox.getId()).userID);
                            assigneeList.add(decodedUsers.get(assigneeCheckBox.getId()).userID);

                        }
                        assigneeList.add(decodedUsers.get(assigneeCheckBox.getId()).userID);
                    }else{
                        assigneeList.remove(decodedUsers.get(assigneeCheckBox.getId()).userID);
                        removedAssignees.add(decodedUsers.get(assigneeCheckBox.getId()).userID);

                    }

                }
            });
            //Add view holder to parent view once all functions are completed.
            layout.addView(mylayout);
        }

    }
    //Method to set up accent color
    private void setUpAccentColor(int colorResource) {

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundResource(colorResource);

        findViewById(R.id.assign_button).setBackgroundResource(colorResource);
    }
}