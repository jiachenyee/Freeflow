package sg.edu.np.mad.freeflow;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    TextView usernameTextView;
    ImageView profileImageView;

    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        usernameTextView = findViewById(R.id.username_text_view);
        profileImageView = findViewById(R.id.profile_image_view);


        db = FirebaseFirestore.getInstance();

        // When the user clicks the profile image, show the account activity
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent accountActivity = new Intent(MainActivity.this, AccountActivity.class);
                startActivity(accountActivity);
            }
        });

        // This is for testing, set up empty state
        setUpEmptyState();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() == null) {
            // The user is not signed in, show sign in interface
            Intent signInActivity = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(signInActivity);
        } else {
            // User is signed in, set `user` to the current user
            user = mAuth.getCurrentUser();

            // Set up the interface for the user
            setUpUser();
        }
    }

    // Set up username and profile picture
    private void setUpUser() {
        usernameTextView.setText("Hello " + user.getDisplayName() + "!");

        // Add profile picture, make async HTTP request to download pfp
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

    // Create empty state if the user does not currently have a workspace.
    private void setUpEmptyState() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_fragment, new HomeEmptyStateFragment(this));
        ft.commit();
    }
}