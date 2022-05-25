package sg.edu.np.mad.freeflow;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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

        return new WorkspaceCardViewHolder(item).setOnWorkspaceOpenHandler(new WorkspaceCardViewHolder.OnWorkspaceOpenHandler() {
            @Override
            public void onWorkspaceOpen(int index) {
                Intent workspaceActivity = new Intent(activity, WorkspaceActivity.class);

                Workspace workspace = workspaces.get(index);

                workspaceActivity.putExtra("workspaceIcon", workspace.workspaceIcon);
                workspaceActivity.putExtra("workspaceAccentColor", workspace.accentColor);
                workspaceActivity.putExtra("workspaceName", workspace.name);
                workspaceActivity.putExtra("workspaceInviteCode", workspace.inviteCode);
                workspaceActivity.putExtra("workspaceUsers", workspace.users);
                workspaceActivity.putExtra("workspaceAdmins", workspace.admins);

                activity.startActivity(workspaceActivity);
            }
        });
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

        int userCount = workspace.users.size();

        holder.workspaceInformationTextView.setText(userCount + (userCount == 1 ? " member" : " members"));

        holder.index = position;

        holder.rootView.setCardBackgroundColor(ContextCompat.getColor(activity, Workspace.colors[workspace.accentColor]));

        if (workspace.workspaceIcon != null) {
            holder.workspaceIconImageView.setVisibility(View.VISIBLE);
            holder.workspaceIconImageView.setImageBitmap(workspace.workspaceIcon);
        } else {
            holder.workspaceIconImageView.setVisibility(View.INVISIBLE);
        }
    }

    public int convertDpToPixels(float dips) {
        return (int) (dips * activity.getResources().getDisplayMetrics().density + 0.5f);
    }

    @Override
    public int getItemCount() {
        return workspaces.size();
    }
}
