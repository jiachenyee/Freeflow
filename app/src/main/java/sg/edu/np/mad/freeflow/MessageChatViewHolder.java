package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class MessageChatViewHolder extends RecyclerView.ViewHolder {

    TextView messageContent_My;
    TextView messageTime_My;

    TextView messageContent_Their;
    TextView messageTime_Their;
    TextView messageUsername_Their;
    ImageView messageProfile_Their;

    TextView messageDate;

    ConstraintLayout messageHolder_My;
    ConstraintLayout messageHolder_Their;
    ConstraintLayout detailsHolder_Their;

    CardView messageContainer_My;


    public MessageChatViewHolder(@NonNull View itemView) {
        super(itemView);

        messageContainer_My = itemView.findViewById(R.id.my_message_container);

        messageContent_My = itemView.findViewById(R.id.my_message_text);
        messageTime_My = itemView.findViewById(R.id.my_message_timestamp);

        messageContent_Their = itemView.findViewById(R.id.their_message_text);
        messageTime_Their = itemView.findViewById(R.id.their_message_timestamp);
        messageUsername_Their = itemView.findViewById(R.id.their_message_username);
        messageProfile_Their = itemView.findViewById(R.id.their_message_profilepic);

        messageDate = itemView.findViewById(R.id.message_date);

        messageHolder_My = itemView.findViewById(R.id.my_message_holder);
        messageHolder_Their = itemView.findViewById(R.id.their_message_holder);
        detailsHolder_Their = itemView.findViewById(R.id.their_details_holder);
    }
}
