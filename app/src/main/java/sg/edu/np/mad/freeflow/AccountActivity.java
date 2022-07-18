package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class AccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    // Grab the UI elements from the view.
    TextView usernameTextView;
    TextView emailTextView;
    ImageView profileImageView;
    ImageButton closeButton;
    Button logOutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // Get the user and auth info from Firebase
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        // Grab the text views from the XML.
        usernameTextView = findViewById(R.id.name_text_view);
        profileImageView = findViewById(R.id.their_message_profilepic);
        emailTextView = findViewById(R.id.email_text_view);

        closeButton = findViewById(R.id.close_button);
        logOutButton = findViewById(R.id.log_out_button);

        // Setup close button to dismiss view on click
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        // Setup log out button to sign user out and dismiss view
        logOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        setUpUser();
    }

    // Display personalised content for each user
    private void setUpUser() {
        usernameTextView.setText(user.getDisplayName());
        emailTextView.setText(user.getEmail());
        // Add profile picture
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(user.getPhotoUrl().toString());

                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());

                    runOnUiThread(()->{
                        profileImageView.setImageBitmap(bmp);
                    });
                    System.out.println("success");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}