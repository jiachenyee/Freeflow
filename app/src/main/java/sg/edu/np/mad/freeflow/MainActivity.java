package sg.edu.np.mad.freeflow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    TextView usernameTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        usernameTextView = findViewById(R.id.username_text_view);
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

            usernameTextView.setText("Hello " + user.getDisplayName() + "!");
        }
    }
}