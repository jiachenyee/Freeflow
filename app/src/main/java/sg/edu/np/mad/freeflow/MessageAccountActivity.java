package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.transition.Explode;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MessageAccountActivity extends AppCompatActivity {

    TextView userNameTextView;
    TextView userEmailTextView;
    TextView accountTitle;
    ImageView userProfileImageView;
    ImageButton closeButton;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setAnimation();
        setContentView(R.layout.activity_message_account);

        //initialising the variables
        ArrayList<User> userArrayList = new ArrayList<>();
        userNameTextView = findViewById(R.id.msg_account_name_text_view);
        userProfileImageView = findViewById(R.id.msg_account_profilepic);
        userEmailTextView = findViewById(R.id.msg_account_email_text_view);
        closeButton = findViewById(R.id.msg_account_close_button);
        accountTitle = findViewById(R.id.msg_account_title);

        //getting data from Intent
        Intent accountActivity = getIntent();
        Bundle extras = this.getIntent().getExtras();
        String userIDD = accountActivity.getStringExtra("userIDD");

        //retrieving the current list of users from FireStore
        db.collection("users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Hi Jin Daat");
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User aUser = new User();
                                aUser.userID = document.getId();
                                Map<String, Object> userMap = document.getData();
                                aUser.name = userMap.get("name").toString();
                                aUser.emailAddress = userMap.get("emailAddress").toString();
                                aUser.profilePictureURL = userMap.get("profilePictureURL").toString();
                                userArrayList.add(aUser);
                                Log.d(TAG, document.getId() + " => " + document.getData());

                            }
                            User userAccount = new User();
                            for (int user = 0; user < userArrayList.size(); user++){
                                if (userArrayList.get(user).userID.equals(userIDD)){
                                    userAccount = userArrayList.get(user);
                                }
                            }

                            //truncate the user name for the header
                            String[] nameString = userAccount.name.split(" ");
                            accountTitle.setText(nameString[0] + "'s Account");
                            //setting up the user data
                            userNameTextView.setText(userAccount.name);
                            userEmailTextView.setText(userAccount.emailAddress);
                            //setting up the user profile pic
                            Picasso.with(MessageAccountActivity.this).load(Uri.parse(userAccount.profilePictureURL)).into(userProfileImageView);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        //ending the activity after closing it
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    //add animation from one activity to another
    public void setAnimation(){
        if(Build.VERSION.SDK_INT>20) {
            Explode explode = new Explode();
            explode.setDuration(1500);
            explode.setInterpolator(new DecelerateInterpolator());
            getWindow().setExitTransition(explode);
            getWindow().setEnterTransition(explode);
        }
    }
}