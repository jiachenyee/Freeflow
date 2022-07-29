package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TaskActivity extends AppCompatActivity {

    TextView taskTitleTextView;
    TextView taskDueDateTextView;
    TextView taskDescriptionTextView;
    ImageButton addAssigneeButton;
    Button markAsCompleteButton;
    FloatingActionButton floatingActionButton;

    RecyclerView taskDetailRecyclerView;

    ArrayList<String> urls = new ArrayList<>();
    ArrayList<Map<String, Object>> subtasks = new ArrayList<>();

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
        taskDueDateTextView = findViewById(R.id.task_due_date_text_view);
        taskDescriptionTextView = findViewById(R.id.task_description_text_view);
        markAsCompleteButton = findViewById(R.id.mark_as_complete_button);
        addAssigneeButton = findViewById(R.id.add_assignee2);

        floatingActionButton = findViewById(R.id.floating_action_button);
        taskDetailRecyclerView = findViewById(R.id.task_detail_recycler_view);

        extras = this.getIntent().getExtras();
        workspaceUsers = extras.getStringArrayList("workspaceUsers");
        if (workspaceUsers != null){
            System.out.println("Look HERE Workspace" + workspaceUsers.size());
        }
        loadFromFirestore(extras);
        setUpAccentColor(extras);
        setUpMarkAsCompleteButton(extras);
        setUpFAB(extras);
        setUpMessageButton();

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
            floatingActionButton.setVisibility(View.GONE);
            findViewById(R.id.mark_as_complete_card).setVisibility(View.GONE);
            addAssigneeButton.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            // New link
            if (data != null && data.getExtras().getString("url") != null) {
                String url = data.getStringExtra("url");

                urls.add(url);

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Bundle extras = this.getIntent().getExtras();

                String workspaceID = extras.getString("workspaceID");
                String taskID = extras.getString("taskID");

                DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);

                if (urls.size() == 1) {
                    docRef.update("urls", urls);
                } else {
                    docRef.update("urls", FieldValue.arrayUnion(url));
                }

                setUpRecyclerView();
            }
        } else if (requestCode == 200) {
            if (data != null && data.getExtras().getString("title") != null) {
                String taskTitle = data.getStringExtra("title");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                Bundle extras = this.getIntent().getExtras();

                String workspaceID = extras.getString("workspaceID");
                String taskID = extras.getString("taskID");

                DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);

                Map<String, Object> obj = new HashMap<>();

                obj.put("title", taskTitle);
                obj.put("isCompleted", false);

                subtasks.add(obj);

                if (subtasks.size() == 1) {
                    docRef.update("subtasks", subtasks);
                } else {
                    docRef.update("subtasks", FieldValue.arrayUnion(obj));
                }

                setUpRecyclerView();
            }
        } else if (requestCode == 102) {
            if(resultCode == RESULT_OK){
                // GET YOUR USER LIST HERE AND USE IT FOR YOUR PURPOSE.
                assigneeList = data.getStringArrayListExtra("assigneeIdList");
                removedAssigneesList = data.getStringArrayListExtra("assigneesRemoved");
                System.out.println("IN Task acitivity:" + assigneeList.size());
                updateTaskAssignee(extras);
                //loadFromFirestore(extras);
            }
        }

        setUpRecyclerView();
    }

    private void setUpMessageButton() {
        findViewById(R.id.message_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(getApplicationContext(), MessageChatActivity.class);
                messageIntent.putExtra("accentColor", extras.getInt("accentColor"));
                messageIntent.putExtra("workspaceID", extras.getString("workspaceID"));
                messageIntent.putExtra("taskID", extras.getString("taskID"));
                System.out.println("hiiiiii" + extras.getInt("accentColor"));
                System.out.println("hiiiiiiiii" + extras.getString("workspaceID"));
                System.out.println("hiiiiiiiiiiiii" + extras.getString("taskID"));
                startActivity(messageIntent);
            }
        });
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
                taskDueDateTextView.setText((String) documentSnapshot.get("dueDate"));
                taskDescriptionTextView.setText((String) documentSnapshot.get("description"));

                assigneeList = (ArrayList<String>) documentSnapshot.get("assignee");
                if (assigneeList != null){
                    if (assigneeList.size() > 0){
                        System.out.println("assignee list updated: " + assigneeList.size());
                        decodeUsers(assigneeList);
                    }
                    decodeUsers(assigneeList);
                }
                ArrayList<String> unsafeURLs = (ArrayList<String>) documentSnapshot.get("urls");

                if (unsafeURLs == null) {
                    urls = new ArrayList<>();
                } else {
                    urls = unsafeURLs;
                }

                ArrayList<Map<String, Object>> unsafeSubtasks = (ArrayList<Map<String, Object>>) documentSnapshot.get("subtasks");

                if (unsafeSubtasks == null) {
                    subtasks = new ArrayList<>();
                } else {
                    subtasks = unsafeSubtasks;
                }

                setUpRecyclerView();
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


        floatingActionButton.setBackgroundTintList(ColorStateList.valueOf(color));
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

    private void setUpFAB(Bundle extras) {
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(TaskActivity.this, floatingActionButton);
                popupMenu.getMenuInflater().inflate(R.menu.task_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.add_attachments_menu) {
                            Intent newLinkActivity = new Intent(TaskActivity.this, NewLinkActivity.class);

                            newLinkActivity.putExtra("workspaceAccentColor", extras.getInt("accentColor"));

                            startActivityForResult(newLinkActivity, 100);
                        } else {
                            Intent newTaskActivity = new Intent(TaskActivity.this, NewSubtaskActivity.class);

                            newTaskActivity.putExtra("workspaceAccentColor", extras.getInt("accentColor"));

                            startActivityForResult(newTaskActivity, 200);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    TaskDetailAdapter taskDetailAdapter;

    private void setUpRecyclerView() {
        taskDetailAdapter = new TaskDetailAdapter(urls, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        taskDetailRecyclerView.setLayoutManager(layoutManager);
        taskDetailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskDetailRecyclerView.setAdapter(taskDetailAdapter);
    }

    public void toggleMarkAsComplete(int taskIndex) {
        Map<String, Object> subtask = subtasks.get(taskIndex);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Bundle extras = this.getIntent().getExtras();

        String workspaceID = extras.getString("workspaceID");
        String taskID = extras.getString("taskID");

        DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);

        docRef.update("subtasks", FieldValue.arrayRemove(subtask));

        boolean isCompleted = (boolean) subtask.get("isCompleted");
        subtask.put("isCompleted", !isCompleted);

        docRef.update("subtasks", FieldValue.arrayUnion(subtask));

        setUpRecyclerView();
    }
}