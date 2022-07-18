package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        Bundle extras = this.getIntent().getExtras();
        loadFromFirestore(extras);
        setUpAccentColor(extras);
        setUpMarkAsCompleteButton(extras);

        if (extras.getString("categoryName") == null) {
            markAsCompleteButton.setVisibility(View.GONE);
            findViewById(R.id.mark_as_complete_card).setVisibility(View.GONE);
        }

        findViewById(R.id.message_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent messageIntent = new Intent(getApplicationContext(), MessageChatActivity.class);
                messageIntent.putExtra("accentColor", extras.getInt("accentColor"));
                messageIntent.putExtra("workspaceID", extras.getString("workspaceID"));
                messageIntent.putExtra("taskID", extras.getString("taskID"));
                System.out.println("hiiiiii" + extras.getInt("accentColor"));
                System.out.println("hiiiiiiiii" + extras.getInt("workspaceID"));
                System.out.println("hiiiiiiiiiiiii" + extras.getInt("taskID"));
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
                taskDescriptionTextView.setText((String) documentSnapshot.get("description"));
            }
        });
    }

    private void setUpAccentColor(Bundle extras) {
        int color = extras.getInt("accentColor");
        findViewById(R.id.task_header).setBackgroundColor(color);

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