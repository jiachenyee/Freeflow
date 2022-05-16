package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    TextView usernameTextView;
    ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        usernameTextView = findViewById(R.id.username_text_view);
        profileImageView = findViewById(R.id.profile_image_view);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // If the user is not signed in, get the user to sign in
        if (mAuth.getCurrentUser() == null) {
            Intent signInActivity = new Intent(MainActivity.this, SignInActivity.class);

            startActivity(signInActivity);
        } else {
            user = mAuth.getCurrentUser();
            setUpUser();
        }
    }

    private void setUpUser() {
        usernameTextView.setText("Hello " + user.getDisplayName() + "!");

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