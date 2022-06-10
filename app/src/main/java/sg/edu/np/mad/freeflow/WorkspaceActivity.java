package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        workspaceImageView = findViewById(R.id.workspace_image);

        todayTaskTextView = findViewById(R.id.today_task_textView);
        todayTaskCardView = findViewById(R.id.today_task);

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
                workspaceSettingsActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));
                workspaceSettingsActivity.putExtra("workspaceUsers", extras.getStringArrayList("workspaceUsers"));
                workspaceSettingsActivity.putExtra("workspaceAdmins", extras.getStringArrayList("workspaceAdmins"));

                startActivity(workspaceSettingsActivity);
            }
        });

        newTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent newTaskActivity = new Intent(WorkspaceActivity.this, NewTaskActivity.class);

                startActivityForResult(newTaskActivity, 20);
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

    enum TaskFilter {
        TODAY,
        ALL_TASK,
        FOR_ME
    }
}