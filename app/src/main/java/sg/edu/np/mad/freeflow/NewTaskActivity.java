package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {

    Spinner categorySpinner;
    Button createButton;
    EditText taskNameEditText;
    EditText taskDescriptionEditText;

    ImageButton addAssigneeButton;

    String selectedItem;

    ArrayList<String> assigneeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        categorySpinner = findViewById(R.id.category_spinner);
        createButton = findViewById(R.id.create_button);
        taskNameEditText = findViewById(R.id.task_title_edit_text);
        taskDescriptionEditText = findViewById(R.id.task_description_edit_text);

        addAssigneeButton = findViewById(R.id.add_assignee2);


        Bundle extras = getIntent().getExtras();
        ArrayList<String> categories = extras.getStringArrayList("workspaceCategories");
        //ArrayList<String> assigneeIdList = extras.getStringArrayList("assigneeIdList");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);

        findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        setUpAccentColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedItem = (String) adapterView.getItemAtPosition(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        addAssigneeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent manageAssigneeActivity = new Intent(NewTaskActivity.this, ManageAssigneeActivity.class);
                manageAssigneeActivity.putExtra("workspaceUsers", extras.getStringArrayList("workspaceAdmins"));
                manageAssigneeActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor",0));
                manageAssigneeActivity.putExtra("assigneeList", assigneeList);
                //manageAssigneeActivity.putExtra("workspaceCategories" ,extras.getStringArrayList("workspaceCategories"));
                startActivityForResult(manageAssigneeActivity, 101);
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = taskNameEditText.getText().toString();
                String description = taskDescriptionEditText.getText().toString();

                Task newTask = new Task(title, description, assigneeList);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                String workspaceID = extras.getString("workspaceID");

                if (selectedItem == null) {
                    Toast.makeText(getApplicationContext(), "Select a category", Toast.LENGTH_LONG).show();
                    return;
                }

                if (title.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Task needs a title", Toast.LENGTH_LONG).show();
                    return;
                }

                if (assigneeList.size() == 0){
                    Toast.makeText(getApplicationContext(), "Add an assignee to this task", Toast.LENGTH_LONG).show();
                    return;
                }

                db.collection("workspaces")
                        .document(workspaceID)
                        .collection("tasks")
                        .add(newTask.toMap())
                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                            @Override
                            public void onSuccess(DocumentReference documentReference) {
                                String id = documentReference.getId();

                                db.collection("workspaces")
                                        .document(workspaceID)
                                        .collection("categories")
                                        .document(selectedItem)
                                        .update("subtasks", FieldValue.arrayUnion(id))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(getApplicationContext(), "Task created successfully", Toast.LENGTH_LONG).show();
                                        finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(getApplicationContext(), "Failed to create task", Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to create task", Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if(resultCode == RESULT_OK){
                // GET YOUR USER LIST HERE AND USE IT FOR YOUR PURPOSE.
                assigneeList = data.getStringArrayListExtra("assigneeIdList");

                decodeWorkSpaceUsers(assigneeList);
            }
        }
    }

    private void decodeWorkSpaceUsers(ArrayList<String> userIdList){
        ArrayList<User> decodedUsers = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (userIdList.size() > 0){
            for (String userId : userIdList){
                DocumentReference docRef = db.collection("users").document(userId);
                docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) { ;
                                User user = new User(document.getData(), userId);
                                decodedUsers.add(user);
                                if (decodedUsers.size() == userIdList.size()){
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
        AssigneeAdapter adapter = new AssigneeAdapter(decodedUsers, NewTaskActivity.this);
        LinearLayoutManager layout = new LinearLayoutManager(NewTaskActivity.this,
                LinearLayoutManager.HORIZONTAL,
                false);

        rv.setLayoutManager(layout);
        rv.setAdapter(adapter);

    }

    private void setUpAccentColor(int colorResource) {

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundResource(colorResource);

        CardView taskTitleCard = findViewById(R.id.task_title_card);
        taskTitleCard.setCardBackgroundColor(getResources().getColor(colorResource));

        CardView taskDescriptionCard = findViewById(R.id.task_description_card);
        taskDescriptionCard.setCardBackgroundColor(getResources().getColor(colorResource));

        CardView taskCategoryCard = findViewById(R.id.task_category_card);
        taskCategoryCard.setCardBackgroundColor(getResources().getColor(colorResource));

        addAssigneeButton.setColorFilter(getResources().getColor(colorResource));

        createButton.setBackgroundResource(colorResource);
    }
}