package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceSettingsAdapter extends RecyclerView.Adapter<WorkspaceSettingsViewHolder> {

    Bundle extras;
    WorkspaceSettingsActivity activity;

    public WorkspaceSettingsAdapter(Bundle extras, WorkspaceSettingsActivity activity) {
        this.extras = extras;
        this.activity = activity;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 0;
        } else {
            return 1;
        }
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
        return new WorkspaceSettingsViewHolder(item, activity, extras);
    }

    @Override
    public void onBindViewHolder(@NonNull WorkspaceSettingsViewHolder holder, int position) {
        if (position == 0) {
            int color = Workspace.colors[extras.getInt("workspaceAccentColor",0)];

            holder.workspaceNameCard.setCardBackgroundColor(ContextCompat.getColor(activity, color));
            if (extras.getParcelable("workspaceIcon") == null) {
                holder.workspaceImage.setImageBitmap(BitmapFactory.decodeResource(activity.getResources(), R.drawable.workspace_icon));
            } else {
                holder.workspaceImage.setImageBitmap((Bitmap) extras.getParcelable("workspaceIcon"));
            }

            holder.workspaceNameEditText.setText(extras.getString("workspaceName"));
        }
    }

    @Override
    public int getItemCount() {
        return 1 + 5;
    }
}
