package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TaskActivity extends AppCompatActivity {

    TextView taskTitleTextView;
    TextView taskDescriptionTextView;

    Button markAsCompleteButton;
    FloatingActionButton floatingActionButton;

    ArrayList<String> urls = new ArrayList<>();

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
        floatingActionButton = findViewById(R.id.floating_action_button);

        Bundle extras = this.getIntent().getExtras();
        loadFromFirestore(extras);
        setUpAccentColor(extras);
        setUpMarkAsCompleteButton(extras);
        setUpFAB(extras);

        if (extras.getString("categoryName") == null) {
            markAsCompleteButton.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            findViewById(R.id.mark_as_complete_card).setVisibility(View.GONE);
        }

        setUpRecyclerView();
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

                ArrayList<String> unsafeURLs = (ArrayList<String>) documentSnapshot.get("urls");

                if (unsafeURLs == null) {
                    urls = new ArrayList<>();
                } else {
                    urls = unsafeURLs;
                }
            }
        });
    }

    private void setUpAccentColor(Bundle extras) {
        int color = extras.getInt("accentColor");
        findViewById(R.id.task_header).setBackgroundColor(color);

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
                            Intent newTaskActivity = new Intent(TaskActivity.this, NewTaskActivity.class);

                            // TODO: Pass in required intents

                            startActivity(newTaskActivity);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });
    }

    private void setUpRecyclerView() {
        TaskDetailAdapter taskDetailAdapter = new TaskDetailAdapter(urls, this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        RecyclerView taskDetailRecyclerView = findViewById(R.id.task_detail_recycler_view);

        taskDetailRecyclerView.setLayoutManager(layoutManager);
        taskDetailRecyclerView.setItemAnimator(new DefaultItemAnimator());
        taskDetailRecyclerView.setAdapter(taskDetailAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            // New link
            if (data != null && data.getExtras().getString("url") != null) {
                urls.add(data.getStringExtra("url"));

            }
        }
    }
}