package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class TaskViewHolder extends RecyclerView.ViewHolder {
    TextView taskTitleTextView;
    TextView taskSubtitleTextView;

    View superview;

    WorkspaceTasksViewHolder.OnTaskOpenHandler onTaskOpenHandler;

    public TaskViewHolder(@NonNull View itemView) {
        super(itemView);

        taskTitleTextView = itemView.findViewById(R.id.task_title_text_view);
        taskSubtitleTextView = itemView.findViewById(R.id.task_subtitle_text_view);

        superview = itemView;

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onTaskOpenHandler != null) {
                    onTaskOpenHandler.onTaskOpenHandler();
                }
            }
        });
    }

    public void setOnClickHandler(WorkspaceTasksViewHolder.OnTaskOpenHandler handler) {
        this.onTaskOpenHandler = handler;
    }

    public void setUpTasks(int color) {
        CardView tasksBackgroundCard = superview.findViewById(R.id.tasks_background_card);
        tasksBackgroundCard.setCardBackgroundColor(color);
        taskTitleTextView.setTextColor(color);
    }

    public interface OnTaskOpenHandler {
        void onTaskOpenHandler();
    }
}
