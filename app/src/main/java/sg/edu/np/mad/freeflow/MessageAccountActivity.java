package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAccountActivity extends AppCompatActivity {

    TextView userNameTextView;
    TextView userEmailTextView;
    TextView accountTitle;
    ImageView userProfileImageView;
    ImageButton closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_account);

        userNameTextView = findViewById(R.id.msg_account_name_text_view);
        userProfileImageView = findViewById(R.id.msg_account_profilepic);
        userEmailTextView = findViewById(R.id.msg_account_email_text_view);
        closeButton = findViewById(R.id.msg_account_close_button);
        accountTitle = findViewById(R.id.msg_account_title);

        Intent accountActivity = getIntent();
        Bundle extras = this.getIntent().getExtras();

        /*
        if (extras != null){
            if (extras.containsKey("userID")){
                String userID = extras.getString("userID");
                String userName = extras.getString("userName");
                String userEmail = extras.getString("userEmail");
                String userPicture = extras.getString("userPicture");
                System.out.println("NAMEEEEE: " + userName);
                System.out.println("EMAILLLLLLL: " + userEmail);
                String[] nameString = userName.split(" ");

                accountTitle.setText(nameString[0] + "'s Account");
                userNameTextView.setText(userName);
                userEmailTextView.setText(userEmail);
                Picasso.with(this).load(Uri.parse(userPicture)).into(userProfileImageView);
            }

        }

         */

        String userID = accountActivity.getStringExtra("userID");
        String userName = accountActivity.getStringExtra("userName");
        String userEmail = accountActivity.getStringExtra("userEmail");
        String userPicture = accountActivity.getStringExtra("userPicture");
        System.out.println("NAMEEEEE: " + userName);
        System.out.println("EMAILLLLLLL: " + userEmail);
        String[] nameString = userName.split(" ");

        accountTitle.setText(nameString[0] + "'s Account");
        userNameTextView.setText(userName);
        userEmailTextView.setText(userEmail);
        Picasso.with(this).load(Uri.parse(userPicture)).into(userProfileImageView);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}