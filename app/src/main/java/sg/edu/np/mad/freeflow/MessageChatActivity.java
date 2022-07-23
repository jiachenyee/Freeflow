package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MessageChatActivity extends AppCompatActivity {

    ImageButton closeButton;
    EditText messageEditText;
    ImageButton sendButton;

    private FirebaseAuth mAuth;
    private FirebaseUser user;

    FirebaseFirestore db;

    private Calendar calendar;
    private SimpleDateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private Date date;
    private Time time;

    private RecyclerView chatRecyclerView;
    private MessageChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        Intent messageIntent = getIntent();

        closeButton = findViewById(R.id.close_button);
        sendButton = findViewById(R.id.send_button);
        messageEditText = findViewById(R.id.edit_message);

        ArrayList<Message> messagesList = new ArrayList<Message>();
        ArrayList<User> usersList = new ArrayList<User>();

        Bundle extras = this.getIntent().getExtras();
        if (extras == null) { System.out.println("invalid");}

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String workspaceID = extras.getString("workspaceID");
        String taskID = extras.getString("taskID");
        System.out.println("TaskID: " + taskID);
        System.out.println("WorkspaceID: " + workspaceID);
        int color = extras.getInt("accentColor");
        findViewById(R.id.msg_header_view).setBackgroundColor(color);

        //setting up current User field
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        String currentId = user.getUid();


        //getting data from cloud Firestore
        db.collection("workspaces")
                .document(workspaceID)
                .collection("tasks")
                .document(taskID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                //Map<String, Object> documentData = document.getData();
                                ArrayList<Map<String, Object>> retrieveArrayList  = (ArrayList<Map<String, Object>>) document.get("messages");
                                if (retrieveArrayList != null){
                                    for (int mes = 0; mes < retrieveArrayList.size(); mes++){
                                        Map<String, Object> retrievedMsg = retrieveArrayList.get(mes);
                                        Message msgDetails = new Message();
                                        msgDetails.msgContent = retrievedMsg.get("MsgContent").toString();
                                        msgDetails.msgUserID = retrievedMsg.get("MsgUserId").toString();
                                        msgDetails.msgTimeStamp = Long.parseLong(retrievedMsg.get("MsgTimeStamp").toString());
                                        System.out.println("YAY: " + msgDetails.msgContent);
                                        messagesList.add(msgDetails);
                                    }
                                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        //getting the list of users
        db.collection("users").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        User allUsers = new User();
                        allUsers.userID = document.getId();
                        Map<String, Object> userMap = document.getData();
                        allUsers.name = userMap.get("name").toString();
                        allUsers.emailAddress = userMap.get("emailAddress").toString();
                        allUsers.profilePictureURL = userMap.get("profilePictureURL").toString();
                        usersList.add(allUsers);
                        Log.d(TAG, document.getId() + " => " + document.getData());
                    }
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        //display data in recycler view
        setUpRecyclerView(messagesList, usersList, currentId, color);

        //ending the activity after close button is clicked
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        //adding data into the message list which is sent to the Adapter
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (messageEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No message inputted", Toast.LENGTH_LONG).show();
                    return;
                }


                Message newMsg = new Message();
                newMsg.msgContent = messageEditText.getText().toString();
                newMsg.msgUserID = currentId;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                newMsg.msgTimeStamp = timestamp.getTime();

                messagesList.add(newMsg);
                messageEditText.setText("");

                Map<String, Object> messageData = new HashMap<>();
                Map<String, Object> eachMessaging = newMsg.toMap();

                messageData.put("messages", Arrays.asList(eachMessaging));


                db.collection("workspaces")
                        .document(workspaceID)
                        .collection("tasks")
                        .document(taskID)
                        .update("messages", FieldValue.arrayUnion(eachMessaging))
                        .addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void unused) {
                                System.out.println(workspaceID);
                                System.out.println(taskID);
                                Toast.makeText(getApplicationContext(), "Message added successfully", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to send message", Toast.LENGTH_LONG).show();
                            }
                        });

                for(int i = 0; i < messagesList.size(); i++){
                    System.out.println("workspaceID" + messagesList.get(i).msgUserID);
                    System.out.println("content" + messagesList.get(i).msgContent);
                }

                setUpRecyclerView(messagesList, usersList, currentId, color);
            }
        });
    }



    protected void setUpRecyclerView(ArrayList<Message> msgList, ArrayList<User> usersList, String currentId, int color){
        new Thread(new Runnable() {
            @Override
            public void run() {
                chatRecyclerView = findViewById(R.id.message_recycler_view);
                chatRecyclerView.setHasFixedSize(true);
                chatAdapter = new MessageChatAdapter(msgList, usersList, currentId, color, MessageChatActivity.this);
                chatRecyclerView.setLayoutManager(new LinearLayoutManager(MessageChatActivity.this));
                chatRecyclerView.scrollToPosition(msgList.size()-1);
                chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
                chatRecyclerView.setAdapter(chatAdapter);
            }
        }).start();
    }
}