package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class NewSubtaskActivity extends AppCompatActivity {

    Button createButton;
    EditText taskTitleEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_subtask);

        taskTitleEditText = findViewById(R.id.task_title_edit_text);
        createButton = findViewById(R.id.create_button);

        Bundle extras = getIntent().getExtras();

        setupAccentColor(extras);
    }

    private void setupAccentColor(Bundle extras) {
        int color = extras.getInt("workspaceAccentColor");

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundColor(color);
        createButton.setBackgroundColor(color);

        findViewById(R.id.task_card).setBackgroundColor(color);
    }
}