package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

public class NewLinkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_link);

        Bundle extras = getIntent().getExtras();

        int color = extras.getInt("workspaceAccentColor");

        LinearLayout headerView = findViewById(R.id.header_view);

        headerView.setBackgroundColor(color);

    }
}