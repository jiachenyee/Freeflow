package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceSettingsAdapter extends RecyclerView.Adapter<WorkspaceSettingsViewHolder> {
    public WorkspaceSettingsAdapter() {

    }

    @NonNull
    @Override
    public WorkspaceSettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View item;
        if (viewType == 0) {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.workspace_settings_configuration,
                    parent,
                    false);
        } else {
            item = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.workspace_user_card,
                    parent,
                    false);
        }
        return new WorkspaceSettingsViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceSettingsViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }
}
