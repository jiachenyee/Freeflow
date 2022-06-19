package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceTasksViewHolder extends RecyclerView.ViewHolder {

    TextView taskCountTextView;
    TextView categoryTitleTextView;

    public WorkspaceTasksViewHolder(@NonNull View itemView) {
        super(itemView);

        taskCountTextView = itemView.findViewById(R.id.task_count_text_view);
        categoryTitleTextView = itemView.findViewById(R.id.category_title_text_view);
    }

    public void setUpCategory(String name, int taskCount) {
        taskCountTextView.setText(taskCount.toString());
        categoryTitleTextView.setText(name);
    }
}
