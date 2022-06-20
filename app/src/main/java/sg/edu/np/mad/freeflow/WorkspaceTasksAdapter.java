package sg.edu.np.mad.freeflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceTasksAdapter extends RecyclerView.Adapter<WorkspaceTasksViewHolder> {

    Workspace workspace;

    public WorkspaceTasksAdapter(Workspace workspace) {
        this.workspace = workspace;
    }

    @NonNull
    @Override
    public WorkspaceTasksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.task_header,
                parent,
                false);

        return new WorkspaceTasksViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceTasksViewHolder holder, int position) {
        holder.categoryTitleTextView.setText(workspace.categories.get(position).name);
    }

    @Override
    public int getItemCount() {
        return workspace.categories.size();
    }
}
