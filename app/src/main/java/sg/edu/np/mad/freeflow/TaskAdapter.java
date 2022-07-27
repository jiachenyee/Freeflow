package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskViewHolder> {

    List<TaskWorkspaceWrapper> taskWorkspaceWrappers;
    MainActivity activity;

    public TaskAdapter(List<TaskWorkspaceWrapper> taskWorkspaceWrappers, MainActivity activity) {
        this.taskWorkspaceWrappers = taskWorkspaceWrappers;
        this.activity = activity;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.task_card,
                parent,
                false);

        return new TaskViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        TaskWorkspaceWrapper taskWorkspaceWrapper = taskWorkspaceWrappers.get(position);

        holder.setUpTasks(activity.getResources().getColor(Workspace.colors[taskWorkspaceWrapper.color]));
        holder.taskTitleTextView.setText(taskWorkspaceWrapper.task.title);
        holder.taskDueDateTextView.setText(taskWorkspaceWrapper.task.dueDate);
        holder.taskDescriptionTextView.setText(taskWorkspaceWrapper.task.description);
        holder.setOnClickHandler(new WorkspaceTasksViewHolder.OnTaskOpenHandler() {
            @Override
            public void onTaskOpenHandler() {
                Intent workspaceTaskActivity = new Intent(activity, TaskActivity.class);

                workspaceTaskActivity.putExtra("workspaceID", taskWorkspaceWrapper.workspaceID);
                workspaceTaskActivity.putExtra("taskID", taskWorkspaceWrapper.task.taskID);
                workspaceTaskActivity.putExtra("accentColor", activity.getResources().getColor(Workspace.colors[taskWorkspaceWrapper.color]));

                activity.startActivity(workspaceTaskActivity);
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskWorkspaceWrappers.size();
    }

    public void setTaskWorkspaceWrapperList(List<TaskWorkspaceWrapper> wrapperList) {
        this.taskWorkspaceWrappers = wrapperList;
    }
}
