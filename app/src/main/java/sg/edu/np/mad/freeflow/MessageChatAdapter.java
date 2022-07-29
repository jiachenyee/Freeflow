package sg.edu.np.mad.freeflow;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import com.squareup.picasso.Picasso;

import io.grpc.InternalChannelz;

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatViewHolder> {

    //initialising the variables
    int color;
    ArrayList<Message> messageArrayList;
    ArrayList<User> userArrayList;
    String currentUserId;
    Activity activity;
    Context context;
    User theirSender;

    //setting the type of display (based on layout file) to numbers for easier reference
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    public static final int VIEW_TYPE_SECOND_RECEIVED = 3;

    public MessageChatAdapter(ArrayList<Message> messageArrayList, ArrayList<User> userArrayList, String currentUserId, int color, Context context, Activity activity){
        this.messageArrayList = messageArrayList;
        this.color = color;
        this.userArrayList = userArrayList;
        this.currentUserId = currentUserId;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MessageChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //use sender layout view
        if (viewType == VIEW_TYPE_SENT)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_card, parent, false);
            return new MessageChatViewHolder(v);
        }
        //use receiver layout view
        else if (viewType == VIEW_TYPE_SECOND_RECEIVED){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_second_message_card, parent, false);
            return new MessageChatViewHolder(v);
        }
        //use receiver layout view without the profile pic and user name
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message_card, parent, false);
            return new MessageChatViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageChatViewHolder holder, int position) {

        //retrieving the position of that message
        Message msg = messageArrayList.get(position);

        //display the elements where necessary
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, EEEE");

            if (position == 0){ //checking for first position
                holder.messageHolder.setVisibility(View.VISIBLE);
                //removing the extra space if that is the first message
                holder.messageSpace.setVisibility(View.GONE);
                holder.messageDate.setText(sdf2.format(messageArrayList.get(position).msgTimeStamp));
            }
            else if (!sdf2.format(messageArrayList.get(position).msgTimeStamp).equals(sdf2.format(messageArrayList.get(position-1).msgTimeStamp))){ //checking if the previous msg timestamp matches the current msg timestamp
                holder.messageHolder.setVisibility(View.VISIBLE);
                holder.messageSpace.setVisibility(View.VISIBLE);
                holder.messageDate.setText(sdf2.format(messageArrayList.get(position).msgTimeStamp));
            }
            else {
                holder.messageHolder.setVisibility(View.GONE);
            }
            //setting up the content
            holder.messageCard_My.setCardBackgroundColor(color);
            holder.messageContent_My.setText(msg.msgContent);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            holder.messageTime_My.setText(sdf1.format(msg.msgTimeStamp));
            if (position != 0 && messageArrayList.get(position).msgUserID.equals(messageArrayList.get(position - 1).msgUserID)){
                holder.messageSpace_My.setVisibility(View.GONE);
            }
        }
        //checking if the message is received by the user
        else if (getItemViewType(position) == VIEW_TYPE_SECOND_RECEIVED){
            //checking the user list sent from the Activity with the user id attached to the message
            for(int u = 0; u < userArrayList.size(); u++){
                if (userArrayList.get(u).userID.equals(messageArrayList.get(position).msgUserID)){
                    theirSender = userArrayList.get(u);
                    break;
                }
            }
            //setting up the content
            holder.messageContent_TheirNext.setText(msg.msgContent);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            holder.messageTime_TheirNext.setText(sdf1.format(msg.msgTimeStamp));
        }
        else{
            //checking the user list sent from the Activity with the user id attached to the message
            for (int u = 0; u < userArrayList.size(); u++) {
                if (userArrayList.get(u).userID.equals(messageArrayList.get(position).msgUserID)) {
                    theirSender = userArrayList.get(u);
                    break;
                }
            }

            //display the elements where necessary
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM dd, EEEE");
            if (position == 0){
                holder.message1Holder.setVisibility(View.VISIBLE);
                holder.message1Space.setVisibility(View.GONE);
                holder.message1Date.setText(sdf2.format(messageArrayList.get(position).msgTimeStamp));
            }
            else if (position != 0 && !sdf2.format(messageArrayList.get(position).msgTimeStamp).equals(sdf2.format(messageArrayList.get(position-1).msgTimeStamp))){
                holder.message1Holder.setVisibility(View.VISIBLE);
                holder.message1Space.setVisibility(View.VISIBLE);
                holder.message1Date.setText(sdf2.format(messageArrayList.get(position).msgTimeStamp));
            }
            else {
                holder.message1Holder.setVisibility(View.GONE);
            }
            //setting up the content
            holder.messageContent_Their.setText(msg.msgContent);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            holder.messageTime_Their.setText(sdf1.format(msg.msgTimeStamp));
            //truncate the username if it is too long
            if (theirSender.name.length() > 15){
                String truncatedName = theirSender.name.substring(0, 15);
                holder.messageUsername_Their.setText(truncatedName + "...");
            }
            else{
                holder.messageUsername_Their.setText(theirSender.name);
            }
            //load profile picture with third party library
            Picasso.with(context).load(Uri.parse(theirSender.profilePictureURL)).fit().centerCrop().into(holder.messageProfile_Their);

            //Viewing the user account by clicking on the user profile picture
            holder.messageProfile_Their.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent accountActivity = new Intent(view.getContext(), MessageAccountActivity.class);
                    accountActivity.putExtra("userIDD", messageArrayList.get(position).msgUserID);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        view.getContext().startActivity(accountActivity, ActivityOptions.makeSceneTransitionAnimation(activity).toBundle());
                    }
                    else{
                        view.getContext().startActivity(accountActivity);
                    }
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    //setting the type of view based on message sent or received
    @Override
    public int getItemViewType(int position){
        if (messageArrayList.get(position).msgUserID.equals(currentUserId)){
            return VIEW_TYPE_SENT;
        } else if (messageArrayList.get(position).msgUserID != (currentUserId) && position != 0 && messageArrayList.get(position).msgUserID.equals(messageArrayList.get(position - 1).msgUserID) ){
            return VIEW_TYPE_SECOND_RECEIVED;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }
}

