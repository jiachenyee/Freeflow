package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    TextView taskTitleTextView;
    TextView taskDescriptionTextView;
    ImageButton addAssigneeButton;
    Button markAsCompleteButton;

    ArrayList<String> assigneeList = new ArrayList<>();
    ArrayList<String> removedAssigneesList = new ArrayList<>();
    ArrayList<String> workspaceUsers;
    int count = 0;

    Bundle extras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        taskTitleTextView = findViewById(R.id.task_title_text_view);
        taskDescriptionTextView = findViewById(R.id.task_description_text_view);
        markAsCompleteButton = findViewById(R.id.mark_as_complete_button);
        addAssigneeButton = findViewById(R.id.add_assignee2);


        extras = this.getIntent().getExtras();
        workspaceUsers = extras.getStringArrayList("workspaceUsers");
        if (workspaceUsers != null){
            System.out.println("Look HERE Workspace" + workspaceUsers.size());
        }
        loadFromFirestore(extras);
        setUpAccentColor(extras);
        setUpMarkAsCompleteButton(extras);

        addAssigneeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageAssigneeActivity = new Intent(TaskActivity.this, ManageAssigneeActivity.class);
                manageAssigneeActivity.putExtra("workspaceUsers", workspaceUsers);
                manageAssigneeActivity.putExtra("assigneeList", assigneeList);
                manageAssigneeActivity.putExtra("previousActivity", "TaskActivity");
                manageAssigneeActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                startActivityForResult(manageAssigneeActivity, 102);
            }
        });

        if (extras.getString("categoryName") == null) {
            markAsCompleteButton.setVisibility(View.GONE);
            findViewById(R.id.mark_as_complete_card).setVisibility(View.GONE);
            addAssigneeButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102) {
            if(resultCode == RESULT_OK){
                // GET YOUR USER LIST HERE AND USE IT FOR YOUR PURPOSE.
                assigneeList = data.getStringArrayListExtra("assigneeIdList");
                removedAssigneesList = data.getStringArrayListExtra("assigneesRemoved");
                System.out.println(removedAssigneesList.size());
                updateTaskAssignee(extras);
                //loadFromFirestore(extras);
            }
        }
    }

    private void loadFromFirestore(Bundle extras) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String workspaceID = extras.getString("workspaceID");
        String taskID = extras.getString("taskID");

        DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                taskTitleTextView.setText((String) documentSnapshot.get("title"));
                taskDescriptionTextView.setText((String) documentSnapshot.get("description"));

                assigneeList = (ArrayList<String>) documentSnapshot.get("assignee");
                if (assigneeList != null){
                    if (assigneeList.size() > 0){
                        System.out.println("assignee list updated: " + assigneeList.size());
                        decodeUsers(assigneeList);
                    }
                    decodeUsers(assigneeList);
                }
            }
        });
    }

    private void updateTaskAssignee(Bundle extras){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String workspaceID = extras.getString("workspaceID");
        String taskID = extras.getString("taskID");

        DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);
        ArrayList<String> removedList = removedAssigneesList;
        ArrayList<String> assignedList = assigneeList;
        if (removedList.size() > 0){
            for (String uId : removedList){
                //removedAssigneesList.remove(uId);
                docRef.update("assignee", FieldValue.arrayRemove(uId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        System.out.println("Updated");
                        removedAssigneesList.remove(uId);
                        if (removedAssigneesList.size() == 0){
                            loadFromFirestore(extras);
                        }

                    }
                });
            }
        }else{
            for (String uId : assigneeList){
                docRef.update("assignee", FieldValue.arrayUnion(uId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        assignedList.remove(uId);
                        System.out.println("Assignee Added");
                        if (assignedList.size() == 0){
                            loadFromFirestore(extras);
                        }

                    }
                });
            }
        }

    }

    private void decodeUsers(ArrayList<String> assigneeList){
        ArrayList<User> decodedUsers = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (assigneeList.size() > 0){
            for (String userId : assigneeList){
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
                                if (decodedUsers.size() == assigneeList.size()){
                                    System.out.println("Final list size: " + decodedUsers.size());
                                    setUpAssigneeRecyclerView(decodedUsers);
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
        setUpAssigneeRecyclerView(decodedUsers);
    }

    private void setUpAssigneeRecyclerView(ArrayList<User> decodedUsers){
        RecyclerView rv = findViewById(R.id.assignee_recyclerView);
        AssigneeAdapter adapter = new AssigneeAdapter(decodedUsers, TaskActivity.this);
        LinearLayoutManager layout = new LinearLayoutManager(TaskActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        rv.setLayoutManager(layout);
        rv.setAdapter(adapter);
    }

    private void setUpAccentColor(Bundle extras) {
        int color = extras.getInt("accentColor");
        findViewById(R.id.task_header).setBackgroundColor(color);
        addAssigneeButton.setColorFilter(color);
        markAsCompleteButton.setBackgroundColor(color);
    }

    private void setUpMarkAsCompleteButton(Bundle extras) {
        markAsCompleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();

                String workspaceID = extras.getString("workspaceID");
                String taskID = extras.getString("taskID");


                DocumentReference catRef = db.collection("workspaces").document(workspaceID).collection("categories").document(extras.getString("categoryName"));
                catRef.update("subtasks", FieldValue.arrayRemove(taskID)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);

                        docRef.delete();
                        Toast.makeText(TaskActivity.this, "Marked as complete", Toast.LENGTH_LONG).show();
                        finish();
                    }
                });
            }
        });
    }
}