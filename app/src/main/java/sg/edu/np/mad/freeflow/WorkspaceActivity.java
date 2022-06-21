package sg.edu.np.mad.freeflow;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WorkspaceActivity extends AppCompatActivity {

    ImageView workspaceImageView;
    TextView workspaceNameTextView;

    TextView todayTaskTextView;
    CardView todayTaskCardView;

    TextView allTasksTextView;
    CardView allTasksCardView;

    TextView forMeTextView;
    CardView forMeCardView;

    TaskFilter taskFilter = TaskFilter.TODAY;
    Bundle extras;

    ImageButton newTaskButton;

    Workspace workspace;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        workspaceImageView = findViewById(R.id.workspace_image);

        todayTaskTextView = findViewById(R.id.today_task_textView);
        todayTaskCardView = findViewById(R.id.category_name_card);

        allTasksTextView = findViewById(R.id.all_task_textView);
        allTasksCardView = findViewById(R.id.all_task);

        forMeTextView = findViewById(R.id.for_me_textView);
        forMeCardView = findViewById(R.id.for_me);

        newTaskButton = findViewById(R.id.new_task_button);

        extras = this.getIntent().getExtras();

        setUpImageView(extras);
        setUpWorkspaceTitle(extras);

        ImageView settingsImageView = findViewById(R.id.settings_image_view);

        ConstraintLayout workspaceActivityHeader = findViewById(R.id.workspace_activity_header);
        workspaceActivityHeader.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        newTaskButton.setColorFilter(getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));

        settingsImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent workspaceSettingsActivity = new Intent(WorkspaceActivity.this, WorkspaceSettingsActivity.class);

                workspaceSettingsActivity.putExtra("workspaceIcon", (Bitmap) extras.getParcelable("workspaceIcon"));
                workspaceSettingsActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                workspaceSettingsActivity.putExtra("workspaceName", extras.getString("workspaceName"));
                workspaceSettingsActivity.putExtra("workspaceID", extras.getString("workspaceID"));
                workspaceSettingsActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));
                workspaceSettingsActivity.putExtra("workspaceUsers", extras.getStringArrayList("workspaceUsers"));
                workspaceSettingsActivity.putExtra("workspaceAdmins", extras.getStringArrayList("workspaceAdmins"));

                startActivity(workspaceSettingsActivity);
            }
        });

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(WorkspaceActivity.this, newTaskButton);
                popupMenu.getMenuInflater().inflate(R.menu.category_task_menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {

                        if (menuItem.getItemId() == R.id.new_task_menu) {
                            Intent newTaskActivity = new Intent(WorkspaceActivity.this, NewTaskActivity.class);

                            newTaskActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                            newTaskActivity.putExtra("workspaceID", extras.getString("workspaceID"));

                            ArrayList<String> categoryNames = new ArrayList<>();

                            for (int i = 0; i < workspace.categories.size(); i++) {
                                categoryNames.add(workspace.categories.get(i).name);
                            }

                            newTaskActivity.putExtra("workspaceCategories", categoryNames);

                            startActivityForResult(newTaskActivity, 10);
                        } else {
                            Intent newCategoryActivity = new Intent(WorkspaceActivity.this, NewCategoryActivity.class);
                            newCategoryActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                            newCategoryActivity.putExtra("workspaceID", extras.getString("workspaceID"));
                            startActivityForResult(newCategoryActivity, 20);
                        }

                        return true;
                    }
                });

                popupMenu.show();
            }
        });

        //User click on "Today"
        todayTaskCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setTaskFilter(TaskFilter.TODAY);
            }
        });

        //User click on "All Tasks"
        allTasksCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                setTaskFilter(TaskFilter.ALL_TASK);
            }
        });
        //User click on "For Me"
        forMeCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                System.out.println("HELLO");
                setTaskFilter(TaskFilter.FOR_ME);
            }
        });

        setTaskFilter(TaskFilter.TODAY);

        loadWorkspace();
    }

    private void loadWorkspace() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String workspaceID = extras.getString("workspaceID");

        DocumentReference docRef = db.collection("workspaces").document(workspaceID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) { ;
                        workspace = new Workspace(document.getData(), workspaceID);
                        loadTasks(docRef);
                    } else {
                        System.out.println("workspace not found");
                    }
                } else {
                    System.out.println("error while getting workspace");
                }
            }
        });
    }

    private void loadTasks(DocumentReference docRef) {
        docRef.collection("categories").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();

                    for (int i = 0; i < documentSnapshots.size(); i++) {
                        DocumentSnapshot snapshot = documentSnapshots.get(i);
                        Map<String, Object> snapshotData = snapshot.getData();

                        String name = (String) snapshotData.get("name");
                        ArrayList<String> subtasks = (ArrayList<String>) snapshotData.get("subtasks");

                        System.out.println(name);
                        workspace.categories.add(new Category(name, subtasks));
                    }

                    setUpRecyclerView();
                } else {
                    System.out.println("error while getting workspace");
                }
            }
        });
    }

    private void setTaskFilter(TaskFilter newValue) {
        switch (taskFilter) {
            case TODAY:
                setInactiveTab(todayTaskTextView, todayTaskCardView);
                break;
            case ALL_TASK:
                setInactiveTab(allTasksTextView, allTasksCardView);
                break;
            case FOR_ME:
                setInactiveTab(forMeTextView, forMeCardView);
                break;
        }

        switch (newValue) {
            case TODAY:
                setActiveTab(todayTaskTextView, todayTaskCardView);
                break;
            case ALL_TASK:
                setActiveTab(allTasksTextView, allTasksCardView);
                break;
            case FOR_ME:
                setActiveTab(forMeTextView, forMeCardView);
                break;
        }

        taskFilter = newValue;
    }

    private void setActiveTab(TextView textview, CardView cardview) {
        textview.setTextColor(getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));
        textview.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
        cardview.setCardBackgroundColor(getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));
    }

    private void setInactiveTab(TextView textview, CardView cardview) {
        textview.setTextColor(Color.BLACK);
        textview.setBackgroundColor(Color.TRANSPARENT);
        cardview.setCardBackgroundColor(getResources().getColor(R.color.white));
    }

    private void setUpImageView(Bundle extras) {
        Bitmap image = extras.getParcelable("workspaceIcon");

        if (image != null) {
            workspaceImageView.setImageBitmap(image);
        } else {
            workspaceImageView.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.workspace_icon));
        }

        workspaceImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setUpWorkspaceTitle(Bundle extras) {
        workspaceNameTextView = findViewById(R.id.workspace_name_show);
        String workspaceName = extras.getString("workspaceName");

        workspaceNameTextView.setText(workspaceName);
    }

    private void setUpRecyclerView() {
        RecyclerView tasksRecyclerView = findViewById(R.id.tasks_recycler_view);
        RecyclerView.Adapter mAdapter = new WorkspaceTasksAdapter(workspace, this);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(
                this,
                LinearLayoutManager.VERTICAL,
                false);

        tasksRecyclerView.setLayoutManager(mLayoutManager);
        tasksRecyclerView.setItemAnimator(new DefaultItemAnimator());
        tasksRecyclerView.setAdapter(mAdapter);
    }

    enum TaskFilter {
        TODAY,
        ALL_TASK,
        FOR_ME
    }
}