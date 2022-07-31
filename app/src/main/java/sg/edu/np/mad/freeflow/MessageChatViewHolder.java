package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MessageChatViewHolder extends RecyclerView.ViewHolder {

    TextView messageContent_My;
    TextView messageTime_My;

    TextView messageContent_Their;
    TextView messageTime_Their;
    TextView messageUsername_Their;
    ImageView messageProfile_Their;

    TextView messageDate;
    TextView message1Date;
    Space messageSpace;
    Space message1Space;
    LinearLayout messageHolder;
    LinearLayout message1Holder;

    TextView messageContent_TheirNext;
    TextView messageTime_TheirNext;

    CardView messageCard_My;
    CardView messageCard_Their;
    CardView messageCard_TheirNext;

    Space messageSpace_My;


    public MessageChatViewHolder(@NonNull View itemView) {
        super(itemView);

        //matching the variables to the resources ID
        messageCard_My = itemView.findViewById(R.id.my_message_cardholder);
        messageCard_Their = itemView.findViewById(R.id.their_message_cardholder);
        messageContent_My = itemView.findViewById(R.id.my_message_text);
        messageTime_My = itemView.findViewById(R.id.my_message_timestamp);
        messageContent_Their = itemView.findViewById(R.id.their_message_text);
        messageTime_Their = itemView.findViewById(R.id.their_message_timestamp);
        messageUsername_Their = itemView.findViewById(R.id.their_message_username);
        messageProfile_Their = itemView.findViewById(R.id.their_message_profilepic);
        messageDate = itemView.findViewById(R.id.message_date_text);
        message1Date = itemView.findViewById(R.id.message1_date_text);
        messageSpace = itemView.findViewById(R.id.message_date_space);
        message1Space = itemView.findViewById(R.id.message1_date_space);
        messageHolder = itemView.findViewById(R.id.message_date_holder);
        message1Holder = itemView.findViewById(R.id.message1_date_holder);
        messageContent_TheirNext = itemView.findViewById(R.id.their_next_message_text);
        messageTime_TheirNext = itemView.findViewById(R.id.their_next_message_timestamp);
        messageCard_TheirNext = itemView.findViewById(R.id.their_next_message_cardholder);
        messageSpace_My = itemView.findViewById(R.id.message_space);
    }

}
