package sg.edu.np.mad.freeflow;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MessageChatAdapter extends RecyclerView.Adapter<MessageChatViewHolder> {

    int color;
    ArrayList<Message> messageArrayList;
    MessageChatActivity activity;
    //Context context;

    public MessageChatAdapter(ArrayList<Message> messageArrayList, int color, Context context){
        this.messageArrayList = messageArrayList;
        this.color = color;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MessageChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MessageChatViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_message_card, null));
    }

    @Override
    public void onBindViewHolder(@NonNull MessageChatViewHolder holder, int position) {
        Message msg = messageArrayList.get(position);


        //Timestamp timestamp = new Timestamp(msg.messageDateTime);
        //Date date = new Date(timestamp.getTime());
        //SimpleDateFormat simpleDateFormat =  new SimpleDateFormat("MMM d", Locale.getDefault());
        //SimpleDateFormat simpleTimeFormat =  new SimpleDateFormat("hh:mm", Locale.getDefault());

        // do a if loop to check if the sender of the message is the current user
        holder.messageContainer_My.setCardBackgroundColor(color);
        holder.messageHolder_My.setVisibility(View.VISIBLE);
        holder.messageContent_My.setText(msg.msgContent);
        SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
        holder.messageTime_My.setText(sdf1.format(msg.msgTimeStamp));
        System.out.println("time now: " + msg.msgTimeStamp);


    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    public void updateChatList(ArrayList<Message> messageArrayList){
        this.messageArrayList = messageArrayList;
    }
}
