package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskDetailViewHolder extends RecyclerView.ViewHolder {

    TextView websiteTitleTextView;
    TextView websiteURLTextView;
    TextView websiteDescriptionTextView;

    TextView titleTextView;

    TextView taskTitleTextView;
    ImageView checkboxImageView;

    View root;

    public TaskDetailViewHolder(@NonNull View itemView) {
        super(itemView);

        root = itemView;

        websiteTitleTextView = itemView.findViewById(R.id.website_title_textview);
        websiteURLTextView = itemView.findViewById(R.id.website_url_textview);
        websiteDescriptionTextView = itemView.findViewById(R.id.website_description_textview);

        titleTextView = itemView.findViewById(R.id.title_text_view);

        taskTitleTextView = itemView.findViewById(R.id.task_title_text_view);
        checkboxImageView = itemView.findViewById(R.id.checkbox_image_view);
    }

    public ViewType getViewType() {
        if (websiteTitleTextView != null) {
            return ViewType.WEBSITE;
        } else if (titleTextView != null) {
            return ViewType.HEADER;
        } else {
            return ViewType.SUBTASK;
        }
    }

    public enum ViewType {
        HEADER, WEBSITE, SUBTASK
    }

    public void setAccentColor(int color) {
        CardView cardView = (CardView) root;
        cardView.setCardBackgroundColor(color);
        websiteURLTextView.setTextColor(color);
    }
}