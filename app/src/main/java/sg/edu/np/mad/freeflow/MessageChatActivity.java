package sg.edu.np.mad.freeflow;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
        Bundle extras = this.getIntent().getExtras();
        if (extras == null) { System.out.println("invalid");}

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String workspaceID = extras.getString("workspaceID");
        String taskID = extras.getString("taskID");
        int color = extras.getInt("accentColor");
        findViewById(R.id.msg_header_view).setBackgroundColor(color);


        //getting data from cloud Firestore
        //DocumentReference docRef = db.collection("workspaces").document(workspaceID).collection("tasks").document(taskID);

        /*
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                messagesList = documentSnapshot.to
                taskTitleTextView.setText((String) documentSnapshot.get("message"));
                taskDescriptionTextView.setText((String) documentSnapshot.get("description"));
            }
        });
        */





        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        setUpRecyclerView(messagesList, color);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth = FirebaseAuth.getInstance();
                user = mAuth.getCurrentUser();
                //SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

                if (messageEditText.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "No message inputted", Toast.LENGTH_LONG).show();
                    return;
                }


                Message newMsg = new Message();
                newMsg.msgContent = messageEditText.getText().toString();
                newMsg.msgUserID = new User(user).userID;
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                newMsg.msgTimeStamp = timestamp.getTime();

                messagesList.add(newMsg);
                messageEditText.setText("");

                Map<String, Object> messageData = new HashMap<>();
                Map<String, Object> eachMessaging = newMsg.toMap();

                messageData.put("messages", Arrays.asList(eachMessaging));


                /*
                db.collection("workspaces")
                        .document(workspaceID)
                        .collection("tasks")
                        .document(taskID)
                        .set(messageData)
                        .addOnSuccessListener(new OnSuccessListener<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                db.collection("workspaces")
                                        .document(workspaceID)
                                        .collection("tasks")
                                        .document(taskID)
                                        .update("message", FieldValue.arrayUnion(eachMessaging))
                                        .addOnSuccessListener(new OnSuccessListener<Void>(){
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getApplicationContext(), "Message added successfully", Toast.LENGTH_LONG).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getApplicationContext(), "Failed to send message", Toast.LENGTH_LONG).show();
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to add message to database", Toast.LENGTH_LONG).show();
                            }
                        });

                 */


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
                    System.out.println(messagesList.get(i).msgUserID);
                    System.out.println(messagesList.get(i).msgContent);
                }

                setUpRecyclerView(messagesList, color);
            }
        });



    }

    protected void setUpRecyclerView(ArrayList<Message> msgList, int color){
        chatRecyclerView = findViewById(R.id.message_recycler_view);
        chatRecyclerView.setHasFixedSize(true);
        chatAdapter = new MessageChatAdapter(msgList, color, MessageChatActivity.this);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.scrollToPosition(msgList.size()-1);
        chatRecyclerView.setItemAnimator(new DefaultItemAnimator());
        chatRecyclerView.setAdapter(chatAdapter);
    }
}