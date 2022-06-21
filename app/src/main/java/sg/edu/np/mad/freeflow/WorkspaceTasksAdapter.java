package sg.edu.np.mad.freeflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceTasksAdapter extends RecyclerView.Adapter<WorkspaceTasksViewHolder> {

    Workspace workspace;
    WorkspaceActivity activity;

    public WorkspaceTasksAdapter(Workspace workspace, WorkspaceActivity activity) {
        this.workspace = workspace;
        this.activity = activity;
    }

    @NonNull
    @Override
    public WorkspaceTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        if (viewType == 0) {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.task_header,
                    parent,
                    false);
        } else {
            v = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.task_card,
                    parent,
                    false);
        }

        WorkspaceTasksViewHolder workspaceTasksViewHolder = new WorkspaceTasksViewHolder(v);

        if (viewType == 0) {
            workspaceTasksViewHolder.taskCountTextView.setTextColor(activity.getResources().getColor(Workspace.colors[workspace.accentColor]));
        } else {
            workspaceTasksViewHolder.setUpTasks(activity.getResources().getColor(Workspace.colors[workspace.accentColor]));
        }

        return workspaceTasksViewHolder;
    }

    @Override
    public int getItemViewType(int position) {
        int rowCount = 0;

        for (Category category: workspace.categories) {
            int subtaskCount = category.subtasks.size();
            rowCount += 1;
            if (position < rowCount) {
                return 0;
            } else if (position < rowCount + subtaskCount) {
                return 1;
            }
            rowCount += subtaskCount;
        }

        return 0;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceTasksViewHolder holder, int position) {

        int rowCount = 0;

        for (Category category: workspace.categories) {
            int subtaskCount = category.subtasks.size();
            rowCount += 1;
            if (position < rowCount) {
                holder.categoryTitleTextView.setText(category.name);
                if (category.subtasks == null) {
                    holder.taskCountTextView.setText("0");
                } else {
                    int size = category.subtasks.size();
                    holder.taskCountTextView.setText(Integer.toString(size));
                }
                break;
            } else if (position < rowCount + subtaskCount) {
                String taskID = category.subtasks.get(position - rowCount);

//                holder.taskCountTextView.setText(taskID);
                // TODO: Load actual task info
                break;
            }
            rowCount += subtaskCount;
        }
    }

    @Override
    public int getItemCount() {
        int count = workspace.categories.size();
        for (Category category: workspace.categories) {
            count += category.subtasks.size();
        }
        return count;
    }
}
