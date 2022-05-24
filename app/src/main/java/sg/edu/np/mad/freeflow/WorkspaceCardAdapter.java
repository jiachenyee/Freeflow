package sg.edu.np.mad.freeflow;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class WorkspaceCardAdapter extends RecyclerView.Adapter<WorkspaceCardViewHolder> {

    List<Workspace> workspaces;
    MainActivity activity;

    public WorkspaceCardAdapter(List<Workspace> workspaces, MainActivity activity) {
        this.workspaces = workspaces;
        this.activity = activity;
    }

    @NonNull
    @Override
    public WorkspaceCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.workspace_card,
                parent,
                false);

        return new WorkspaceCardViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceCardViewHolder holder, int position) {

        Workspace workspace = workspaces.get(position);

        RecyclerView.LayoutParams params = new RecyclerView.LayoutParams(
                RecyclerView.LayoutParams.WRAP_CONTENT,
                RecyclerView.LayoutParams.WRAP_CONTENT
        );

        if (position == 0) {
            params.setMargins(convertDpToPixels(16), 0, convertDpToPixels(4), 0);
        } else if (position == getItemCount() - 1) {
            params.setMargins(convertDpToPixels(4), 0, convertDpToPixels(16), 0);
        } else {
            params.setMargins(convertDpToPixels(4), 0, convertDpToPixels(4), 0);
        }

        holder.rootView.setLayoutParams(params);

        holder.workspaceNameTextView.setText(workspace.name);
        holder.workspaceInformationTextView.setText(workspace.users.size() + " members");
    }

    public int convertDpToPixels(float dips) {
        return (int) (dips * activity.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public int getItemCount() {
        return workspaces.size();
    }
}
