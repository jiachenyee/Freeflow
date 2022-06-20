package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

public class NewTaskActivity extends AppCompatActivity {

    Spinner categorySpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);

        categorySpinner = findViewById(R.id.category_spinner);

//        newTaskActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
//        newTaskActivity.putExtra("workspaceID", extras.getString("workspaceID"));
//        newTaskActivity.putExtra("workspaceCategories", workspace.categories);

        Bundle extras = getIntent().getExtras();
        ArrayList<String> categories = extras.getStringArrayList("workspaceCategories");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        adapter.setDropDownViewResource(android.R.layout
                .simple_spinner_dropdown_item);

        categorySpinner.setAdapter(adapter);
    }
}