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

    ImageButton closeButton;
    EditText messageEditText;
    ImageButton sendButton;
    TextView messageTitle;

    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private RecyclerView chatRecyclerView;
    private MessageChatAdapter chatAdapter;

    FirebaseFirestore db;

    ArrayList<Message> messagesList = new ArrayList<>();
    ArrayList<User> usersList = new ArrayList<>();
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
        findViewById(R.id.msg_header_view).setBackgroundColor(color);

        sentNotification = true;
        System.out.println("TaskID: " + taskID);
        System.out.println("WorkspaceID: " + workspaceID);


        //display of task title
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
                                //String taskTitle = documentSnapshot.get("title").toString();
                                //System.out.println("Title: " + taskTitle);
                                messageTitle.setText(documentSnapshot.get("title").toString());
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                                System.out.println("Done");
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });


        /*
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
                                    setUpRecyclerView(messagesList, usersList, currentId, color);
                                    System.out.println("Done3");
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });

        System.out.println("Now" + messagesList);

         */

        //getting users from FireStore
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
                //Loop every document to get user details
                for (QueryDocumentSnapshot document : value) {
                    User aUser = new User();
                    aUser.userID = document.getId();
                    Map<String, Object> userMap = document.getData();
                    aUser.name = userMap.get("name").toString();
                    aUser.emailAddress = userMap.get("emailAddress").toString();
                    aUser.profilePictureURL = userMap.get("profilePictureURL").toString();
                    usersList.add(aUser);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
            }
        });

        //getting messages from FireStore
        db.collection("workspaces")
                .document(workspaceID)
                .collection("tasks")
                .document(taskID)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    //retrieving the messages with Realtime Updates
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
                            ArrayList<Map<String, Object>> retrieveArrayList  = (ArrayList<Map<String, Object>>) snapshot.get("messages");
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

                        //checking if notification for user exist
                        if (messagesList.size() != 0){
                            for(int uidd = 0; uidd < messagesList.size(); uidd ++){
                                System.out.println("AHHHHHHAHAHAHAHHA");
                                System.out.println("------" + currentId);
                                System.out.println(messagesList.get(uidd).msgUserID);
                                if (messagesList.get(uidd).msgUserID.equals(currentId)){
                                    sentNotification = false;
                                }
                            }
                        }

                        System.out.println("Notification: " + sentNotification);
                        //sending notification
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




        //getMsgDataWithRealtimeUpdates();

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
                    Toast.makeText(getApplicationContext(), "No message input", Toast.LENGTH_LONG).show();
                    return;
                }
                Boolean removeWhitespace = true;

                Message newMsg = new Message();
                String inputMessage = messageEditText.getText().toString();

                while (removeWhitespace){
                    Character back = inputMessage.charAt(inputMessage.length() -1);
                    Character front = inputMessage.charAt(0);
                    System.out.println("character is: " + back);
                    if (Character.isWhitespace(back)){
                        System.out.println("Spaccee");
                        inputMessage = inputMessage.substring(0, inputMessage.length() -1);
                        System.out.println("you are |" + inputMessage + "|" );
                    }
                    else if(Character.isWhitespace(front)){
                        System.out.println("Spaccee");
                        inputMessage = inputMessage.substring(1);
                        System.out.println("you are |" + inputMessage + "|" );
                    }
                    else{
                        removeWhitespace = false;
                    }
                }


                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                newMsg.msgContent = inputMessage;
                newMsg.msgUserID = currentId;
                newMsg.msgTimeStamp = timestamp.getTime();

                messagesList.add(newMsg);
                messageEditText.setText("");

                Map<String, Object> messageData = new HashMap<>();
                Map<String, Object> eachMessaging = newMsg.toMap();

                messageData.put("messages", Arrays.asList(eachMessaging));
                //adding messages to Firestore Database
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

                //setUpRecyclerView(messagesList, usersList, currentId, color);
                chatAdapter.notifyDataSetChanged();
            }
        });
    }



    private void setUpRecyclerView(ArrayList<Message> msgList, ArrayList<User> usersList, String currentId, int color){
        chatRecyclerView = findViewById(R.id.message_recycler_view);
        chatRecyclerView.setHasFixedSize(true);
        chatAdapter = new MessageChatAdapter(msgList, usersList, currentId, color, MessageChatActivity.this);
        System.out.println("list size: " + usersList.size());
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(MessageChatActivity.this));
        //chatRecyclerView.scrollToPosition(msgList.size()-1);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatRecyclerView.getLayoutManager().scrollToPosition(msgList.size() - 1);
        chatRecyclerView.setAdapter(chatAdapter);
    }

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

    private void sentNotification(){

    }

    private void getMsgDataWithRealtimeUpdates(){
        ArrayList<Message> updatedMsgList = new ArrayList<Message>();
        FirebaseFirestore.getInstance().collection("workspaces")
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
                    messagesList.clear();
                }
                if (snapshot != null && snapshot.exists()) {
                    ArrayList<Map<String, Object>> retrieveArrayList  = (ArrayList<Map<String, Object>>) snapshot.get("messages");
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
                        Log.d(TAG, "DocumentSnapshot data: " + snapshot.getData());
                        setUpRecyclerView(messagesList, usersList, currentId, color);
                        chatAdapter.notifyDataSetChanged();
                    }
                    Log.d(TAG, "Current data: " + snapshot.getData());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }

    private void getTaskTitle(){
        FirebaseFirestore.getInstance().collection("workspaces")
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
                                String taskTitle = documentSnapshot.get("title").toString();
                                System.out.println("Title: " + taskTitle);
                                //messageTitle.setText(documentSnapshot.get("title").toString());
                                Log.d(TAG, "DocumentSnapshot data: " + documentSnapshot.getData());
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }

    private void getUsersList(){
        System.out.println("Hello Ryan Y");
        FirebaseFirestore.getInstance().collection("users")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User allUsers = new User();
                            allUsers.userID = document.getId();
                            Map<String, Object> userMap = document.getData();
                            allUsers.name = userMap.get("name").toString();
                            System.out.println("NAMEEEEEEEEEEEEEEE: " + allUsers.name);
                            allUsers.emailAddress = userMap.get("emailAddress").toString();
                            allUsers.profilePictureURL = userMap.get("profilePictureURL").toString();
                            usersList.add(allUsers);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                        return;
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
        });
        System.out.println("DATAAAAAAAAAAAA: " + usersList);
    }

    private void getUpdatedUserList(){
        ArrayList<User> updatedUserList = new ArrayList<>();
        FirebaseFirestore.getInstance().collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                for (QueryDocumentSnapshot document : value) {
                    User aUser = new User();
                    aUser.userID = document.getId();
                    Map<String, Object> userMap = document.getData();
                    aUser.name = userMap.get("name").toString();
                    aUser.emailAddress = userMap.get("emailAddress").toString();
                    aUser.profilePictureURL = userMap.get("profilePictureURL").toString();
                    updatedUserList.add(aUser);
                    Log.d(TAG, document.getId() + " => " + document.getData());
                }
            }
        });
    }

    private void getMsgList(){
        //getting data from cloud Firestore
        ArrayList<Message> messageData = new ArrayList<Message>();
        FirebaseFirestore.getInstance().collection("workspaces")
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
                                    return;
                                }
                            } else {
                                Log.d(TAG, "No such document");
                            }
                        } else {
                            Log.d(TAG, "get failed with ", task.getException());
                        }
                    }
                });
    }
}