package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class WorkspaceTasksViewHolder extends RecyclerView.ViewHolder {

    TextView taskCountTextView;
    TextView categoryTitleTextView;

    TextView taskTitleTextView;
    TextView taskDueDateTextView;
    TextView taskDescriptionTextView;

    View superview;

    OnTaskOpenHandler onTaskOpenHandler;
    String taskID;

    public WorkspaceTasksViewHolder(@NonNull View itemView) {
        super(itemView);

        taskCountTextView = itemView.findViewById(R.id.task_count_text_view);
        categoryTitleTextView = itemView.findViewById(R.id.category_title_text_view);

        taskTitleTextView = itemView.findViewById(R.id.task_title_text_view);
        taskDueDateTextView = itemView.findViewById(R.id.task_due_date_text_view);
        taskDescriptionTextView = itemView.findViewById(R.id.task_description_text_view);

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

    public void setUpCategory(String name, int taskCount) {
        taskCountTextView.setText(String.valueOf(taskCount));
        categoryTitleTextView.setText(name);
    }

    public void setOnClickHandler(OnTaskOpenHandler handler) {
        this.onTaskOpenHandler = handler;
    }

    public void setUpTasks(int color) {
        CardView tasksBackgroundCard = superview.findViewById(R.id.tasks_background_card);
        tasksBackgroundCard.setCardBackgroundColor(color);
        taskTitleTextView.setTextColor(color);
        taskDueDateTextView.setTextColor(color);
    }

    public interface OnTaskOpenHandler {
        void onTaskOpenHandler();
    }
}
