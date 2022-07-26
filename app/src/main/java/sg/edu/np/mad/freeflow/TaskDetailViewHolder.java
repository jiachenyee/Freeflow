package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TaskDetailViewHolder extends RecyclerView.ViewHolder {

    TextView websiteTitleTextView;
    TextView websiteURLTextView;
    TextView websiteDescriptionTextView;

    public TaskDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        websiteTitleTextView = itemView.findViewById(R.id.website_title_textview);
        websiteURLTextView = itemView.findViewById(R.id.website_url_textview);
        websiteDescriptionTextView = itemView.findViewById(R.id.website_description_textview);
    }
}