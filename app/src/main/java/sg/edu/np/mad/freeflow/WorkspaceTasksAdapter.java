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
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.task_header,
                parent,
                false);

        WorkspaceTasksViewHolder workspaceTasksViewHolder = new WorkspaceTasksViewHolder(v);
        workspaceTasksViewHolder.taskCountTextView.setTextColor(activity.getResources().getColor(Workspace.colors[workspace.accentColor]));

        return workspaceTasksViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceTasksViewHolder holder, int position) {
        Category category = workspace.categories.get(position);

        holder.categoryTitleTextView.setText(category.name);
        if (category.subtasks == null) {
            holder.taskCountTextView.setText("0");
        } else {
            int size = category.subtasks.size();

            holder.taskCountTextView.setText(Integer.toString(size));
        }
    }

    @Override
    public int getItemCount() {
        return workspace.categories.size();
    }
}
