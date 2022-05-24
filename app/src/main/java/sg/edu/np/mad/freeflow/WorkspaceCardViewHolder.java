package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceCardViewHolder extends RecyclerView.ViewHolder {

    View rootView;
    ImageView workspaceIconImageView;
    TextView workspaceNameTextView;
    TextView workspaceInformationTextView;

    public WorkspaceCardViewHolder(View view) {
        super(view);

        rootView = view;

        workspaceIconImageView = view.findViewById(R.id.workspace_image);
        workspaceNameTextView = view.findViewById(R.id.workspace_name);
        workspaceInformationTextView = view.findViewById(R.id.workspace_information);
    }
}
