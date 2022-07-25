package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AssigneeViewHolder extends RecyclerView.ViewHolder {

    TextView userNameTextView;
    ImageView profileImage;

    public AssigneeViewHolder(View viewItem){
        super(viewItem);
        //profileImage = viewItem.findViewById(R.id.profile_image_view);
        userNameTextView = viewItem.findViewById(R.id.assignee_text_view);
    }
}
