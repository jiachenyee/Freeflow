package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MessageChat extends AppCompatActivity {

    ImageButton closeButton;
    EditText messageEditText;
    ImageButton sendButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        Intent messageIntent = getIntent();

        closeButton = findViewById(R.id.close_button);
        sendButton = findViewById(R.id.send_button);
        messageEditText = findViewById(R.id.edit_message);

        Bundle extras = this.getIntent().getExtras();
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        LinearLayout headerView = findViewById(R.id.header_view);
        //headerView.setBackgroundResource(Workspace.colors[extras.getInt("accentColor",0)]);

    }
}