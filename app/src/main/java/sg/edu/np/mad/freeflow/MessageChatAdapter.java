package sg.edu.np.mad.freeflow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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

    int color;
    ArrayList<Message> messageArrayList;
    ArrayList<User> userArrayList;
    String currentUserId;
    MessageChatActivity activity;
    Context context;
    User theirSender;

    public MessageChatActivity getActivity() {
        return activity;
    }

    public static final int VIEW_TYPE_DATE = 0;
    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;
    public static final int VIEW_TYPE_SECOND_RECEIVED = 3;

    public MessageChatAdapter(ArrayList<Message> messageArrayList, ArrayList<User> userArrayList, String currentUserId, int color, Context context){
        this.messageArrayList = messageArrayList;
        this.color = color;
        this.userArrayList = userArrayList;
        this.currentUserId = currentUserId;
        this.context = context;
    }

    @NonNull
    @Override
    public MessageChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SENT)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_card, parent, false);
            return new MessageChatViewHolder(v);
        }
        else if (viewType == VIEW_TYPE_SECOND_RECEIVED){
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_second_message_card, parent, false);
            return new MessageChatViewHolder(v);
        }
        else
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.their_message_card, parent, false);
            return new MessageChatViewHolder(v);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageChatViewHolder holder, int position) {
        Message msg = messageArrayList.get(position);

        // do a if loop to check if the sender of the message is the current user
        if (getItemViewType(position) == VIEW_TYPE_SENT){
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            if (position == 0){
                holder.messageHolder.setVisibility(View.VISIBLE);
                holder.messageSpace.setVisibility(View.GONE);
                holder.messageDate.setText(sdf2.format(messageArrayList.get(position).msgTimeStamp));
            }
            else if (position != 0 && !sdf2.format(messageArrayList.get(position).msgTimeStamp).equals(sdf2.format(messageArrayList.get(position-1).msgTimeStamp))){
                holder.messageHolder.setVisibility(View.VISIBLE);
                holder.messageSpace.setVisibility(View.VISIBLE);
                holder.messageDate.setText(sdf2.format(messageArrayList.get(position).msgTimeStamp));
            }
            else {
                holder.messageHolder.setVisibility(View.GONE);
            }
            holder.messageCard_My.setCardBackgroundColor(color);
            holder.messageContent_My.setText(msg.msgContent);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            holder.messageTime_My.setText(sdf1.format(msg.msgTimeStamp));
            if (messageArrayList.get(position).msgUserID.equals(messageArrayList.get(position - 1).msgUserID)){
                holder.messageSpace_My.setVisibility(View.GONE);
            }
            System.out.println("time now: " + msg.msgTimeStamp);
        }
        else if (getItemViewType(position) == VIEW_TYPE_SECOND_RECEIVED){
            for(int u = 0; u < userArrayList.size(); u++){
                System.out.println("yes sir: " + messageArrayList.get(position).msgUserID);
                System.out.println("ryan: " + userArrayList.get(u).userID);
                if (userArrayList.get(u).userID.equals(messageArrayList.get(position).msgUserID)){
                    theirSender = userArrayList.get(u);
                    break;
                }
            }

            holder.messageContent_TheirNext.setText(msg.msgContent);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            holder.messageTime_TheirNext.setText(sdf1.format(msg.msgTimeStamp));
        }
        else{

            for (int u = 0; u < userArrayList.size(); u++) {
                System.out.println("yes sir: " + messageArrayList.get(position).msgUserID);
                System.out.println("ryan: " + userArrayList.get(u).userID);
                if (userArrayList.get(u).userID.equals(messageArrayList.get(position).msgUserID)) {
                    System.out.println("this is true lol");
                    theirSender = userArrayList.get(u);
                    break;
                }
            }

            System.out.println(userArrayList.size());

            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
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

            holder.messageContent_Their.setText(msg.msgContent);
            SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
            holder.messageTime_Their.setText(sdf1.format(msg.msgTimeStamp));
            if (theirSender.name.length() > 15){
                String truncatedName = theirSender.name.substring(0, 15);
                holder.messageUsername_Their.setText(truncatedName + "...");
            }
            else{
                holder.messageUsername_Their.setText(theirSender.name);
            }
            Picasso.with(context).load(Uri.parse(theirSender.profilePictureURL)).fit().centerCrop().into(holder.messageProfile_Their);
            //Picasso.with(context).load(Uri.parse(theirSender.profilePictureURL)).into(holder.messageProfile_Their);

            System.out.println("time now: " + msg.msgTimeStamp);

            holder.messageProfile_Their.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Ask Jia Chen for HELP!
                    Intent accountActivity = new Intent(view.getContext(), MessageAccountActivity.class);
                    System.out.println("ITS MEE: " + theirSender.name);
                    accountActivity.putExtra("userID", theirSender.userID);
                    accountActivity.putExtra("userName", theirSender.name);
                    accountActivity.putExtra("userEmail", theirSender.emailAddress);
                    accountActivity.putExtra("userPicture", theirSender.profilePictureURL);

                    view.getContext().startActivity(accountActivity);
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    @Override
    public int getItemViewType(int position){
        /*
        String myMsgDate = "";
        String previousMsgDate = "";
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        myMsgDate = sdf2.format(messageArrayList.get(position).msgTimeStamp);
        if (position != 0){
            previousMsgDate = sdf2.format(messageArrayList.get(position-1).msgTimeStamp);
        }
        if (position == 0 || (position != 0 && !myMsgDate.equals(previousMsgDate))){
            return VIEW_TYPE_DATE;
        }

         */
        if (messageArrayList.get(position).msgUserID.equals(currentUserId)){
            return VIEW_TYPE_SENT;
        } else if (messageArrayList.get(position).msgUserID != (currentUserId) && position != 0 && messageArrayList.get(position).msgUserID.equals(messageArrayList.get(position - 1).msgUserID) ){
            System.out.println("Now" + messageArrayList.get(position).msgUserID);
            System.out.println("Before" + messageArrayList.get(position - 1).msgUserID);
            return VIEW_TYPE_SECOND_RECEIVED;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }
}

