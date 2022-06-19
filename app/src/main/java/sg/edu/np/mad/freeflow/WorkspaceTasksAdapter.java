package sg.edu.np.mad.freeflow;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceTasksAdapter extends RecyclerView.Adapter<WorkspaceTasksViewHolder> {

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

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
