package sg.edu.np.mad.freeflow;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceSettingsViewHolder extends RecyclerView.ViewHolder {

    Button inviteButton;
    EditText workspaceNameEditText;
    ImageView workspaceImage;
    CardView workspaceNameCard;

    public WorkspaceSettingsViewHolder(View view, WorkspaceSettingsActivity activity, Bundle extras) {
        super(view);

        inviteButton = view.findViewById(R.id.invite_button);
        workspaceNameEditText = view.findViewById(R.id.workspace_name_edit_text);
        workspaceImage = view.findViewById(R.id.workspace_image);
        workspaceNameCard = view.findViewById(R.id.workspace_name_edit_text_card);

        if (inviteButton != null) {
            inviteButton.setTextColor(activity.getResources().getColor(Workspace.colors[extras.getInt("workspaceAccentColor",0)]));
            inviteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent workspaceInviteActivity = new Intent(activity, WorkspaceInviteActivity.class);

                    workspaceInviteActivity.putExtra("workspaceIcon", (Bitmap) extras.getParcelable("workspaceIcon"));
                    workspaceInviteActivity.putExtra("workspaceAccentColor", extras.getInt("workspaceAccentColor"));
                    workspaceInviteActivity.putExtra("workspaceName", extras.getString("workspaceName"));
                    workspaceInviteActivity.putExtra("workspaceInviteCode", extras.getString("workspaceInviteCode"));

                    activity.startActivity(workspaceInviteActivity);
                }
            });
        }
    }
}
