package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceTasksViewHolder extends RecyclerView.ViewHolder {

    TextView taskCountTextView;
    TextView categoryTitleTextView;

    TextView taskTitleTextView;

    View superview;
    public WorkspaceTasksViewHolder(@NonNull View itemView) {
        super(itemView);

        taskCountTextView = itemView.findViewById(R.id.task_count_text_view);
        categoryTitleTextView = itemView.findViewById(R.id.category_title_text_view);

        taskTitleTextView = itemView.findViewById(R.id.task_title_text_view);

        superview = itemView;
    }

    public void setUpCategory(String name, int taskCount) {
        taskCountTextView.setText(String.valueOf(taskCount));
        categoryTitleTextView.setText(name);
    }

    public void setUpTasks(int color) {
        CardView tasksBackgroundCard = superview.findViewById(R.id.tasks_background_card);
        tasksBackgroundCard.setCardBackgroundColor(color);
        taskTitleTextView.setTextColor(color);
    }
}
