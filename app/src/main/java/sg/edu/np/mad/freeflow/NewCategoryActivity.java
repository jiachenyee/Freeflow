package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class NewCategoryActivity extends AppCompatActivity {

    ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_category);

        closeButton = findViewById(R.id.close_button);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Bundle extras = this.getIntent().getExtras();

        Button doneButton = findViewById(R.id.done_button);
        doneButton.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        LinearLayout headerView = findViewById(R.id.header_view);
        headerView.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);

        CardView categoryNameCard = findViewById(R.id.category_name_card);
        categoryNameCard.setBackgroundResource(Workspace.colors[extras.getInt("workspaceAccentColor",0)]);
    }
}