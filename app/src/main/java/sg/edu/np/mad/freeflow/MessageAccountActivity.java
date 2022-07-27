package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
        setContentView(R.layout.activity_message_account);

        ArrayList<User> userArrayList = new ArrayList<>();

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

        String userIDD = accountActivity.getStringExtra("userIDD");
        System.out.println("..." + userIDD);
        /*
        String userName = accountActivity.getStringExtra("userName");
        String userEmail = accountActivity.getStringExtra("userEmail");
        String userPicture = accountActivity.getStringExtra("userPicture");

         */

        /*
        FirebaseFirestore.getInstance().collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                else{
                    userArrayList.clear();
                }

                for (QueryDocumentSnapshot document : value) {
                    User aUser = new User();
                    aUser.userID = document.getId();
                    Map<String, Object> userMap = document.getData();
                    aUser.name = userMap.get("name").toString();
                    aUser.emailAddress = userMap.get("emailAddress").toString();
                    aUser.profilePictureURL = userMap.get("profilePictureURL").toString();
                    userArrayList.add(aUser);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    System.out.println("Done4");
                }
            }
        });

         */


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
                            System.out.println("hi its me: " + userArrayList);
                            User userAccount = new User();
                            for (int user = 0; user < userArrayList.size(); user++){
                                if (userArrayList.get(user).userID.equals(userIDD)){
                                    userAccount = userArrayList.get(user);
                                }
                            }


                            System.out.println("NAMEEEEE: " + userAccount.name);
                            System.out.println("EMAILLLLLLL: " + userAccount.emailAddress);
                            String[] nameString = userAccount.name.split(" ");

                            accountTitle.setText(nameString[0] + "'s Account");
                            userNameTextView.setText(userAccount.name);
                            userEmailTextView.setText(userAccount.emailAddress);
                            Picasso.with(MessageAccountActivity.this).load(Uri.parse(userAccount.profilePictureURL)).into(userProfileImageView);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });





        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}