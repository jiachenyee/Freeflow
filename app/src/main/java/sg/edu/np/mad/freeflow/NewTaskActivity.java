package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {

    Spinner categorySpinner;
    Button createButton;
    EditText taskNameEditText;
    EditText taskDescriptionEditText;

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

        setUpAccentColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = taskNameEditText.getText().toString();
                String description = taskDescriptionEditText.getText().toString();

                Task newTask = new Task(title, description);
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
}