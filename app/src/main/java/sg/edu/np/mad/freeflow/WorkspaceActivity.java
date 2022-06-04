package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workspace);

        workspaceImageView = findViewById(R.id.workspace_image);

        todayTaskTextView = findViewById(R.id.today_task_textView);
        todayTaskCardView = findViewById(R.id.today_task);

        allTasksTextView = findViewById(R.id.all_task_textView);
        todayTaskCardView = findViewById(R.id.all_task);

        forMeTextView = findViewById(R.id.for_me_textView);
        todayTaskCardView = findViewById(R.id.for_me);

        Bundle extras = this.getIntent().getExtras();

        setUpImageView(extras);
        setUpWorkspaceTitle(extras);

        ImageView settingsImageView = findViewById(R.id.settings_image_view);

        ConstraintLayout workspaceActivityHeader = findViewById(R.id.workspace_activity_header);
        workspaceActivityHeader.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

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

        //User click on "Today"
        todayTaskCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){ changeActiveTab(todayTaskTextView, todayTaskCardView); }
        });

        //User click on "All Tasks"
        allTasksCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                changeActiveTab(allTasksTextView, allTasksCardView);
            }
        });
        //User click on "For Me"
        forMeCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                changeActiveTab(forMeTextView, forMeCardView);
            }
        });
    }

    private void changeActiveTab(TextView textview, CardView cardview){
        if(textview.getCurrentTextColor() == 0xFF6746){
            textview.setTextColor(Color.BLACK);
            textview.setBackgroundColor(Color.TRANSPARENT);
            cardview.setCardBackgroundColor(Color.TRANSPARENT);
        }
        else{
            textview.setTextColor(Color.parseColor("#FF6746"));
            textview.setBackgroundColor(Color.parseColor("#CCFFFFFF"));
            cardview.setCardBackgroundColor(Color.parseColor("#FF6746"));
        }
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

    private void setUpWorkspaceAccentColor(Bundle extras) {
        String workspaceName = extras.getString("workspaceName");

        workspaceNameTextView.setText(workspaceName);
    }
}