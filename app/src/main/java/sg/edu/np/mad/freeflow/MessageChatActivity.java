package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    //variables for buttons and other resources
    ImageButton closeButton;
    EditText messageEditText;
    ImageButton sendButton;
    TextView messageTitle;

    //variables for firebase/firestore connection
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RecyclerView chatRecyclerView;
    private MessageChatAdapter chatAdapter;

    FirebaseFirestore db;

    //array list used for populating the messages from Firestore
    ArrayList<Message> messagesList = new ArrayList<>();
    //array list used for populating the users from Firestore
    ArrayList<User> usersList = new ArrayList<>();

    //variables used for implementing the recycler view and adapter
    Boolean sentNotification;
    String taskID;
    String workspaceID;
    String currentId;
    int color;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_chat);

        //get Intent from Task Activity
        Intent messageIntent = getIntent();

        //Initialise to the resource file elements
        closeButton = findViewById(R.id.close_button);
        sendButton = findViewById(R.id.send_button);
        messageEditText = findViewById(R.id.edit_message);
        messageTitle = findViewById(R.id.message_title);

        //getting memory from Bundle
        Bundle extras = this.getIntent().getExtras();
        if (extras == null) { System.out.println("invalid");}

        //setting up current User field
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        currentId = user.getUid();
        db = FirebaseFirestore.getInstance();

        //getting data from Bundle
        workspaceID = extras.getString("workspaceID");
        taskID = extras.getString("taskID");
        color = extras.getInt("accentColor");
        //setting background of task header to the workspace colour
        findViewById(R.id.msg_header_view).setBackgroundColor(color);

        //notification will be send by default unless user has interacted in the chat
        sentNotification = true;


        /* ----------------------------------------------------------------- */
        /* ----------------   GETTING DATA FROM FIRESTORE   ---------------- */
        /* ----------------------------------------------------------------- */

        //display of task title in the header from FireStore
        db.collection("workspaces")
                .document(workspaceID)
                .collection("tasks")
                .document(taskID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                messageTitle.setText(documentSnapshot.get("title").toString());
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });



        //getting users from FireStore based on realtime updates
        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }
                else{
                    //prevent duplication of userList
                    usersList.clear();
                }
                //Loop every document to get all user details
                for (QueryDocumentSnapshot document : value) {
                    User aUser = new User();
                    aUser.userID = document.getId(); //userID is based on document ID
                    Map<String, Object> userMap = document.getData();
                    //filling in the user properties
                    aUser.name = userMap.get("name").toString();
                    aUser.emailAddress = userMap.get("emailAddress").toString();
                    aUser.profilePictureURL = userMap.get("profilePictureURL").toString();
                    //adding that user to the user list
                    usersList.add(aUser);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
            }
        });



        //getting messages from FireStore with Realtime Updates
        db.collection("workspaces")
                .document(workspaceID)
                .collection("tasks")
                .document(taskID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w(TAG, "Listen failed.", e);
                            return;
                        }
                        else{
                            //prevent duplication of messageList
                            messagesList.clear();
                        }
                        if (snapshot != null && snapshot.exists()) {
                            //mapping the nested array into an array list
                            ArrayList<Map<String, Object>> retrieveArrayList  = (ArrayList<Map<String, Object>>) snapshot.get("messages");
                            if (retrieveArrayList != null){
                                for (int mes = 0; mes < retrieveArrayList.size(); mes++){
                                    Map<String, Object> retrievedMsg = retrieveArrayList.get(mes);
                                    //setting up the Message class and its properties
                                    Message msgDetails = new Message();
                                    msgDetails.msgContent = retrievedMsg.get("MsgContent").toString();
                                    msgDetails.msgUserID = retrievedMsg.get("MsgUserId").toString();
                                    msgDetails.msgTimeStamp = Long.parseLong(retrievedMsg.get("MsgTimeStamp").toString());
                                    //adding the message class into the message list
                                    messagesList.add(msgDetails);
                                }
                                Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
                                if (messagesList.size() != 0){
                                    setUpRecyclerView(messagesList, usersList, currentId, color);
                                    //chatAdapter.notifyDataSetChanged();
                                }
                            }
                            Log.d(TAG, "Current data: " + snapshot.getData());
                        } else {
                            Log.d(TAG, "Current data: null");
                        }

                        //notification: checking if user has sent at least a message
                        if (messagesList.size() != 0){
                            for(int uidd = 0; uidd < messagesList.size(); uidd ++){
                                if (messagesList.get(uidd).msgUserID.equals(currentId)){
                                    sentNotification = false;
                                }
                            }
                        }

                        //sending notification if valid
                        createNotificationChannel();
                        if (sentNotification){
                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MessageChatActivity.this, "Message Notification")
                                    .setSmallIcon(R.drawable.ic_freeflow_icon_logo)
                                    .setContentTitle("Get started with Freeflow Messaging!")
                                    .setContentText("Sent your first message today & stay updated with the latest messages.")
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .bigText("Sent your first message today & stay updated with the latest messages."))
                                    .setAutoCancel(true)
                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);

                            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(MessageChatActivity.this);
                            notificationManager.notify(1001, builder.build());
                            sentNotification = false;
                        }
                    }
                });


        /* --------------------------------------------------------------------- */
        /* ----------------   EVENT LISTENERS FOR THE BUTTONS   ---------------- */
        /* --------------------------------------------------------------------- */

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
                //prevent user from sending an empty message
                if (messageEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No message input", Toast.LENGTH_LONG).show();
                    return;
                }

                //removing whitespace at the start and end of the message
                Boolean removeWhitespace = true;
                Message newMsg = new Message();
                String inputMessage = messageEditText.getText().toString();
                while (removeWhitespace){
                    Character back = inputMessage.charAt(inputMessage.length() -1);
                    Character front = inputMessage.charAt(0);
                    if (Character.isWhitespace(back)){
                        //removing the last character
                        inputMessage = inputMessage.substring(0, inputMessage.length() -1);
                    }
                    else if(Character.isWhitespace(front)){
                        //removing the first character
                        inputMessage = inputMessage.substring(1);
                    }
                    else{
                        removeWhitespace = false;
                    }
                }

                //Populating the message class object
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                newMsg.msgContent = inputMessage;
                newMsg.msgUserID = currentId;
                newMsg.msgTimeStamp = timestamp.getTime();
                //adding the message to the message list
                messagesList.add(newMsg);
                //clearing the input field
                messageEditText.setText("");

                //mapping the message object into a mapped nested list
                Map<String, Object> messageData = new HashMap<>();
                Map<String, Object> eachMessaging = newMsg.toMap();
                messageData.put("messages", Arrays.asList(eachMessaging));

                //adding messages to Firestore Database
                db.collection("workspaces")
                        .document(workspaceID)
                        .collection("tasks")
                        .document(taskID)
                        .update("messages", FieldValue.arrayUnion(eachMessaging)) //arrayUnion is used to append the new message to the nested messages array in FireStore
                        .addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "Message added successfully", Toast.LENGTH_LONG).show();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to send message", Toast.LENGTH_LONG).show();
                            }
                        });

                //set up the recycler view if this is the first message sent
                if (messagesList.size() <= 1){
                    setUpRecyclerView(messagesList, usersList, currentId, color);
                }
                //add the message into the message list found in the chat Adapter
                else{
                    chatAdapter = new MessageChatAdapter(messagesList, usersList, currentId, color, MessageChatActivity.this, MessageChatActivity.this);
                    chatAdapter.notifyDataSetChanged();
                }

            }
        });
    }


    //method used for setting up the message recycler view
    private void setUpRecyclerView(ArrayList<Message> msgList, ArrayList<User> usersList, String currentId, int color){
        chatRecyclerView = findViewById(R.id.message_recycler_view);
        chatRecyclerView.setHasFixedSize(true);
        chatAdapter = new MessageChatAdapter(msgList, usersList, currentId, color, MessageChatActivity.this, MessageChatActivity.this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(MessageChatActivity.this));
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatRecyclerView.getLayoutManager().scrollToPosition(msgList.size() - 1); //always display the most recent message
        chatRecyclerView.setAdapter(chatAdapter);
    }

    //method used for setting up the notification
    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Message Notification", "Message Notification", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Freeflow Task Messaging");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}