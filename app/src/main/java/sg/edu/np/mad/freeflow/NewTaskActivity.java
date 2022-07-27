package sg.edu.np.mad.freeflow;

import static sg.edu.np.mad.freeflow.NewTaskDueDateActivity.CHOSEN_DUE_DATE;
import static sg.edu.np.mad.freeflow.NewTaskDueDateActivity.CHOSEN_DUE_TIME;
import static sg.edu.np.mad.freeflow.NewTaskDueDateActivity.REQUEST_CODE;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
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
import java.util.Calendar;


@RequiresApi(api = Build.VERSION_CODES.O)
public class NewTaskActivity extends AppCompatActivity {

    Spinner categorySpinner;
    private DatePickerDialog datePickerDialog;
    private Button dueDateButton;
    Button createButton;
    EditText taskNameEditText;
    EditText taskDescriptionEditText;

    String selectedItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        categorySpinner = findViewById(R.id.category_spinner);
        dueDateButton = findViewById(R.id.due_date_picker);
        createButton = findViewById(R.id.create_button);
        taskNameEditText = findViewById(R.id.task_title_edit_text);
        taskDescriptionEditText = findViewById(R.id.task_description_edit_text);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> categories = extras.getStringArrayList("workspaceCategories");

        initDueDatePicker();

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

        setUpAccentColor(Workspace.colors[extras.getInt("workspaceAccentColor", 0)]);

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
                String dueDate = dueDateButton.getText().toString();
                String description = taskDescriptionEditText.getText().toString();

                Task newTask = new Task(title, dueDate, description);

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

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundResource(colorResource);

        CardView taskTitleCard = findViewById(R.id.task_title_card);
        taskTitleCard.setCardBackgroundColor(getResources().getColor(colorResource));

        CardView taskDescriptionCard = findViewById(R.id.task_description_card);
        taskDescriptionCard.setCardBackgroundColor(getResources().getColor(colorResource));

        CardView taskCategoryCard = findViewById(R.id.task_category_card);
        taskCategoryCard.setCardBackgroundColor(getResources().getColor(colorResource));

        createButton.setBackgroundResource(colorResource);
    }

    private void initDueDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String dueDate = makeDateString(day, month, year);
                dueDateButton.setText(dueDate);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
    }

    private String getTodayDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private String makeDateString(int day, int month, int year) {
        return day + " " + getMonthFormat(month) + " " + year;
    }

    private String hyphenDateString(int day, int month, int year) {
        return day + "-" + getMonthFormat(month) + "-" + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "January";
        if (month == 2)
            return "February";
        if (month == 3)
            return "March";
        if (month == 4)
            return "April";
        if (month == 5)
            return "May";
        if (month == 6)
            return "June";
        if (month == 7)
            return "July";
        if (month == 8)
            return "August";
        if (month == 9)
            return "September";
        if (month == 10)
            return "October";
        if (month == 11)
            return "November";
        if (month == 12)
            return "December";

        return "January";
    }

    public void dueDatePicker(View view) {
        final int accentColor = Workspace.colors[
                this.getIntent()
                        .getExtras()
                        .getInt("workspaceAccentColor", 0)
                ];

        NewTaskDueDateActivity.startActivityForResult(this, accentColor);
    }

    public void onDateSet(View view) {
    }

    /**
     * Call static method to start activity and request the activity to return a result
     *
     * @param requestCode Request code
     * @param resultCode  Result code
     * @param data        Intent containing the data return from the activity
     */
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (REQUEST_CODE == requestCode) {
            final String dueDate = data.getStringExtra(CHOSEN_DUE_DATE);
            final String dueTime = data.getStringExtra(CHOSEN_DUE_TIME);

            dueDateButton.setText(String.format("%s %s", dueDate, dueTime));
        }
    }
}