package sg.edu.np.mad.freeflow;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class WorkspaceCardViewHolder extends RecyclerView.ViewHolder {

    CardView rootView;
    ImageView workspaceIconImageView;
    TextView workspaceNameTextView;
    TextView workspaceInformationTextView;

    OnWorkspaceOpenHandler onWorkspaceOpenHandler;

    int index;

    public WorkspaceCardViewHolder(View view) {
        super(view);

        rootView = (CardView) view;

        workspaceIconImageView = view.findViewById(R.id.workspace_image);
        workspaceNameTextView = view.findViewById(R.id.workspace_name);
        workspaceInformationTextView = view.findViewById(R.id.workspace_information);

        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onWorkspaceOpenHandler != null) {
                    onWorkspaceOpenHandler.onWorkspaceOpen(index);
                }
            }
        });
    }

    public WorkspaceCardViewHolder setOnWorkspaceOpenHandler(OnWorkspaceOpenHandler onWorkspaceOpenHandler) {
        this.onWorkspaceOpenHandler = onWorkspaceOpenHandler;
        return this;
    }


    public interface OnWorkspaceOpenHandler {
        void onWorkspaceOpen(int index);
    }
}
