package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {

    Spinner categorySpinner;
    Button createButton;
    EditText taskNameEditText;
    EditText taskDescriptionEditText;

    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        categorySpinner = findViewById(R.id.category_spinner);
        createButton = findViewById(R.id.create_button);
        taskNameEditText = findViewById(R.id.task_title_edit_text);
        taskDescriptionEditText = findViewById(R.id.task_description_edit_text);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> categories = extras.getStringArrayList("workspaceCategories");

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

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = taskNameEditText.getText().toString();
                String description = taskDescriptionEditText.getText().toString();

                Task newTask = new Task(title, description);

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

    private void setUpAccentColor(int colorResource) {

        LinearLayout headerView = findViewById(R.id.msg_header_view);

        headerView.setBackgroundResource(colorResource);

        CardView taskTitleCard = findViewById(R.id.task_title_card);
        taskTitleCard.setCardBackgroundColor(getResources().getColor(colorResource));

        CardView taskDescriptionCard = findViewById(R.id.task_description_card);
        taskDescriptionCard.setCardBackgroundColor(getResources().getColor(colorResource));

        CardView taskCategoryCard = findViewById(R.id.task_category_card);
        taskCategoryCard.setCardBackgroundColor(getResources().getColor(colorResource));

        createButton.setBackgroundResource(colorResource);
    }
}