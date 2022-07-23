package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
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

    TextView messageContent_TheirNext;
    TextView messageTime_TheirNext;

    CardView messageCard_My;
    CardView messageCard_Their;
    CardView messageCard_TheirNext;


    public MessageChatViewHolder(@NonNull View itemView) {
        super(itemView);

        messageCard_My = itemView.findViewById(R.id.my_message_container);
        messageCard_Their = itemView.findViewById(R.id.their_next_message_card);


        messageContent_My = itemView.findViewById(R.id.my_message_text);
        messageTime_My = itemView.findViewById(R.id.my_message_timestamp);

        messageContent_Their = itemView.findViewById(R.id.their_next_message_text);
        messageTime_Their = itemView.findViewById(R.id.their_next_message_timestamp);
        messageUsername_Their = itemView.findViewById(R.id.their_message_username);
        messageProfile_Their = itemView.findViewById(R.id.their_message_profilepic);

        messageDate = itemView.findViewById(R.id.message_date);

        messageContent_TheirNext = itemView.findViewById(R.id.their_next_message_text);
        messageTime_TheirNext = itemView.findViewById(R.id.their_next_message_timestamp);
        messageCard_TheirNext = itemView.findViewById(R.id.their_next_message_card);
    }
}
